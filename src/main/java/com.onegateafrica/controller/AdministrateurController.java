package com.onegateafrica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onegateafrica.entity.Administrateur;
import com.onegateafrica.repository.AdministrateurRepository;
import com.onegateafrica.service.AdministrateurService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController

@RequestMapping("/admin")
public class AdministrateurController {

  private final AdministrateurRepository administrateurRepository;
  private final AdministrateurService administrateurService;

  @Autowired
  public AdministrateurController(AdministrateurRepository administrateurRepository,
      AdministrateurService administrateurService) {
    this.administrateurRepository = administrateurRepository;
    this.administrateurService = administrateurService;
  }

  @PostMapping("/loginAdministrateur")
  public ResponseEntity loginAdministrateur(@Validated @RequestBody Object objet) {
    try {
      if (objet != null) {
        ObjectMapper mapper = new ObjectMapper();
        Administrateur administrateur = mapper.convertValue(objet, Administrateur.class);
        Administrateur administrateurReel = administrateurRepository.findByEmail(administrateur.getEmail());
        if (administrateurReel != null && administrateurService
            .matchPasswords(administrateur.getMot_de_passe(), administrateurReel.getMot_de_passe())) {
          return new ResponseEntity<>(administrateurReel.getToken(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faux administrateur");
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Données ne doivent pas être null");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/consulterAdministrateur/{token}")
  public ResponseEntity userbytoken(@PathVariable String token) {

    if (token != null) {
      try {

        Administrateur administrateur = administrateurRepository.findByToken(token);
        if (administrateur != null) {
          return new ResponseEntity(administrateur, HttpStatus.OK);
        }
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get consommateur info");

      }
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("token can't be null");
  }
}
