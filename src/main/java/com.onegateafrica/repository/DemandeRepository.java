package com.onegateafrica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onegateafrica.entity.Demande;

@Repository
public interface DemandeRepository extends CrudRepository<Demande, Long> {

  Optional<Demande> findById(Long id);
Demande findByNom (String nom);
  List<Demande> findByPrestataireIdAndFeedbackNotNull(Long id);

  List<Demande> findByPrestataireId(Long id);

  List<Demande> findByEmployesIdAndStatut(Long id, Integer i);

  List<Demande> findByDemandeurId(Long id);

  @Query(value = "SELECT nom,count(nom) as 'count' FROM presta.demande group by nom", nativeQuery = true)
 List<Res> demperserv();

  public static interface Res {

    String getNom();

    String getCount();

  }
}
