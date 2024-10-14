package com.app.cc.auth;

import com.app.cc.file.file;
import com.app.cc.user.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String nom;
  private String prenom;
  private String email;
  private String password;
  private Long numtel;

  // For Client
  private String nomEntreprise;
  private String siteWebEntreprise;
  private String secteurActivite;

  // For Createur
  private String bio;
  private String lienInsta;
  private String lienTikTok;
  private String categoriesContenu;
  private Role role; // This is where the role is determined (CREATOR, CLIENT, etc.)

  private file image; // File associated with the user
}
