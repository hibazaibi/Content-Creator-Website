package com.app.cc.messagerie.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages/")
public class MessageController {


        @Autowired
        private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageRequest messageRequest) {
        Message message = messageService.sendMessage(messageRequest);
        return ResponseEntity.ok("Message sent with ID: " + message.getId());
    }

        @GetMapping("/{conversationId}")
        public List<Message> getMessages(@PathVariable Long conversationId) {
            return messageService.getMessages(conversationId);
        }
    @PutMapping("/mark-as-read/{messageId}")
    public ResponseEntity<Message> markAsRead(@PathVariable Long messageId) {
        Message updatedMessage = messageService.markAsRead(messageId);
        return ResponseEntity.ok(updatedMessage);
    }

    }


