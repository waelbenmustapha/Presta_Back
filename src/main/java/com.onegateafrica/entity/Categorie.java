package com.onegateafrica.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Data
@Table(name = "categorie")
public class Categorie implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;

  private String icone;

  @OneToMany(targetEntity = Service.class, mappedBy = "cat", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Service> services;

  @JsonManagedReference
  public List<Service> getServices() {
    return services;
  }
}
