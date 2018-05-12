package com.creativeworkes.configserver.config;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE;
import static org.springframework.http.HttpHeaders.WWW_AUTHENTICATE;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

/**
 * WEB授权配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String[] IGNORE_URIS = new String[] {
    "/**/css/**", "/**/js/**", "/**/img/**", "/**/favicon.ico", "/*.yaml",
    "/*.sh", "/*.bak", "/*.zip"
  };

  protected Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

  @Lazy
  @Autowired
  private UserDetailsService userService;
  @Autowired
  private AppConfiguration appConfiguration;

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(IGNORE_URIS).antMatchers(OPTIONS, "/**");
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.sessionManagement().sessionCreationPolicy(STATELESS).and()
      // Http Basic Authentication
      .httpBasic().and()
      // .httpBasic().authenticationEntryPoint(authenticationEntryPointBean()).and()
      // .addFilterBefore(basicAuthenticationFilterBean(),
      // SecurityContextHolderAwareRequestFilter.class)
      .authorizeRequests().antMatchers("/").anonymous().antMatchers("/index.html")
      .anonymous().anyRequest().authenticated().and()
      // Disable CSRF Protect
      .csrf().disable().headers()
      // X-Frame-Options
      .frameOptions().sameOrigin()
      // X-XSS-Protection
      .xssProtection().xssProtectionEnabled(true)
      // cache-control
      .and().cacheControl().disable()
      // X-Content-Type-Options
      .contentTypeOptions();
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    if (appConfiguration.getAuth().isEnable()) {
      List<AppConfiguration.Auth.User> users = appConfiguration.getAuth().getUsers();
      for (AppConfiguration.Auth.User user : users) {
        String[] dest = new String[user.getRoles().size()];
        auth.inMemoryAuthentication().withUser(user.getUsername())
          .password(user.getPassword()).roles(user.getRoles().toArray(dest));
      }
    } else {
      auth.userDetailsService(userService);
    }
  }

  @Bean
  @Role(ROLE_INFRASTRUCTURE)
  public AuthenticationEntryPoint authenticationEntryPointBean() throws Exception {
    return new ConfigBasicAuthenticationEntryPoint("HeadingConfigServer");
  }

  @Bean
  @Role(ROLE_INFRASTRUCTURE)
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  // @Bean
  // @Role(ROLE_INFRASTRUCTURE)
  // public BasicAuthenticationFilter basicAuthenticationFilterBean() throws
  // Exception {
  // return new BasicAuthenticationFilter(userService,
  // authenticationManagerBean(),
  // authenticationEntryPointBean());
  // }

  /**
   * BasicAuthEntryPoint
   */
  private final class ConfigBasicAuthenticationEntryPoint
    implements AuthenticationEntryPoint, InitializingBean {

    private String realmName;

    public ConfigBasicAuthenticationEntryPoint() {

    }

    public ConfigBasicAuthenticationEntryPoint(String realmName) {
      this();
      this.realmName = realmName;
      logger.info("Initialize BasicAuthenticationEntryPoint With RealName: " + realmName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
      Assert.hasText(realmName, "realmName must be specified");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException e) throws IOException, ServletException {
      response.setStatus(SC_UNAUTHORIZED);
      response.addHeader(WWW_AUTHENTICATE, e.getMessage());
    }
  }

  /**
   * BasicAuth Filter
   */
  private final class BasicAuthenticationFilter extends GenericFilterBean {

    private UserDetailsService service;
    private AuthenticationManager manager;
    private AuthenticationEntryPoint point;

    public BasicAuthenticationFilter(UserDetailsService service,
      AuthenticationManager manager, AuthenticationEntryPoint point) {
      this.service = service;
      this.manager = manager;
      this.point = point;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
      final HttpServletRequest request = (HttpServletRequest) req;
      final HttpServletResponse response = (HttpServletResponse) res;

      response.setContentType("application/json; charset=UTF-8");
      final String header = request.getHeader("Authorization");

      try {

        if (header == null || header.trim().equals("") || !header.startsWith("Basic ")) {
          throw new AuthenticationCredentialsNotFoundException(
            "Basic Auth String Is Required");
        }

        final String[] tokens = extractAndDecodeHeader(header, request);
        assert tokens.length == 2;

        final String username = tokens[0];
        if (tokens[1].trim().equals("")) {
          throw new BadCredentialsException("Basic Auth String's Password Is Request");
        }

        UserDetails user = service.loadUserByUsername(username);
        if (user == null) {
          throw new BadCredentialsException("Invaild Auth String");
        }
        if (!user.getPassword().equals(tokens[1])) {
          throw new BadCredentialsException("Invaild Auth Password");
        }

        final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
          username, tokens[1]);
        authRequest.setDetails(user);
        final Authentication authResult = manager.authenticate(authRequest);

        SecurityContextHolder.getContext().setAuthentication(authResult);

        chain.doFilter(request, response);
      } catch (AuthenticationException failed) {
        SecurityContextHolder.clearContext();
        point.commence(request, response, failed);
      }
    }

    /**
     * Decodes the header into a username and password.
     *
     * @throws BadCredentialsException
     *           if the Basic header is not present or is not valid Base64
     */
    private String[] extractAndDecodeHeader(String header, HttpServletRequest request)
      throws IOException {

      final byte[] base64Token = header.substring(6).getBytes("UTF-8");
      byte[] decoded;
      try {
        decoded = Base64.decode(base64Token);
      } catch (IllegalArgumentException e) {
        throw new BadCredentialsException("Failed to decode basic authentication token");
      }

      final String token = new String(decoded, "UTF-8");

      final int delim = token.indexOf(":");

      if (delim == -1) {
        throw new BadCredentialsException("Invalid basic authentication token");
      }

      return new String[] {
        token.substring(0, delim), token.substring(delim + 1)
      };
    }

    private boolean authenticationIsRequired(String username) {
      final Authentication existingAuth = SecurityContextHolder.getContext()
        .getAuthentication();

      if (existingAuth == null || !existingAuth.isAuthenticated()) {
        return true;
      }

      if (existingAuth instanceof UsernamePasswordAuthenticationToken && !existingAuth.getName()
        .equals(username)) {
        return true;
      }

      if (existingAuth instanceof AnonymousAuthenticationToken) {
        return true;
      }

      return false;
    }
  }
}
