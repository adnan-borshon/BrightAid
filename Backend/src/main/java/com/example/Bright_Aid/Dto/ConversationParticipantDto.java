package com.example.Bright_Aid.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConversationParticipantDto {
    private Integer participantId;
    private Integer conversationId;
    private Integer userId;
    private String userName;
    private String userType;
    private LocalDateTime joinedAt;
    private LocalDateTime lastReadAt;
}