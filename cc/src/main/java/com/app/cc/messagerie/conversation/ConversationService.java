package com.app.cc.messagerie.conversation;

import com.app.cc.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ConversationService {

        @Autowired
        private ConversationRepository conversationRepository;

        public Conversation createOrGetConversation(User client, User creator) {
            var existingConversation = conversationRepository.findByClientAndCreator(client, creator);
            if (existingConversation.isPresent()) {
                return existingConversation.get();
            }
            Conversation conversation = new Conversation();
            conversation.setClient(client);
            conversation.setCreator(creator);
            conversation.setUpdatedAt(LocalDateTime.now());
            return conversationRepository.save(conversation);
        }

        public List<Conversation> getUserConversations(User user) {
            return conversationRepository.findByClientOrCreator(user, user);
        }
    }


