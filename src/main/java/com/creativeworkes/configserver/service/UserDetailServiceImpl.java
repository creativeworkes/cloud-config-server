package com.creativeworkes.configserver.service;

import com.creativeworkes.configserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.creativeworkes.configserver.entity.PUser;

/**
 * 用户服务
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    PUser pu = userRepository.findByUsername(username);
    if (pu == null) {
      return null;
    }

    return PUserConvert.getInstance().convert(pu);
  }
}
