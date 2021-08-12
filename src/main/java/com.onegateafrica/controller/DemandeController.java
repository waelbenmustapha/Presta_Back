package com.onegateafrica.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.onegateafrica.entity.Demande;
import com.onegateafrica.entity.Feedback;
import com.onegateafrica.repository.DemandeRepository;
import com.onegateafrica.repository.FeedbackRepository;
import com.onegateafrica.service.DemandeService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/demande")

public class DemandeController {

  private final FeedbackRepository feedbackRepository;
  private final DemandeService demandeService;
  private final DemandeRepository demandeRepository;

  @Autowired
  public DemandeController(DemandeService demandeService, DemandeRepository demandeRepository, FeedbackRepository feedbackRepository) {
    this.demandeRepository = demandeRepository;
    this.demandeService = demandeService;
    this.feedbackRepository = feedbackRepository;
  }


  @PostMapping("/creerDemande")
  public ResponseEntity creerDemande(@Validated @RequestBody Demande demande) {
    try {
      demandeRepository.save(demande);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To Create Demande");
    }
  }


  @GetMapping("/getAllFeedback/{id}")
  public ResponseEntity getAllFeedback(@PathVariable("id") Long id) {
    return new ResponseEntity<>(demandeRepository.findByPrestataireIdAndFeedbackNotNull(id), HttpStatus.OK);
  }

  @DeleteMapping("/supprimerFeedback/{id}")
  public ResponseEntity supprimerFeedback(@PathVariable("id") Long id) {
    Optional<Demande> demande = demandeRepository.findById(id);
    Feedback feedback = demandeRepository.findById(id).get().getFeedback();
    feedbackRepository.delete(feedback);
    //setfeedbackid in demande to null
    demande.get().setFeedback(null);
    demandeRepository.save(demande.get());
    //savedemande

    return ResponseEntity.status(HttpStatus.OK).body("Feedback deleted");
  }


  @GetMapping("/demandeEmploye/{employeid}/{statut}")
  public ResponseEntity demandeEmploye(@PathVariable("employeid") Long employeid, @PathVariable("statut") Integer statut) {
    return new ResponseEntity<>(demandeRepository.findByEmployesIdAndStatut(employeid, statut), HttpStatus.OK);

  }

  @GetMapping("/alldemandes")
  public ResponseEntity countcategories()
  {
    return new ResponseEntity(demandeRepository.findAll(), HttpStatus.OK);

  }
  @GetMapping("/getAllDemandesReçues/{id}")

  public ResponseEntity getAllDemandesReçu(@PathVariable("id") Long id) {

    try {
      if (id != null) {
        List<Demande> demandes = demandeRepository.findByPrestataireId(id);

        return new ResponseEntity<>(demandes, HttpStatus.OK);
      }
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Echec");
    }
  }
  @GetMapping("/demandeperservice")
  public ResponseEntity demperserv()
  {
    return new ResponseEntity(demandeRepository.demperserv(), HttpStatus.OK);

  }
  @GetMapping("/getAllDemandes/{id}")

  public ResponseEntity getAllDemandes(@PathVariable("id") Long id) {
    try {
      if (id != null) {
        List<Demande> demandes = demandeRepository.findByDemandeurId(id);

        return new ResponseEntity<>(demandes, HttpStatus.OK);
      }
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Echec");
    }
  }

  @PutMapping("/changerStatut/{id}/{statut}")
  public ResponseEntity changerStatut(@PathVariable("id") Long id, @PathVariable("statut") Integer statut) {
    try {
      if (id != null) {
        demandeService.accepterourefuser(id, statut);

        return new ResponseEntity<>(HttpStatus.OK);
      }
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To Accept Or Refuse demandes");
    }
  }

  @PostMapping("/ajouterFeedback/{id}")
  public ResponseEntity ajouterFeedback(@Validated @RequestBody Feedback feedback, @PathVariable Long id) {
    Demande demande = demandeRepository.findById(id).get();
    Feedback newfeed = new Feedback();
    newfeed.setAvis(feedback.getAvis());
    newfeed.setNote(feedback.getNote());
    newfeed.setDemande(demande);
    feedbackRepository.save(newfeed);
    demande.setFeedback(newfeed);
    demandeRepository.save(demande);
    return ResponseEntity.status(HttpStatus.OK).body("FEEDBACK Ajouter");

  }
  @PutMapping("/modifierDemande/{id}")
  public ResponseEntity modifierDemande(@PathVariable Long id, @Validated @RequestBody Demande demande) {
    Demande demandeamodifier = demandeRepository.findById(id).get();
    demandeamodifier.setDescription(demande.getDescription());
    demandeamodifier.setDate_debut(demande.getDate_debut());
    demandeRepository.save(demandeamodifier);
    return ResponseEntity.status(HttpStatus.OK).body("Demande modifier");
  }
  @PutMapping("/modifierFeedback/{id}")
  public ResponseEntity modifierFeedback(@PathVariable Long id, @Validated @RequestBody Feedback feedback) {
    Feedback editedfeedback = feedbackRepository.findById(id).get();
    editedfeedback.setAvis(feedback.getAvis());
    editedfeedback.setNote(feedback.getNote());
    feedbackRepository.save(editedfeedback);
    return ResponseEntity.status(HttpStatus.OK).body("FEEDBACK edited");
  }

}