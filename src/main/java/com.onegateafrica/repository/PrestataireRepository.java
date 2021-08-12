package com.onegateafrica.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onegateafrica.entity.Prestataire;

@Repository
public interface PrestataireRepository extends CrudRepository<Prestataire, Long> {

  List<Prestataire> findByNomContaining(String nom);

  Prestataire findByEmail(String email);

  @Query(value = "SELECT * FROM Demandeur where role='Prestataire' or role='Entreprise' ORDER BY note DESC LIMIT 5", nativeQuery = true)
  List<Prestataire> finpopulairePrestataire();

  @Modifying
  @Transactional
  @Query(value = "update Demandeur c set c.note=(select avg(note) from feedback as f,demande as d where f.demande_id=d.id and d.prestataire_id=:id) where c.id=:id", nativeQuery = true)
  int updatenote(@Param("id") Long id);

  @Query(value = "SELECT * FROM demandeur d WHERE d.role='Prestataire'", nativeQuery = true)
  Iterable<Prestataire> findALlPrestataires();
}

