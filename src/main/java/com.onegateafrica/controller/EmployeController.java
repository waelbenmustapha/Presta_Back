package com.onegateafrica.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import com.onegateafrica.entity.Demande;
import com.onegateafrica.entity.Employe;
import com.onegateafrica.repository.DemandeRepository;
import com.onegateafrica.repository.EmployeRepository;
import com.onegateafrica.repository.ServiceRepository;
import com.onegateafrica.service.EmployeService;

import net.bytebuddy.utility.RandomString;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/employe")
public class EmployeController {


  private final EmployeRepository employeRepository;
  private final ServiceRepository serviceRepository;
  private final DemandeRepository demandeRepository;
  private final EmployeService employeService;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  public EmployeController(EmployeRepository employeRepository, ServiceRepository serviceRepository, DemandeRepository demandeRepository,
      EmployeService employeService) {
    this.employeRepository = employeRepository;
    this.serviceRepository = serviceRepository;
    this.demandeRepository = demandeRepository;
    this.employeService = employeService;
  }


  @GetMapping("/getAllEmployes")
  public ResponseEntity getAllEmployes() {
    try {
      Iterable<Employe> employes = employeRepository.findALlEmployes();
      return new ResponseEntity<>(employes, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get All Employés");

    }
  }

  @GetMapping("/getEmploye/{id}")
  public ResponseEntity getEmploye(@PathVariable("id") Long id) {
    if (id != null) {
      Employe employe = employeRepository.findById(id).get();
      return new ResponseEntity<>(employe, HttpStatus.OK);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id Invalide");
    }
  }

  @PutMapping("/modifierEmploye")
  public void modifierEmploye(@RequestBody Employe emp) {
    Employe employe = employeRepository.findById(emp.getId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:"));
    employe.setNom(emp.getNom());
    employe.setEmail(emp.getEmail());
    employe.setAddresse(emp.getAddresse());
    employe.setNumero_telephone(emp.getNumero_telephone());
    employeRepository.save(employe);
  }

  @PostMapping("/ajouterEmploye")
  public Employe ajouterEmploye(@Validated @RequestBody Employe employe) {
    if (employe != null) {
      Employe employetoadd = new Employe();
      employetoadd.setNom(employe.getNom());
      employetoadd.setEmail(employe.getEmail());
      employetoadd.setNumero_telephone(employe.getNumero_telephone());
      employetoadd.setNotification_token(employe.getNotification_token());
      String randomCode2 = RandomString.make(64);
      employetoadd.setToken(randomCode2);
      if (employe.getMot_de_passe() != null) {
        employetoadd.setMot_de_passe(employeService.encodePasswordEmploye(employe));
      }
      employetoadd.setEntreprise(employe.getEntreprise());
      employetoadd.setAddresse(employe.getAddresse());
      return employeRepository.save(employetoadd);
    }
    return null;
  }


  @PutMapping("/affecterDemandeEmploye/{id}/{idDemande}")
  public ResponseEntity affecterDemandeEmploye(@PathVariable("id") Long id, @PathVariable("idDemande") Long idDemande) {
    Employe employe = employeRepository.findById(id).get();
    Demande demande = demandeRepository.findById(idDemande).get();
    if (!employe.getDemande().contains(demande) && !demande.getEmployes().contains(employe)) {
      employe.getDemande().add(demande);
      demande.getEmployes().add(employe);
      employeRepository.save(employe);
      demandeRepository.save(demande);
    }
    return ResponseEntity.status(HttpStatus.OK).body("ok");

  }

  @GetMapping("/avoirDemandesEnCours/{id}")
  public ResponseEntity avoirDemandesEnCours(@PathVariable("id") Long id) {

    Optional<Employe> employe = employeRepository.findById(id);
    int i = 0;
    boolean ademande = false;
    while (i < employe.get().getDemande().size() && ademande == false) {
      if (employe.get().getDemande().get(i).getStatut() == 2) {
        ademande = true;
      }
    }
    return new ResponseEntity<>(ademande, HttpStatus.OK);

  }

  @DeleteMapping("/supprimerEmploye/{id}")
  public ResponseEntity deleteEmploye(@PathVariable Long id) {
    if (id != null) {
      Employe employe = employeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
      employeRepository.delete(employe);
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id can't be null");
    }
  }
}
