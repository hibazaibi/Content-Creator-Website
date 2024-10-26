package com.app.cc.messagerie.message;

import com.app.cc.messagerie.conversation.Conversation;
import com.app.cc.messagerie.conversation.ConversationRepository;
import com.app.cc.user.User;
import com.app.cc.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;
    @Transactional
    public Message sendMessage(MessageRequest messageRequest) {
        User sender = userRepository.findById(messageRequest.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(messageRequest.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        Optional<Conversation> conversation1 = conversationRepository
                .findByClientIdAndCreatorId(sender.getId(), receiver.getId());
        Optional<Conversation> conversation2 = conversationRepository
                .findByClientIdAndCreatorId(receiver.getId(), sender.getId());
        Conversation conversation = conversation1.orElse(conversation2.orElseGet(() -> {
            Conversation newConversation = new Conversation();
            newConversation.setClient(sender);
            newConversation.setCreator(receiver);
            newConversation.setUpdatedAt(LocalDateTime.now());
            return conversationRepository.save(newConversation);
        }));
        conversation.setUpdatedAt(LocalDateTime.now());
        Message message = new Message();
        message.setSender(sender);
        message.setConversation(conversation);
        message.setMessageText(messageRequest.getMessageText());
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);

        return messageRepository.save(message);
    }



    public List<Message> getMessages(Long conversationId) {
            return messageRepository.findByConversationId(conversationId);
        }

    public Message markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setRead(true);
        return messageRepository.save(message);
    }
    }


