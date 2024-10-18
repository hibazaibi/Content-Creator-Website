package com.app.cc.Createur;

import com.app.cc.evaluation.Evaluation;
import com.app.cc.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
    @OneToMany(mappedBy = "createur")
    private List<Evaluation> evaluations;






}
