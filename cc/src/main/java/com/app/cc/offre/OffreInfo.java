package com.app.cc.offre;

import com.app.cc.Client.Client;
import com.app.cc.Createur.Createur;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OffreInfo {


    private Long idOffre;
    private String description;
    private Double budget;

    private String status;

    private LocalDateTime dateSoumission;

    private Date deadline;
    private String collaborationDetails;
    private String specialRequests;
    public String  namecreateur;
    public Long useridoffre;
public String nameclient ;
    private Long idcreateur ;
    private LocalDateTime expirationDate;
private boolean isev ;

}
