package com.app.cc.offre;

import com.app.cc.Client.Client;
import com.app.cc.Createur.Createur;
import com.app.cc.messagerie.conversation.Conversation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OffreRepository extends JpaRepository<Offre, Long> {

    List<Offre> findByUseridoffre(Client useridoffre);

    List<Offre> findByIdcreateur(Createur idcreateur);
    long countByStatus(OffreStatus status);

    @Query("SELECT AVG(o.budget) FROM Offre o")
    Double calculateAverageBudget();
    List<Offre> findByUseridoffreId(Long clientId);

    @Query("SELECT SUM(o.budget) FROM Offre o WHERE o.useridoffre.id = :clientId")
    Double calculateTotalBudgetByClientId(@Param("clientId") Long clientId);

    Long countByUseridoffreIdAndStatus(Long clientId, OffreStatus status);
    long countByIdcreateurId(Long creatorId);

    long countByIdcreateurIdAndStatus(Long creatorId, OffreStatus status);

    @Query("SELECT AVG(o.budget) FROM Offre o WHERE o.idcreateur.id = :creatorId")
    Double calculateAverageBudgetForCreator(Long creatorId);

    @Query("SELECT SUM(o.budget) FROM Offre o WHERE o.idcreateur.id = :creatorId AND o.status = :status")
    Double calculateTotalBudgetByCreatorIdAndStatus(Long creatorId, OffreStatus status);

    List<Offre> findByIdcreateurIdOrderByDateSoumissionDesc(Long creatorId, Pageable pageable);
}

