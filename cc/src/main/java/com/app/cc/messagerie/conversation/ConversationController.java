package com.app.cc.messagerie.conversation;

import com.app.cc.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversations/")
@RequiredArgsConstructor

public class ConversationController {

        @Autowired
        private ConversationRepository conversationRepository;

        @GetMapping("/{userId}")
        public List<Conversation> getUserConversations(@PathVariable Long userId) {
            var user = new User();
            user.setId(userId);
            return conversationRepository.findByClientOrCreator(user, user);
        }

}
