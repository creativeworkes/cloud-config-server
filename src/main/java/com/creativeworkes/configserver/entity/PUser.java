package com.creativeworkes.configserver.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 用户表实体
 */
@Entity
@Table(name = "user")
public class PUser {

  private String id;
  private String username;
  private String password;
  private Boolean enable;
  private Boolean accountNonLocked;
  private Boolean accountNonExpired;
  private Boolean credentialsNonExpired;

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @Column(name = "id", length = 64, nullable = false)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(name = "username", length = 64, nullable = false)
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Column(name = "password", length = 64, nullable = false)
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Column(name = "enable", nullable = false)
  public Boolean isEnable() {
    return enable;
  }

  public void setEnable(Boolean enable) {
    this.enable = enable;
  }

  @Column(name = "account_non_locked", nullable = false)
  public Boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  public void setAccountNonLocked(Boolean locked) {
    this.accountNonLocked = locked;
  }

  @Column(name = "account_non_expired", nullable = false)
  public Boolean getAccountNonExpired() {
    return accountNonExpired;
  }

  public void setAccountNonExpired(Boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  @Column(name = "credential_non_expired", nullable = false)
  public Boolean getCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }
}
