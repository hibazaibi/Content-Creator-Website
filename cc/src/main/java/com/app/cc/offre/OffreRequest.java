package com.app.cc.offre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OffreRequest {
    private String description;
    private Double budget;
    private Long useridoffre;
    private LocalDateTime dateSoumission;
    private OffreStatus status;

    private Long idcreateur ; 

}
