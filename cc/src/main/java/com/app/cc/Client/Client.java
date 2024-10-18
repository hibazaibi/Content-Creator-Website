package com.app.cc.Client;


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
@DiscriminatorValue("Client")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Client extends User {

    private String nomEntreprise;
    private String siteWebEntreprise;
    private String secteurActivite;
    @OneToMany(mappedBy = "client")
    private List<Evaluation> evaluations;

}