package com.onegateafrica.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onegateafrica.entity.Demandeur;

@Repository
public interface DemandeurRepository extends CrudRepository<Demandeur, Long> {


  @Query("SELECT c FROM Demandeur c WHERE c.email = ?1")
  Demandeur findByEmail(String email);

  Demandeur findByToken(String token);

  @Query(value = "SELECT count(*) FROM demandeur ", nativeQuery = true)
  int countusers();
  @Query("SELECT u FROM Demandeur u WHERE u.verificationCodeForPasswordChange = ?1")
  Demandeur findByVerificationCodeForPasswordChange(String code);

  @Query(value = "SELECT * FROM demandeur d WHERE d.role='Demandeur'", nativeQuery = true)
  Iterable<Demandeur> findALlDemandeurs();
}
