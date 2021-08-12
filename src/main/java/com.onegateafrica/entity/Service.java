package com.onegateafrica.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@Entity
@Data
@Table(name = "service")
public class Service implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;

  private String description;

  private String icone;

  @ManyToOne
  @JoinColumn(name = "categorie_id")
  private Categorie cat;
  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "service")
  private List<Prestataire> prestataires;
  @JsonIgnore
  @OneToMany(mappedBy = "service")
  private List<Demande> Demandes;

  public Service() {
  }

  @JsonBackReference
  public Categorie getCat() {
    return cat;
  }

  @JsonIgnore
  public List<Prestataire> getPrestataire() {
    return prestataires;
  }
}
