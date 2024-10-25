package com.app.cc.messagerie.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository  extends JpaRepository<Message, Long> {
    List<Message> findByConversationId(Long conversationId) ;
}
