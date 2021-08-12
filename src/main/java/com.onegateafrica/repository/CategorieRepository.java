package com.onegateafrica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onegateafrica.entity.Categorie;

@Repository
public interface CategorieRepository extends CrudRepository<Categorie, Long> {

  @Query(value = "SELECT * FROM categorie ORDER BY RAND() LIMIT 4", nativeQuery = true)
  List<Categorie> finpopulairecategorie();
  Categorie findByNom(String nom);
  @Query(value = "SELECT count(*) FROM categorie ", nativeQuery = true)
  int countcategories();

}
