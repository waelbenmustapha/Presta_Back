package com.onegateafrica.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onegateafrica.entity.Administrateur;

@Repository
public interface AdministrateurRepository extends CrudRepository<Administrateur, Long> {

  Administrateur findByToken(String token);

  Administrateur findByEmail(String email);
}
