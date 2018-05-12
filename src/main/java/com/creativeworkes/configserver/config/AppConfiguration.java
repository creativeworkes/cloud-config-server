package com.creativeworkes.configserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

/**
 * 项目自定义配置
 *
 * @author Yeqinglan
 */
@Component
@ConfigurationProperties(prefix = "app", ignoreInvalidFields = true)
public class AppConfiguration {

  private Auth auth;

  public Auth getAuth() {
    return auth;
  }

  public void setAuth(Auth auth) {
    this.auth = auth;
  }

  public final static class Auth {
    private List<User> users = newArrayList();
    private boolean enable = false;

    public List<User> getUsers() {
      return users;
    }

    public void setUsers(List<User> users) {
      this.users = users;
    }

    public boolean isEnable() {
      return enable;
    }

    public void setEnable(boolean enable) {
      this.enable = enable;
    }

    public final static class User {
      private String username;
      private String password;
      private List<String> roles = newArrayList();

      public User() {
      }

      public User(String username, String password, List<String> roles) {
        this();
        this.username = username;
        this.password = password;
        this.roles = roles;
      }

      public String getUsername() {
        return username;
      }

      public void setUsername(String username) {
        this.username = username;
      }

      public String getPassword() {
        return password;
      }

      public void setPassword(String password) {
        this.password = password;
      }

      public List<String> getRoles() {
        return roles;
      }

      public void setRoles(List<String> roles) {
        this.roles = roles;
      }
    }
  }

}
