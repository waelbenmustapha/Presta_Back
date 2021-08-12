package com.example.presta;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.onegateafrica.PrestaApplication;
import com.onegateafrica.entity.Categorie;
import com.onegateafrica.entity.Demande;
import com.onegateafrica.entity.Demandeur;
import com.onegateafrica.entity.Employe;
import com.onegateafrica.entity.Entreprise;
import com.onegateafrica.entity.Feedback;
import com.onegateafrica.entity.Service;
import com.onegateafrica.repository.AdministrateurRepository;
import com.onegateafrica.repository.CategorieRepository;
import com.onegateafrica.repository.DemandeRepository;
import com.onegateafrica.repository.DemandeurRepository;
import com.onegateafrica.repository.EmployeRepository;
import com.onegateafrica.repository.EntrepriseRepository;
import com.onegateafrica.repository.FeedbackRepository;
import com.onegateafrica.repository.PrestataireRepository;
import com.onegateafrica.repository.ServiceRepository;
import com.onegateafrica.service.DemandeurService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = PrestaApplication.class)
class PrestaApplicationTests {

  @Autowired
  CategorieRepository categorieRepository;
  @Autowired
  DemandeurRepository demandeurRepository;
  @Autowired
  EmployeRepository employeRepository;
  @Autowired
  EntrepriseRepository entrepriseRepository;
  @Autowired
  DemandeurService demandeurService;
  @Autowired
  FeedbackRepository feedbackRepository;
  @Autowired
  PrestataireRepository prestataireRepository;
  @Autowired
  ServiceRepository serviceRepository;
  @Autowired
  AdministrateurRepository administrateurRepository;
  @Autowired
  DemandeRepository demandeRepository;
  TestInfo testInfo;

  @Test
  @DisplayName("ajouter modifier suppresion categorie")
  void ajoutcategorie() {
    Categorie categorie = new Categorie();
    List<Service> service = new ArrayList<>();
    categorie.setServices(service);
    categorie.setNom("testcategorie");
    categorie.setIcone("first");
    categorieRepository.save(categorie);
    Assert.assertNotNull("ajout categorie is working", categorieRepository.findByNom("testcategorie"));
    Categorie categoriefrombase = categorieRepository.findByNom("testcategorie");
    categoriefrombase.setIcone("changed");
    categorieRepository.save(categoriefrombase);
    Assert.assertEquals("changed", categorieRepository.findByNom("testcategorie").getIcone());
    categorieRepository.delete(categoriefrombase);
  }
//f
  @Test
  @DisplayName("Inscription connexion suppression")
  void testinscriptionconnexioncompletesuppression() throws UnsupportedEncodingException, MessagingException {
    Demandeur demandeur = new Demandeur();
    Boolean correct = false;
    demandeur.setNom("test demandeur");
    demandeur.setEmail("demandeur@test.fr");
    demandeur.setMot_de_passe("testpassword");
    demandeurService.create(demandeur);
    if (demandeurService
        .matchPasswords("testpassword", demandeurRepository.findByEmail("demandeur@test.fr").getMot_de_passe())) {
      correct = true; }
    demandeurRepository.delete(demandeur);
    Assert.assertEquals(true, correct);
  }















  @Test
  @DisplayName("Admin exist")
  void checkadminexist() {
    Assert.assertNotNull(administrateurRepository.findById(1L));
  }














  @Test
  @DisplayName("Ajout modifier demande supprimer")
  void ajoutermodifierdemande() {
    Demande demande = new Demande();
    demande.setDate_debut("g");
    demande.setDemandeur(null);
    demande.setFeedback(null);
    demande.setNom("test demande");
    demande.setPrestataire(null);
    demande.setService(null);
    demande.setDescription("old");
    demande.setStatut(0);
    demandeRepository.save(demande);
    Demande demandefrombase = demandeRepository.findByNom("test demande");
    demandefrombase.setDescription("new");
    demandeRepository.save(demandefrombase);
    Assert.assertEquals(demandeRepository.findByNom("test demande").getDescription(), "new");
    demandeRepository.delete(demandefrombase);
  }








  @Test
  @DisplayName("Ajout et suppression Employe")
  void ajouteremploye() {
    Employe employe = new Employe();
    employe.setNom("employe");
    employe.setEmail("employemail");
    employe.setAddresse("adresse");
    employeRepository.save(employe);
    Assert.assertNotNull(employeRepository.findByEmail("employemail"));
    employeRepository.delete(employe);
  }










  @Test
  @DisplayName("Ajout suppression et validation Employe")
  void Entreprise() {
    Entreprise entreprise = new Entreprise();
    entreprise.setNom("entreprise");
    entreprise.setEmail("entreprise@email.fr");
    entreprise.setValidation(1);
    entrepriseRepository.save(entreprise);
    Assert.assertEquals("entreprise", entrepriseRepository.findByEmail("entreprise@email.fr").getNom());
    Assert.assertEquals(1, entrepriseRepository.findByEmail("entreprise@email.fr").getValidation());
    entrepriseRepository.delete(entreprise);
  }










  @Test
  @DisplayName("Ajout et modification feedback")
  void feedback() {
    Feedback feedback = new Feedback();
    feedback.setNote(5L);
    feedback.setAvis("bien");
    feedbackRepository.save(feedback);
    Assert.assertEquals("bien", feedbackRepository.findById(feedback.getId()).get().getAvis());
    Feedback feedbackfrombase = feedbackRepository.findById(feedback.getId()).get();
    feedbackfrombase.setAvis("bien changé");
    feedbackRepository.save(feedbackfrombase);
    Assert.assertEquals("bien changé", feedbackRepository.findById(feedbackfrombase.getId()).get().getAvis());
  }










  @BeforeEach
  @Test
  void timestamp(TestInfo testInfo, TestReporter testReporter) {
    testReporter.publishEntry("en cours d'execution " + testInfo.getDisplayName());

  }
}
