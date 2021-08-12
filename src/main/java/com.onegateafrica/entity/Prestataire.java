package com.onegateafrica.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "prestataire")
@DiscriminatorValue("Prestataire")
public class Prestataire extends Demandeur {

  @JsonIgnore
  @OneToMany(mappedBy = "prestataire")
  private List<Demande> demandes;

  private String adresse;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
      name = "Prestataire_Service",
      joinColumns = @JoinColumn(name = "Prestataire_id"),
      inverseJoinColumns = @JoinColumn(name = "Service_id")


  )
  private List<Service> service = new ArrayList<>();
  private float note = 0;

  private String image_verification1;
  private String image_verification2;
  private Double latitude;
  private Double longitude;
  public Prestataire() {
  }

  @JsonIgnore
  public List<Service> getServices() {
    return service;
  }
}
