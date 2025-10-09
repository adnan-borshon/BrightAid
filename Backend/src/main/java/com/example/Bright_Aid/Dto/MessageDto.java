package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.Message.MessageType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDto {
    private Integer messageId;
    private Integer conversationId;
    private Integer senderId;
    private String senderName;
    private String senderType;
    private MessageType messageType;
    private String messageText;
    private String imageUrl;
    private LocalDateTime sentAt;
}