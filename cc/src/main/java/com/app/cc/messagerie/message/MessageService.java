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
        Optional<Conversation> existingConversation = conversationRepository
                .findByClientIdAndCreatorIdOrCreatorIdAndClientId(sender.getId(), receiver.getId(), receiver.getId(), sender.getId());

        Conversation conversation;
        if (existingConversation.isPresent()) {
            conversation = existingConversation.get();
        } else {
            conversation = new Conversation();
            conversation.setClient(sender);
            conversation.setCreator(receiver);
            conversation.setUpdatedAt(LocalDateTime.now());

            conversation = conversationRepository.save(conversation);
        }

        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);
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


