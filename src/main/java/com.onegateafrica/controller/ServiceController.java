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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onegateafrica.entity.Categorie;
import com.onegateafrica.entity.Prestataire;
import com.onegateafrica.entity.Service;
import com.onegateafrica.repository.CategorieRepository;
import com.onegateafrica.repository.ServiceRepository;
import com.onegateafrica.service.ServiceService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/service")
public class ServiceController {

  private final ServiceService serviceService;
  private final ServiceRepository serviceRepository;
  private final CategorieRepository categorieRepository;

  @Autowired
  public ServiceController(ServiceService serviceService, ServiceRepository serviceRepository,
      CategorieRepository categorieRepository) {
    this.serviceService = serviceService;
    this.serviceRepository = serviceRepository;
    this.categorieRepository = categorieRepository;
  }


  @GetMapping("/populaire")
  public ResponseEntity populaire() {

    return new ResponseEntity(serviceRepository.findPopulaireService(), HttpStatus.OK);

  }

  @GetMapping("/getServiceByNom")
  public ResponseEntity getServiceByNom(@RequestParam String keyword) {
    try {
      if (keyword != null) {
        List<Service> service = serviceRepository.getServiceByKeyword(keyword);

        return new ResponseEntity<>(service, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get prestations");
    }

  }


  @GetMapping("/getPrestataireByService/{id}")
  public ResponseEntity getPrestataireByService(@PathVariable Long id) {
    try {
      if (id != null) {
        List<Prestataire> prestation = serviceService.getServicePrestataire(id);

        return new ResponseEntity<>(prestation, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get prestataires de prestation");
    }
  }


  @GetMapping("/getAllServices")
  public ResponseEntity getAllServices() {
    try {
      Iterable<Service> services = serviceRepository.findAll();
      return new ResponseEntity<>(services, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get All Services");

    }
  }

  @GetMapping("/getService/{id}")
  public ResponseEntity getService(@PathVariable("id") Long id) {
    if (id != null) {
      Service service = serviceRepository.findById(id).get();
      return new ResponseEntity<>(service, HttpStatus.OK);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id Invalide");
    }
  }


  @PostMapping("/ajouterService/{id}")
  public ResponseEntity ajouterService(@Validated @RequestBody Service service, @PathVariable("id") Long id) {
    if (service != null && id != null) {
      Categorie categorie = categorieRepository.findById(id).get();
      service.setCat(categorie);
      serviceRepository.save(service);
      return ResponseEntity.status(HttpStatus.OK).body("Service ajouté");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Service invalide");
    }
  }

  @DeleteMapping("/supprimerService/{id}")
  public ResponseEntity supprimerService(@PathVariable("id") Long id) {
    if (id != null) {
      serviceRepository.deleteById(id);
      return ResponseEntity.status(HttpStatus.OK).body("Service supprimé");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalide");
    }
  }
  @GetMapping("/countservices")
  public ResponseEntity countservices()
  {
    return new ResponseEntity(serviceRepository.countservices(), HttpStatus.OK);

  }
  @PutMapping("/modifierService/{id}")
  public ResponseEntity modifierCategorie(@PathVariable("id") Long id, @RequestBody Service service) {
    if (id != null) {
      Service service1 = serviceRepository.findById(id).get();
      service1.setNom(service.getNom());
      service1.setDescription(service.getDescription());
      service1.setIcone(service.getIcone());
      serviceRepository.save(service1);
      return ResponseEntity.status(HttpStatus.OK).body("Service modifié");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalide");
    }
  }
}
