package com.onegateafrica.controller;

import java.util.List;
import java.util.Optional;

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

import com.onegateafrica.entity.Categorie;
import com.onegateafrica.entity.Service;
import com.onegateafrica.repository.CategorieRepository;
import com.onegateafrica.service.CategorieService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/categorie")
public class CategorieController {

  private final CategorieService categorieService;
  private final CategorieRepository categorieRepository;

  @Autowired
  public CategorieController(CategorieService categorieService, CategorieRepository categorieRepository) {
    this.categorieService = categorieService;
    this.categorieRepository = categorieRepository;
  }

  @GetMapping("/getAllCategories")
  public ResponseEntity getAllCategories() {
    try {
      Iterable<Categorie> categories = categorieRepository.findAll();
      return new ResponseEntity<>(categories, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get All Categories");

    }
  }

  @GetMapping("/populaire")
  public ResponseEntity populaire() {

    return new ResponseEntity(categorieRepository.finpopulairecategorie(), HttpStatus.OK);

  }

  @GetMapping("/getCategorie/{id}")
  public ResponseEntity getCategorieById(@PathVariable("id") Long id) {
    try {
      if (id != null) {
        Optional<Categorie> categorie = categorieRepository.findById(id);
        if (categorie.isPresent()) {
          return new ResponseEntity<>(categorie.get(), HttpStatus.OK);
        } else {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Id");
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get Categorie by id");
    }
  }

  @PostMapping("/ajouterCategorie")
  public ResponseEntity ajouterCategorie(@Validated @RequestBody Categorie categorie ){
    if (categorie != null){
      categorieRepository.save(categorie);
      return ResponseEntity.status(HttpStatus.OK).body("Catégorie ajoutée");
    }else{
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalide");
    }
  }

  @DeleteMapping("/supprimerCategorie/{id}")
  public ResponseEntity supprimerCategorie( @PathVariable("id") Long id ){
    if (id != null){
      categorieRepository.deleteById(id);
      return ResponseEntity.status(HttpStatus.OK).body("Catégorie supprimée");
    }else{
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalide");
    }
  }

  @PutMapping("/modifierCategorie/{id}")
  public ResponseEntity modifierCategorie( @PathVariable("id") Long id,@RequestBody Categorie categorie ){
    if (id != null){
      Categorie categorie1 = categorieRepository.findById(id).get();
      categorie1.setNom(categorie.getNom());
      categorie1.setIcone(categorie.getIcone());
      categorieRepository.save(categorie1);
      return ResponseEntity.status(HttpStatus.OK).body("Catégorie modifiée");
    }else{
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalide");
    }
  }
@GetMapping("/countcategories")
public ResponseEntity countcategories()
{
  return new ResponseEntity(categorieRepository.countcategories(), HttpStatus.OK);

}
  @GetMapping("/getServiceByCategorie/{id}")
  public ResponseEntity getServiceByCategorie(@PathVariable("id") Long id) {

    try {
      if (id != null) {
        List<Service> services = categorieService.getServiceCategorie(id);
        if (!services.isEmpty()) {
          return new ResponseEntity<>(services, HttpStatus.OK);
        } else {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request ...");
    }
  }
}


