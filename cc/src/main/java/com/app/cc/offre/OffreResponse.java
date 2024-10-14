package com.app.cc.offre;

import com.app.cc.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class OffreResponse {
    private String description;
    private Double budget;
    private String creatorEmail; // Email of the creator to whom the offer is made
    private Long useridoffre; // ID of the user (Client) making the offer
    private LocalDateTime dateSoumission;
    private OffreStatus status;

}
