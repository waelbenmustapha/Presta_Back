package com.onegateafrica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdministrateurService {
  @Autowired
  private PasswordEncoder passwordEncoder;

  public Boolean matchPasswords(String rawPassword, String encodedPassword) {
    return (passwordEncoder.matches(rawPassword, encodedPassword));
  }

}
