package com.onegateafrica.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onegateafrica.entity.Entreprise;

@Repository
public interface EntrepriseRepository extends CrudRepository<Entreprise, Long> {

  @Query("SELECT c FROM Entreprise c WHERE c.email = ?1")
  Entreprise findByEmail(String email);

  @Query(value = "SELECT * FROM demandeur d WHERE d.role='Entreprise'", nativeQuery = true)
  Iterable<Entreprise> findALlEntreprises();

}
