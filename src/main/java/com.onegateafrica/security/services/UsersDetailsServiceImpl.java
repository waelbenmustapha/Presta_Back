package com.onegateafrica.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.onegateafrica.entity.Demandeur;
import com.onegateafrica.repository.DemandeurRepository;

public class UsersDetailsServiceImpl implements UserDetailsService {

  public final DemandeurRepository demandeurRepository;

  @Autowired
  public UsersDetailsServiceImpl(DemandeurRepository demandeurRepository) {
    this.demandeurRepository = demandeurRepository;
  }


  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Demandeur demandeur = demandeurRepository.findByEmail(username);
    return UserDetailsImpl.build(demandeur);
  }

}
