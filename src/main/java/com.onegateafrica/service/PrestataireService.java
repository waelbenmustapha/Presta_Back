package com.onegateafrica.service;


import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onegateafrica.entity.Demandeur;
import com.onegateafrica.entity.Prestataire;
import com.onegateafrica.repository.DemandeurRepository;
import com.onegateafrica.repository.PrestataireRepository;
import com.onegateafrica.repository.ServiceRepository;

import net.bytebuddy.utility.RandomString;

@Service
public class PrestataireService {

  private final ServiceRepository serviceRepository;
  private final PrestataireRepository prestataireRepository;
private final DemandeurRepository demandeurRepository;
  @Autowired
  public PrestataireService(ServiceRepository serviceRepository,DemandeurRepository demandeurRepository,PrestataireRepository prestataireRepository) {
    this.prestataireRepository = prestataireRepository;
    this.demandeurRepository=demandeurRepository;
    this.serviceRepository = serviceRepository;
  }

  @Autowired
  private PasswordEncoder passwordEncoder;

  public String getPrestataireNotificationToken(Long id) {
    Prestataire PressToken = prestataireRepository.findById(id).orElse(new Prestataire());
    return PressToken.getNotification_token();

  }

  public String encodePasswordPrestataire(Prestataire prestataire) {
    String encodedPassword = passwordEncoder.encode(prestataire.getMot_de_passe());
    return encodedPassword;
  }

  public void savePrestataireNotificationToken(Long id, String token) {
    Prestataire prestataire = prestataireRepository.findById(id).orElse(new Prestataire());
    prestataire.setNotification_token(token);
    prestataireRepository.save(prestataire);
  }

  public Boolean create(Prestataire prestataire, Long id)
      throws UnsupportedEncodingException, MessagingException {
    Demandeur prestataireExistant = demandeurRepository.findByEmail(prestataire.getEmail());
    if (prestataireExistant != null) {
    return false;
    } else {
      prestataire.setMot_de_passe(encodePasswordPrestataire(prestataire));

      String randomCode = RandomString.make(64);

      prestataire.setToken(randomCode);
      prestataire.setValidation(0);

      prestataire.getService().add(serviceRepository.findById(id).get());

      prestataireRepository.save(prestataire);

      return true;
    }
  }

}
