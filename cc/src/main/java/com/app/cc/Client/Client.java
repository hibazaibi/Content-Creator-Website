package com.app.cc.Client;


import com.app.cc.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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


}