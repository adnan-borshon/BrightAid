package com.example.Bright_Aid.Dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConversationDto {
    private Integer conversationId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer projectId;
    private Integer ngoProjectId;
    private LocalDateTime lastMessageAt;
    private List<ConversationParticipantDto> participants;
    private MessageDto lastMessage;
    private Integer unreadCount;
}