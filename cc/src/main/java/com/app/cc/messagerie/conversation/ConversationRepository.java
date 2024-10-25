package com.app.cc.messagerie.conversation;

import com.app.cc.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByClientAndCreator(User client, User creator);

    List<Conversation> findByClientOrCreator(User client, User creator);

    Optional<Conversation> findByClientIdAndCreatorIdOrCreatorIdAndClientId(Long clientId, Long creatorId, Long creatorId2, Long clientId2);
}