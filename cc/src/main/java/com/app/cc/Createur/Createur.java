package com.app.cc.Createur;

import com.app.cc.file.file;
import com.app.cc.token.Token;
import com.app.cc.user.Role;
import com.app.cc.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
@Data
@Entity
@SuperBuilder
@DiscriminatorValue("CREATOR")
@NoArgsConstructor
@AllArgsConstructor
public class Createur extends User {

    private String bio;
    private String lienInsta;
    private String lienTikTok;
    private String categoriesContenu;







}
