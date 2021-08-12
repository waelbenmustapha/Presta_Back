package com.onegateafrica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onegateafrica.entity.Demande;
import com.onegateafrica.repository.DemandeRepository;


@Service
public class DemandeService {

  private final DemandeRepository demandeRepository;

  @Autowired
  public DemandeService(DemandeRepository demandeRepository) {
    this.demandeRepository = demandeRepository;
  }

  public void accepterourefuser(Long id, Integer statut) {
    Demande dem = demandeRepository.findById(id).orElse(new Demande());
    dem.setStatut(statut);
    demandeRepository.save(dem);
  }

}
