package com.onegateafrica.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "Demandeur")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "Role",
    discriminatorType = DiscriminatorType.STRING
)
@DiscriminatorValue("Demandeur")

public class Demandeur implements Serializable {

  @JsonIgnore
  @OneToMany(mappedBy = "demandeur")
  private List<Demande> Demandes;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;

  private String email;

  @Column(name = "Role", insertable = false, updatable = false)
  private String Role = getClass().getAnnotation(DiscriminatorValue.class).value();

  private String mot_de_passe;

  private String numero_telephone;

  private String notification_token;

  private String image = "https://www.tenforums.com/geek/gars/images/2/types/thumb_15951118880user.png";

  private int validation;

  @Column(name = "verification_code_for_password_change", length = 32)
  private String verificationCodeForPasswordChange;

  private String token;


  public Demandeur() {
  }

}
