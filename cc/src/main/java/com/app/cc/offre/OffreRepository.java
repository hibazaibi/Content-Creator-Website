package com.app.cc.offre;

import com.app.cc.messagerie.conversation.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OffreRepository extends JpaRepository<Offre, Long> {

    Optional<Offre> findByUseridoffre(Long useridoffre);


}
