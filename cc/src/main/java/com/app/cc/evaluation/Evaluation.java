package com.app.cc.evaluation;

import com.app.cc.Client.Client;
import com.app.cc.Createur.Createur;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "Evaluation")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idevaluation;
    @ManyToOne
    private Client client;

    @ManyToOne
    private Createur creator;

    private int rating;

    private String feedback;

    private LocalDateTime dateEvaluated;
}

