package com.onegateafrica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onegateafrica.entity.Employe;

@Service
public class EmployeService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Boolean matchPasswords(String rawPassword, String encodedPassword) {
    return (passwordEncoder.matches(rawPassword, encodedPassword));
  }

  public String encodePasswordEmploye(Employe employe) {
    String encodedPassword = passwordEncoder.encode(employe.getMot_de_passe());
    return encodedPassword;
  }

}
