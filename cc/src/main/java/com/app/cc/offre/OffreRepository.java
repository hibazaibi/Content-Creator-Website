package com.app.cc.offre;

import com.app.cc.Client.Client;
import com.app.cc.Createur.Createur;
import com.app.cc.messagerie.conversation.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OffreRepository extends JpaRepository<Offre, Long> {

    List<Offre> findByUseridoffre(Client useridoffre);

    List<Offre> findByIdcreateur(Createur idcreateur);
}
