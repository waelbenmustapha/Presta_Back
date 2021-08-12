package com.onegateafrica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onegateafrica.entity.Prestataire;
import com.onegateafrica.repository.ServiceRepository;

@Service
public class ServiceService {

  private final ServiceRepository serviceRepository;

  @Autowired
  public ServiceService(ServiceRepository serviceRepository) {
    this.serviceRepository = serviceRepository;
  }


  public List<Prestataire> getServicePrestataire(Long id) {

    List<Prestataire> liste_prestataires = serviceRepository.findById(id).get().getPrestataires();
    return liste_prestataires;
  }
}
