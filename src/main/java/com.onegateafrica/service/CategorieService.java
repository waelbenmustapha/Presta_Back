package com.onegateafrica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.onegateafrica.entity.Categorie;
import com.onegateafrica.entity.Service;
import com.onegateafrica.repository.CategorieRepository;

@org.springframework.stereotype.Service
public class CategorieService {

  private final CategorieRepository categorieRepository;

  @Autowired
  public CategorieService(CategorieRepository categorieRepository) {
    this.categorieRepository = categorieRepository;
  }

  public List<Service> getServiceCategorie(Long id) {

    Categorie categorie = categorieRepository.findById(id).orElse(new Categorie());
    return categorie.getServices();
  }
}
