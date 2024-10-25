package com.app.cc.messagerie.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
        private Long senderId;
        private Long receiverId;
        private String messageText;


}
