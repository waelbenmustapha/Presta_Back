package com.onegateafrica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onegateafrica.entity.Feedback;
import com.onegateafrica.entity.Prestataire;
import com.onegateafrica.repository.DemandeRepository;
import com.onegateafrica.repository.FeedbackRepository;
import com.onegateafrica.repository.PrestataireRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

  private final FeedbackRepository feedbackRepository;
  private final DemandeRepository demandeRepository;
  private final PrestataireRepository prestataireRepository;

  @Autowired
  public FeedbackController(PrestataireRepository prestataireRepository, FeedbackRepository feedbackRepository, DemandeRepository demandeRepository) {
    this.feedbackRepository = feedbackRepository;
    this.demandeRepository = demandeRepository;
    this.prestataireRepository = prestataireRepository;
  }


  @GetMapping("/getAllFeedbacks")
  public ResponseEntity getAllFeedbacks() {
    try {
      Iterable<Feedback> feedbacks = feedbackRepository.findAll();
      return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To get All Feedbacks");

    }
  }

  @DeleteMapping("/supprimerFeedback/{id}")
  public ResponseEntity supprimerFeedback(@PathVariable("id") Long id) {
    if (id != null) {
      feedbackRepository.deleteById(id);
      return ResponseEntity.status(HttpStatus.OK).body("Feedback supprimé");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id invalide");
    }
  }
  @GetMapping("/countfeedbacks")
  public ResponseEntity countfeedbacks()
  {
    return new ResponseEntity(feedbackRepository.countfeedback(), HttpStatus.OK);

  }
  @PutMapping("/notePrestataire/{id}")
  public ResponseEntity notePrestataire(@PathVariable Long id) {
    try {
      if (id != null) {
        int i;
        float average = 0;
        List<Feedback> feedbacks = feedbackRepository.findByDemandePrestataireId(id);
        for (i = 0; i < feedbacks.size(); i++) {
          average = average + feedbacks.get(i).getNote();
          System.out.println(average);
        }
        average = average / i;
        Prestataire prestataire = prestataireRepository.findById(id).get();
        prestataire.setNote(average);
        prestataireRepository.save(prestataire);
        return new ResponseEntity(average, HttpStatus.OK);
      }

    } catch (Exception e) {
      ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Echec");
    }
    return new ResponseEntity(" error !", HttpStatus.BAD_REQUEST);
  }
}
