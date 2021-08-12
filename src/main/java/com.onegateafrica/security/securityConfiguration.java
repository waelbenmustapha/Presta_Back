package com.onegateafrica.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.onegateafrica.security.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class securityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private DataSource dataSource;

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.cors().and().csrf().disable()
        .httpBasic()
        .and().authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers(HttpMethod.PUT, "/demande/modifierDemande/{id}","/demande/modifierFeedback/{id}","/demande/changerStatut/{id}/{statut}",
            "/demandeur/modifierDemandeur",
            "/demandeur/changeDemandeurPassword/{email}/{password}",
            "/employe/modifierEmploye","/employe//affecterDemandeEmploye/{id}/{idDemande}",
            "/feedback/notePrestataire/{id}","/mobilenumbers/otp","/categorie/modifierCategorie/{id}",
            "/service/modifierService/{id}","/prestataire/valider/{id}","/entreprise/valider/{id}")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/demandeur/ConsulterDemandeurByEmail/{email}","/demande/creerDemande","/demande/ajouterFeedback/{id}",
            "/demandeur/loginDemandeur","/demandeur/ajouterDemandeur","/demandeur/processRegister",
            "/demandeur/resetPasswordVerification/{email}",
            "/demandeur/demandeurExistant","/employe/ajouterEmploye","/entreprise/ajouterEntreprise/{id}",
            "/mobilenumbers/otp", "/prestataire/ajouterPrestataire/{id}",
            "/prestataire/prestataireExistant", "/entreprise/entrepriseExistant",
            "/categorie/ajouterCategorie","/service/ajouterService/{id}",
            "/admin/loginAdministrateur").permitAll()
        .antMatchers(HttpMethod.DELETE, "/demande/supprimerFeedback/{id}",
            "/demandeur/supprimerDemandeur/{id}","/employe/supprimerEmploye/{id}",
            "/categorie/supprimerCategorie/{id}",
            "/service/supprimerService/{id}",
            "/prestataire/supprimerPrestataire/{id}","/entreprise/supprimerEntreprise/{id}").permitAll()
        .antMatchers(HttpMethod.GET, "/service/countservices","/demande/demandeperservice","/demande/alldemandes","/demandeur/countusers","/feedback/countfeedbacks","/categorie/countcategories","/service/populaire","/service/getServiceByNom",
            "/service/getPrestataireByService/{id}","/prestataire/populaire",
            "/prestataire/recherchePrestataireByNom/{nom}","/entreprise/getListeDesEmployes/{id}",
            "/employe/avoirDemandesEnCours/{id}","/demandeur/consulterDemandeur/{token}",
            "/demandeur/verifyCodeForPasswordChange/{code}","/demande/demandeEmploye/{employeid}/{statut}",
            "/demande/getAllDemandesReçues/{id}","/demande/getAllFeedback/{id}","/demande/getAllDemandes/{id}",
            "/categorie/getAllCategories","/categorie/populaire","/categorie/getCategorie/{id}",
            "/categorie/getServiceByCategorie/{id}",
            "/categorie/getCategorie/{id}",
            "/categorie/getServiceByCategorie/{id}","/service/getService/{id}","/demandeur/getDemandeur/{id}",
            "/prestataire/getPrestataire/{id}","/entreprise/getEntreprise/{id}","/employe/getEmploye/{id}",
            "/feedback/getFeedback/{id}","/service/getAllServices","/prestataire/getAllPrestataires",
            "/entreprise/getAllEntreprises","/employe/getAllEmployes","/feedback/getAllFeedbacks",
            "/demandeur/getAllDemandeurs", "/admin/consulterAdministrateur/{token}").permitAll()
        .anyRequest().authenticated();
  }
}