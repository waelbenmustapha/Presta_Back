package com.onegateafrica.controller;

import java.util.List;

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

import com.onegateafrica.entity.Employe;
import com.onegateafrica.entity.Entreprise;
import com.onegateafrica.repository.EntrepriseRepository;
import com.onegateafrica.repository.ServiceRepository;
import com.onegateafrica.service.EntrepriseService;

import net.bytebuddy.utility.RandomString;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/entreprise")
public class EntrepriseController {


  private final EntrepriseRepository entrepriseRepository;
  private final EntrepriseService entrepriseService;
private final ServiceRepository serviceRepository;
  @Autowired
  public EntrepriseController(ServiceRepository serviceRepository,EntrepriseRepository entrepriseRepository, EntrepriseService entrepriseService) {
    this.entrepriseRepository = entrepriseRepository;
    this.entrepriseService = entrepriseService;
    this.serviceRepository=serviceRepository;
  }


  @GetMapping("/getAllEntreprises")
  public ResponseEntity getAllEntreprises() {
    try {
      Iterable<Entreprise> entreprises = entrepriseRepository.findALlEntreprises();
      return new ResponseEntity<>(entreprises, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get All Entreprises");

    }
  }

  @GetMapping("/getEntreprise/{id}")
  public ResponseEntity getEntreprise(@PathVariable("id") Long id) {
    if (id != null) {
      Entreprise entreprise = entrepriseRepository.findById(id).get();
      return new ResponseEntity<>(entreprise, HttpStatus.OK);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id Invalide");
    }
  }

  @DeleteMapping("/supprimerEntreprise/{id}")
  public ResponseEntity supprimerEntreprise(@PathVariable("id") Long id) {
    if (id != null) {
      entrepriseRepository.deleteById(id);
      return ResponseEntity.status(HttpStatus.OK).body("Entreprise supprimée");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalide");
    }
  }

  @PutMapping("/valider/{id}")
  public ResponseEntity modifierCategorie(@PathVariable("id") Long id) {
    if (id != null) {
      Entreprise prestataire = entrepriseRepository.findById(id).get();
      prestataire.setValidation(1);
      entrepriseRepository.save(prestataire);
      return ResponseEntity.status(HttpStatus.OK).body("Service validé");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalide");
    }
  }

  @PostMapping("/ajouterEntreprise/{id}")
  public Entreprise ajouterEntreprise(@Validated @RequestBody Entreprise entreprise,@PathVariable Long id) {
    Entreprise entrepriseToAdd = new Entreprise();
    if (entrepriseRepository.findByEmail(entreprise.getEmail()) == null) {
      entrepriseToAdd.setNom(entreprise.getNom());
      entrepriseToAdd.setEmail(entreprise.getEmail());
      entrepriseToAdd.setMatfisc(entreprise.getMatfisc());
      entrepriseToAdd.getService().add(serviceRepository.findById(id).get());
      entrepriseToAdd.setNumero_telephone(entreprise.getNumero_telephone());
      entrepriseToAdd.setLatitude(entreprise.getLatitude());
      String randomCode = RandomString.make(64);
      entrepriseToAdd.setToken(randomCode);
      entrepriseToAdd.setImage_verification1(entreprise.getImage_verification1());
      entrepriseToAdd.setLongitude(entreprise.getLongitude());
      entrepriseToAdd.setMot_de_passe(entreprise.getMot_de_passe());
      entrepriseToAdd.setAdresse(entreprise.getAdresse());
      if (entreprise.getMot_de_passe() != null) {
        entrepriseToAdd.setMot_de_passe(entrepriseService.encodePasswordConsommateur(entreprise));
      }
      return entrepriseRepository.save(entrepriseToAdd);
    }
    return entrepriseRepository.save(entrepriseToAdd);
  }

  @PostMapping("/entrepriseExistant")
  public ResponseEntity entrepriseExistant(@RequestBody String email) {
    try {
      if (email != null) {
        Entreprise entrepriseExistant = entrepriseRepository.findByEmail(email);
        if (entrepriseExistant == null) {
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


  @GetMapping("/getListeDesEmployes/{id}")
  public List<Employe> getListeDesEmployes(@PathVariable("id") Long id) {

    return entrepriseService.getListdesEmployee(id);

  }

 /* @PostMapping("/processRegister")
  public Entreprise processRegister(@RequestBody Entreprise user) throws UnsupportedEncodingException, MessagingException {
    if (entrepriseService.register(user)) {
      return user;
    } else {
      return null;
    }
  }

  @GetMapping("/verifyUser/{code}")
  public String verifyUser(@PathVariable String code) {
    if (entrepriseService.verify(code)) {
      return "verify_success";
    } else {
      return "verify_fail";
    }

  }
*/
}
