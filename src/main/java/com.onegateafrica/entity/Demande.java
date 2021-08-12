package com.onegateafrica.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "demande")
public class Demande implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nom;
  private String description;
  private String date_debut;

  @Column(name = "statut", nullable = false, columnDefinition = "int default 0")
  private Integer statut;

  @ManyToOne
  @JoinColumn(name = "demandeur_id", referencedColumnName = "id")
  private Demandeur demandeur;

  @ManyToOne
  @JoinColumn(name = "prestataire_id", referencedColumnName = "id")
  private Prestataire prestataire;

  @ManyToOne
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private Service service;

  @ManyToMany(mappedBy = "demande")
  private List<Employe> employes;

  @OneToOne(cascade = CascadeType.REMOVE)
  private Feedback feedback;
}
