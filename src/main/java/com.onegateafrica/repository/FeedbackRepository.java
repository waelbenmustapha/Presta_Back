package com.onegateafrica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onegateafrica.entity.Demande;
import com.onegateafrica.entity.Feedback;

@Repository
public interface FeedbackRepository extends CrudRepository<Feedback, Long> {

  @Query("SELECT c FROM Feedback c WHERE c.demande = ?1")
  Feedback findByDemande(Demande demande);
  @Query(value = "SELECT count(*) FROM feedback ", nativeQuery = true)
  int countfeedback();
  List<Feedback> findByDemandePrestataireId(Long id);

}
