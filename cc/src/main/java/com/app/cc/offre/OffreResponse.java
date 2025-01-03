package com.app.cc.offre;

import com.app.cc.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class OffreResponse {
    private String description;
    private Double budget;
    private String useridoffre;
    private LocalDateTime dateSoumission;
    private OffreStatus status;
    private String idcreateur ;
    private LocalDateTime expirationDate;
    private Date deadline;
    private String collaborationDetails;
    private String specialRequests;

}
