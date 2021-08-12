package com.onegateafrica.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "entreprise")
@DiscriminatorValue("Entreprise")
public class Entreprise extends Prestataire {

  private String matfisc;

  @JsonIgnore
  @OneToMany(mappedBy = "entreprise")

  private List<Employe> employes;

}
