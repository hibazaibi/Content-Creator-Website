package com.app.cc.offre;

import com.app.cc.Client.Client;
import com.app.cc.Createur.Createur;
import com.app.cc.user.User;
import jakarta.persistence.*;

import java.util.Date;

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

    @Temporal(TemporalType.DATE)
    private Date dateSoumission;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "createur_id")
    private Createur createur; }
