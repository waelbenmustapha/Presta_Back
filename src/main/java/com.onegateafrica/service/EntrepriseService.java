package com.onegateafrica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onegateafrica.entity.Employe;
import com.onegateafrica.entity.Entreprise;
import com.onegateafrica.repository.EntrepriseRepository;

@Service
public class EntrepriseService {

  private final EntrepriseRepository entrepriseRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  public EntrepriseService(EntrepriseRepository entrepriseRepository) {
    this.entrepriseRepository = entrepriseRepository;
  }

  public List<Employe> getListdesEmployee(Long id) {

    Entreprise entreprise = entrepriseRepository.findById(id).orElse(new Entreprise());
    return entreprise.getEmployes();
  }

  public Boolean matchPasswords(String rawPassword, String encodedPassword) {
    return (passwordEncoder.matches(rawPassword, encodedPassword));
  }

  public String encodePasswordConsommateur(Entreprise entreprise) {
    String encodedPassword = passwordEncoder.encode(entreprise.getMot_de_passe());
    return encodedPassword;
  }

 /* public Boolean register(Entreprise user)
      throws UnsupportedEncodingException, MessagingException {
    Entreprise existingUser = entrepriseRepository.findByEmail(user.getEmail());
    if (existingUser != null) {
      return false;
    } else {

      user.setMot_de_passe(encodePasswordConsommateur(user));

      String randomCode = RandomString.make(10);
      String randomCode2 = RandomString.make(64);
      System.out.println(randomCode);
      user.setVerificationCode(randomCode);
      user.setToken(randomCode2);
      user.setEnabled(false);

      entrepriseRepository.save(user);

      sendVerificationEmail(user);
      return true;
    }
  }


  private void sendVerificationEmail(Entreprise user)
      throws MessagingException, UnsupportedEncodingException {
    String toAddress = user.getEmail();
    String fromAddress = "presta@gmail.com";
    String senderName = "Presta";
    String subject = "Please verify your registration";
    String content = "Dear Mr/Madame,<br>"
        + "Please use this verification code to confirm your registration:<br>"
        + user.getVerificationCode()
        + "<br>Thank you,<br>"
        + "Presta .";

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom(fromAddress, senderName);
    helper.setTo(toAddress);
    helper.setSubject(subject);

    helper.setText(content, true);

    mailSender.send(message);

    System.out.println("Email has been sent");

  }

  */

}