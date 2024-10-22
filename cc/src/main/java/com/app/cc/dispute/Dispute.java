package com.app.cc.dispute;

import com.app.cc.Client.Client;
import com.app.cc.Createur.Createur;
import com.app.cc.offre.Offre;
import com.app.cc.user.User;
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
public class Dispute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offre offer;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Createur creator;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;
    private String raison;
    private String Detailsresolution;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisputeStatus status;
    private String notesAdmin;
    private LocalDateTime dateSoumis;
    private LocalDateTime dateResolution;
}
