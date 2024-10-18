package com.app.cc.offre;

import com.app.cc.Client.Client;
import com.app.cc.Createur.Createur;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "Offre")
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOffre;
    private String description;
    private Double budget;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OffreStatus status;

    private LocalDateTime dateSoumission;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_idoffre")
    public Client useridoffre;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "createur_id")
    private Createur idcreateur ;


}
