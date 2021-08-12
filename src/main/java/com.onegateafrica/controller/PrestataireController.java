package com.onegateafrica.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onegateafrica.entity.Prestataire;
import com.onegateafrica.repository.PrestataireRepository;
import com.onegateafrica.service.PrestataireService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/prestataire")
public class PrestataireController {

  private final PrestataireService prestataireService;
  private final PrestataireRepository prestataireRepository;

  @Autowired
  public PrestataireController(PrestataireService prestataireService, PrestataireRepository prestataireRepository) {
    this.prestataireService = prestataireService;
    this.prestataireRepository = prestataireRepository;
  }

  @PostMapping("/prestataireExistant")
  public ResponseEntity prestataireExistant(@RequestBody String email) {
    try {
      if (email != null) {
        Prestataire prestataireExistant = prestataireRepository.findByEmail(email);
        if (prestataireExistant == null) {
          return new ResponseEntity<>(HttpStatus.OK);
        } else {
          return ResponseEntity.status(HttpStatus.OK).body("Compte existant");
        }
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PostMapping("/ajouterPrestataire/{id}")
  public ResponseEntity<Object> ajouterPrestataire(@Validated @RequestBody Prestataire prestataire, @PathVariable Long id)
      throws UnsupportedEncodingException, MessagingException {

    prestataireService.create(prestataire, id);
    return ResponseEntity.status(HttpStatus.OK).body("ALL GOOD");

  }

  @GetMapping("/populaire")
  public ResponseEntity populaire() {

    return new ResponseEntity(prestataireRepository.finpopulairePrestataire(), HttpStatus.OK);

  }

  @GetMapping("/recherchePrestataireByNom/{nom}")
  public ResponseEntity recherchePrestataireByNom(@PathVariable("nom") String nom) {
    try {
      if (nom != null) {
        List<Prestataire> Prestataires = prestataireRepository.findByNomContaining(nom);
        return new ResponseEntity<>(Prestataires, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get prestataire by nom");
    }

  }


  @GetMapping("/getAllPrestataires")
  public ResponseEntity getAllPrestataires() {
    try {
      Iterable<Prestataire> prestataires = prestataireRepository.findALlPrestataires();
      return new ResponseEntity<>(prestataires, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get All Prestataires");

    }
  }

  @GetMapping("/getPrestataire/{id}")
  public ResponseEntity getPrestataire(@PathVariable("id") Long id) {
    if (id != null) {
      Prestataire prestataire = prestataireRepository.findById(id).get();
      return new ResponseEntity<>(prestataire, HttpStatus.OK);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id Invalide");
    }
  }

  @DeleteMapping("/supprimerPrestataire/{id}")
  public ResponseEntity supprimerPrestataire(@PathVariable("id") Long id) {
    if (id != null) {
      prestataireRepository.deleteById(id);
      return ResponseEntity.status(HttpStatus.OK).body("Prestataire supprimé");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalide");
    }
  }

  @PutMapping("/valider/{id}")
  public ResponseEntity modifierCategorie(@PathVariable("id") Long id) {
    if (id != null) {
      Prestataire prestataire = prestataireRepository.findById(id).get();
      prestataire.setValidation(1);
      prestataireRepository.save(prestataire);
      return ResponseEntity.status(HttpStatus.OK).body("Service validé");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalide");
    }
  }

}