package com.onegateafrica.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onegateafrica.entity.Employe;

@Repository
public interface EmployeRepository extends CrudRepository<Employe, Long> {

  Employe findByToken(String id);

  @Query("SELECT c FROM Employe c WHERE c.email = ?1")
  Employe findByEmail(String email);

  @Query(value = "SELECT * FROM employe e", nativeQuery = true)
  Iterable<Employe> findALlEmployes();
}
