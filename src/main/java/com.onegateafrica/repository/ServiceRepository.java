package com.onegateafrica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onegateafrica.entity.Service;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Long> {

  @Query("SELECT d FROM Service d WHERE d.description LIKE %?1% or d.nom LIKE %?1%")
  List<Service> getServiceByKeyword(String keyword);
  @Query(value = "SELECT count(*) FROM service ", nativeQuery = true)
  int countservices();
  @Query(value = "SELECT * FROM Service ORDER BY RAND() LIMIT 5", nativeQuery = true)
  List<Service> findPopulaireService();

}
