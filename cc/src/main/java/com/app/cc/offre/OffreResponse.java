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
    private String creatorEmail;
    private Long useridoffre;
    private LocalDateTime dateSoumission;
    private OffreStatus status;

}
