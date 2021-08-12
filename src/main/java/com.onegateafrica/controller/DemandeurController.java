package com.onegateafrica.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onegateafrica.entity.Demandeur;
import com.onegateafrica.entity.Employe;
import com.onegateafrica.repository.DemandeurRepository;
import com.onegateafrica.repository.EmployeRepository;
import com.onegateafrica.service.DemandeurService;
import com.onegateafrica.service.EmployeService;

import net.bytebuddy.utility.RandomString;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/demandeur")
public class DemandeurController {

  private final DemandeurService demandeurService;
  private final DemandeurRepository demandeurRepository;
  private final EmployeRepository employeRepository;
  private final EmployeService employeService;
  @Autowired
  private JavaMailSender mailSender;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  public DemandeurController(DemandeurService demandeurService, DemandeurRepository demandeurRepository,
      EmployeRepository employeRepository, EmployeService employeService) {
    this.demandeurService = demandeurService;
    this.demandeurRepository = demandeurRepository;
    this.employeRepository = employeRepository;
    this.employeService = employeService;
  }


  @GetMapping("/getAllDemandeurs")
  public ResponseEntity getAllDemandeurs() {
    try {
      Iterable<Demandeur> demandeurs = demandeurRepository.findALlDemandeurs();
      return new ResponseEntity<>(demandeurs, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get All Demandeurs");

    }
  }

  @GetMapping("/getDemandeur/{id}")
  public ResponseEntity getDemandeur(@PathVariable("id") Long id) {
    if (id != null) {
      Demandeur demandeur = demandeurRepository.findById(id).get();
      return new ResponseEntity<>(demandeur, HttpStatus.OK);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id Invalide");
    }
  }

  @PostMapping("/ConsulterDemandeurByEmail/{email}")
  public ResponseEntity userbyemail(@PathVariable("email") String email,@RequestBody String image)
  {
    Demandeur demandeur = demandeurRepository.findByEmail(email);
    demandeur.setImage(image);
    demandeurRepository.save(demandeur);
    return new ResponseEntity(demandeur, HttpStatus.OK);
  }
  @GetMapping("/consulterDemandeur/{token}")
  public ResponseEntity userbytoken(@PathVariable String token) {

    if (token != null) {
      try {

        Demandeur demandeur = demandeurRepository.findByToken(token);
        if (demandeur != null) {
          return new ResponseEntity(demandeur, HttpStatus.OK);
        } else {
          Employe employe = employeRepository.findByToken(token);
          return new ResponseEntity(employe, HttpStatus.OK);

        }
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get consommateur info");

      }
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("token can't be null");
    }
  }


  @PostMapping("/loginDemandeur")
  public ResponseEntity loginDemandeur(@Validated @RequestBody Object objet) {
    try {
      if (objet != null) {
        ObjectMapper mapper = new ObjectMapper();
        Demandeur demandeur = mapper.convertValue(objet, Demandeur.class);
        Demandeur demandeurInscrit = demandeurRepository.findByEmail(demandeur.getEmail());
        if (demandeurInscrit != null && demandeurService
            .matchPasswords(demandeur.getMot_de_passe(), demandeurInscrit.getMot_de_passe())) {
          demandeurInscrit.setNotification_token(demandeur.getNotification_token());
          demandeurRepository.save(demandeurInscrit);
          return new ResponseEntity<>(demandeurInscrit.getToken(), HttpStatus.OK);
        } else {
          Employe employe = mapper.convertValue(objet, Employe.class);
          Employe employeInscrit = employeRepository.findByEmail(employe.getEmail());
          if (employeInscrit != null && employeService.matchPasswords(employe.getMot_de_passe(), employeInscrit.getMot_de_passe())) {
            employeInscrit.setNotification_token(employe.getNotification_token());
            employeRepository.save(employeInscrit);
            return new ResponseEntity<>(employeInscrit.getToken(), HttpStatus.OK);
          }
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("employee introuvable");
        }
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demandeur introuvable");

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PostMapping("/ajouterDemandeur")
  public ResponseEntity<Object> ajouterDemandeur(@RequestBody Demandeur demandeur)
      throws UnsupportedEncodingException, MessagingException {
    try {
      if (demandeur != null) {
        if (demandeurService.create(demandeur)) {
          return ResponseEntity.status(HttpStatus.OK).body("Demandeur ajouté");
        }
      }
    } catch (Exception e) {
      ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Echec");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demandeur ne doit pas être null");
  }

  @GetMapping("/countusers")
  public ResponseEntity countusers()
  {
    return new ResponseEntity(demandeurRepository.countusers(), HttpStatus.OK);

  }
  @PutMapping("/modifierDemandeur")
  public ResponseEntity modifierDemandeur
      (@RequestBody Demandeur demandeur) {
    Demandeur demandeur1 = demandeurRepository.findById(demandeur.getId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + demandeur.getId()));
    demandeur1.setNom(demandeur.getNom());
    demandeur1.setEmail(demandeur.getEmail());
    demandeur1.setImage(demandeur.getImage());
    if (demandeur.getMot_de_passe() != "") {
      demandeur1.setMot_de_passe(passwordEncoder.encode(demandeur.getMot_de_passe()));
    }
    demandeur1.setNumero_telephone(demandeur.getNumero_telephone());
    demandeurRepository.save(demandeur1);
    return ResponseEntity.status(HttpStatus.OK).body("Compte modifié avec succès");

  }

  @DeleteMapping("/supprimerDemandeur/{id}")
  public ResponseEntity supprimerDemandeur(@PathVariable Long id) {
    if (demandeurRepository.findById(id).isPresent()) {
      Demandeur demandeur1 = demandeurRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
      demandeurRepository.delete(demandeur1);
      return ResponseEntity.status(HttpStatus.OK).body("Compte supprimé");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("compte inexistant");
    }
  }

  @PostMapping("/processRegister")
  public String processRegister(@RequestBody String email)
      throws MessagingException, UnsupportedEncodingException {
    String code = RandomString.make(6);
    String toAddress = email;
    String fromAddress = "presta.tn.app@gmail.com";
    String senderName = "Presta";
    String subject = "Please verify your registration";
    String content = "Dear Mr/Madame,<br>"
        + "Please use this verification code to confirm your registration:<br><h3>"
        + code
        + "</h3><br>Thank you,<br>"
        + "Presta.";
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setFrom(fromAddress, senderName);
    helper.setTo(toAddress);
    helper.setSubject(subject);
    helper.setText(content, true);
    mailSender.send(message);
    return code;
  }

  @PostMapping("/resetPasswordVerification/{email}")
  public ResponseEntity resetPasswordVerification(@PathVariable String email) throws UnsupportedEncodingException, MessagingException {
    try {
      if (email != null) {

        if (demandeurService.resetPasswordVerification(email)) {
          return new ResponseEntity<>("Succes", HttpStatus.OK);
        } else {
          return new ResponseEntity<>("Email introuvable", HttpStatus.OK);
        }

      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Echec " + e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email cannot be null");
  }

  @PostMapping("/demandeurExistant")
  public ResponseEntity demandeurExistant(@RequestBody String email) {
    try {
      if (email != null) {
        Demandeur demandeurExistant = demandeurRepository.findByEmail(email);
        if (demandeurExistant == null) {
          return new ResponseEntity<>(HttpStatus.OK);
        } else {
          return ResponseEntity.status(HttpStatus.OK).body("Compte existant");
        }
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/verifyCodeForPasswordChange/{code}")
  public ResponseEntity verifyCodeForPasswordChange(@PathVariable String code) {
    if (code != "") {
      Demandeur demandeur = demandeurRepository.findByVerificationCodeForPasswordChange(code);
      if (demandeurService.verifyCodeForPasswordChange(code) != null) {
        return new ResponseEntity<>(demandeur, HttpStatus.OK);
      }
      return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);

    }
    return new ResponseEntity<>("Le code est null", HttpStatus.BAD_REQUEST);
  }

  @PutMapping("/changeDemandeurPassword/{email}/{password}")
  public ResponseEntity changeDemandeurPassword(@PathVariable String email, @PathVariable String password) {
    try {

      if (email != null) {
        Demandeur demandeur = demandeurRepository.findByEmail(email);
        if ((demandeur != null) && (password != null)) {
          demandeur.setMot_de_passe(password);
          demandeur.setMot_de_passe(demandeurService.encodePasswordDemandeur(demandeur));
          demandeurRepository.save(demandeur);
          return new ResponseEntity<>("Success", HttpStatus.OK);
        }
      }
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>("Vérifier vos données", HttpStatus.NOT_FOUND);
  }

}



