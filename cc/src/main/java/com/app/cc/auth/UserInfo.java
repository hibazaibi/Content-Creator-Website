package com.app.cc.auth;

import com.app.cc.file.file;
import com.app.cc.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Long numtel;
    private LocalDate dateNaissance;
    private String nomEntreprise;
    private String siteWebEntreprise;
    private String secteurActivite;
    private String bio;
    private String lienInsta;
    private String lienTikTok;
    private String categoriesContenu;
    private Role role;
    private file image;
    private Double avgrattings ;
private Long id;
}

