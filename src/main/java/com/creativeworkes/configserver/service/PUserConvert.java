package com.creativeworkes.configserver.service;

import com.creativeworkes.configserver.entity.PUser;
import com.creativeworkes.configserver.utils.Convertor;

/**
 * 用户转换器
 */
public class PUserConvert implements Convertor<PUser, User> {

  private static PUserConvert instance = null;

  private PUserConvert() {

  }

  public static PUserConvert getInstance() {
    if (instance == null) {
      instance = new PUserConvert();
      return instance;
    }
    return instance;
  }

  @Override
  public User convert(PUser source) {
    User dest = new User();
    dest.setId(source.getId());
    dest.setUsername(source.getUsername());
    dest.setPassword(source.getPassword());
    dest.setEnable(source.isEnable());
    dest.setAccountNonLocked(source.isAccountNonLocked());
    dest.setAccountNonExpired(source.getAccountNonExpired());
    dest.setCredentialsNonExpired(source.getCredentialsNonExpired());

    return dest;
  }

}
