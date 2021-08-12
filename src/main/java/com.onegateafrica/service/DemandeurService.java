package com.onegateafrica.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onegateafrica.entity.Demandeur;
import com.onegateafrica.repository.DemandeurRepository;

import net.bytebuddy.utility.RandomString;


@Service
public class DemandeurService {

  private final DemandeurRepository demandeurRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  public DemandeurService(DemandeurRepository demandeurRepository) {
    this.demandeurRepository = demandeurRepository;
  }

  public String getDemandeurNotificationToken(Long id) {
    Demandeur demandeurToken = demandeurRepository.findById(id).orElse(new Demandeur());

    return demandeurToken.getNotification_token();

  }

  public Boolean create(Demandeur demandeur)
      throws UnsupportedEncodingException, MessagingException {
    Demandeur demandeurExistant = demandeurRepository.findByEmail(demandeur.getEmail());
    if (demandeurExistant != null) {
      return false;
    } else {
      demandeur.setMot_de_passe(encodePasswordDemandeur(demandeur));
      String randomCode = RandomString.make(64);
      demandeur.setToken(randomCode);
      demandeur.setValidation(1);
      demandeurRepository.save(demandeur);
      return true;
    }
  }

  public void saveDemandeurNotificationToken(Long id, String token) {
    Demandeur demandeur = demandeurRepository.findById(id).orElse(new Demandeur());
    demandeur.setNotification_token(token);
    demandeurRepository.save(demandeur);
  }


  public Boolean matchPasswords(String rawPassword, String encodedPassword) {
    return (passwordEncoder.matches(rawPassword, encodedPassword));
  }

  public String encodePasswordDemandeur(Demandeur demandeur) {
    String encodedPassword = passwordEncoder.encode(demandeur.getMot_de_passe());
    return encodedPassword;
  }


  public Boolean resetPasswordVerification(String email)
      throws UnsupportedEncodingException, MessagingException {
    Demandeur existingUser = demandeurRepository.findByEmail(email);
    if (existingUser != null) {
      sendVerificationEmailForPasswordChange(existingUser);
      return true;
    }
    return false;
  }


  public Demandeur verifyCodeForPasswordChange(String code) {

    Demandeur user = demandeurRepository.findByVerificationCodeForPasswordChange(code);

      if (user == null) {
          return null;
      } else {
          user.setVerificationCodeForPasswordChange(null);
          demandeurRepository.save(user);
          return user;
      }

  }

  private void sendVerificationEmailForPasswordChange(Demandeur user)
      throws MessagingException, UnsupportedEncodingException {
    String toAddress = user.getEmail();
    String randomCode = RandomString.make(6);
    user.setVerificationCodeForPasswordChange(randomCode);
    demandeurRepository.save(user);
    String fromAddress = "presta.tn.app@gmail.com";
    String senderName = "Presta";
    String subject = "Please verify your your password reset";
    String content = "Dear Mr/Madame,<br>"
        + "Please use this verification code to confirm your password reset:<br>"
        + "<br><h3>" + user.getVerificationCodeForPasswordChange() + "</h3><br>"
        + "<br>Thank you,<br>"
        + "Presta.";

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom(fromAddress, senderName);
    helper.setTo(toAddress);
    helper.setSubject(subject);

    helper.setText(content, true);

    mailSender.send(message);

    System.out.println("Email has been sent");

  }

}
