package com.onegateafrica.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "Employe")
public class Employe {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nom;
  private String email;
  private String mot_de_passe;
  private String notification_token;
  private String numero_telephone;
  private String image = "https://i.postimg.cc/c1Psyksx/kisspng-computer-icons-laborer-employee-5b078584c8efc4-6088737915272195888231.png";
  private String addresse;
  private String token;
  @ManyToOne
  @JoinColumn(name = "Entreprise_id", referencedColumnName = "id")
  private Demandeur entreprise;
  private String Role = "Employe";
  @ManyToMany
  @JsonIgnore
  private List<Demande> demande;
}
