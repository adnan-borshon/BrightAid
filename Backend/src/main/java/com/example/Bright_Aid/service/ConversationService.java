package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.*;
import com.example.Bright_Aid.Entity.*;
import com.example.Bright_Aid.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SchoolProjectRepository schoolProjectRepository;
    private final NgoProjectRepository ngoProjectRepository;

    // Get all conversations for a user
    public List<ConversationDto> getUserConversations(Integer userId) {
        List<Conversation> conversations = conversationRepository.findByUserId(userId);
        return conversations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Create or get conversation between two users for a project
    @Transactional
    public ConversationDto createOrGetConversation(Integer userId1, Integer userId2, Integer projectId, Integer ngoProjectId) {
        Conversation conversation;
        
        if (projectId != null) {
            conversation = conversationRepository.findByUsersAndProject(userId1, userId2, projectId)
                    .orElseGet(() -> createNewConversation(userId1, userId2, projectId, null));
        } else if (ngoProjectId != null) {
            conversation = conversationRepository.findByUsersAndNgoProject(userId1, userId2, ngoProjectId)
                    .orElseGet(() -> createNewConversation(userId1, userId2, null, ngoProjectId));
        } else {
            throw new IllegalArgumentException("Either projectId or ngoProjectId must be provided");
        }
        
        return convertToDto(conversation);
    }

    private Conversation createNewConversation(Integer userId1, Integer userId2, Integer projectId, Integer ngoProjectId) {
        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId1));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId2));

        Conversation conversation = Conversation.builder()
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if (projectId != null) {
            SchoolProject project = schoolProjectRepository.findById(projectId)
                    .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
            conversation.setProject(project);
        }

        if (ngoProjectId != null) {
            NgoProject ngoProject = ngoProjectRepository.findById(ngoProjectId)
                    .orElseThrow(() -> new RuntimeException("NGO Project not found: " + ngoProjectId));
            conversation.setNgoProject(ngoProject);
        }

        conversation = conversationRepository.save(conversation);

        // Add participants
        ConversationParticipant participant1 = ConversationParticipant.builder()
                .conversation(conversation)
                .user(user1)
                .joinedAt(LocalDateTime.now())
                .build();

        ConversationParticipant participant2 = ConversationParticipant.builder()
                .conversation(conversation)
                .user(user2)
                .joinedAt(LocalDateTime.now())
                .build();

        participantRepository.save(participant1);
        participantRepository.save(participant2);

        return conversation;
    }

    // Mark conversation as read for a user
    @Transactional
    public void markAsRead(Integer conversationId, Integer userId) {
        ConversationParticipant participant = participantRepository
                .findByConversationConversationIdAndUserUserId(conversationId, userId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
        
        participant.setLastReadAt(LocalDateTime.now());
        participantRepository.save(participant);
    }

    private ConversationDto convertToDto(Conversation conversation) {
        ConversationDto dto = new ConversationDto();
        dto.setConversationId(conversation.getConversationId());
        dto.setCreatedAt(conversation.getCreatedAt());
        dto.setUpdatedAt(conversation.getUpdatedAt());
        dto.setLastMessageAt(conversation.getLastMessageAt());
        
        if (conversation.getProject() != null) {
            dto.setProjectId(conversation.getProject().getProjectId());
        }
        if (conversation.getNgoProject() != null) {
            dto.setNgoProjectId(conversation.getNgoProject().getNgoProjectId());
        }

        // Get participants
        List<ConversationParticipant> participants = participantRepository
                .findByConversationConversationId(conversation.getConversationId());
        dto.setParticipants(participants.stream()
                .map(this::convertParticipantToDto)
                .collect(Collectors.toList()));

        // Get last message
        messageRepository.findLatestByConversationId(conversation.getConversationId())
                .ifPresent(message -> dto.setLastMessage(convertMessageToDto(message)));

        return dto;
    }

    private ConversationParticipantDto convertParticipantToDto(ConversationParticipant participant) {
        ConversationParticipantDto dto = new ConversationParticipantDto();
        dto.setParticipantId(participant.getParticipantId());
        dto.setConversationId(participant.getConversation().getConversationId());
        dto.setUserId(participant.getUser().getUserId());
        dto.setUserName(participant.getUser().getUsername());
        dto.setUserType(participant.getUser().getUserType().toString());
        dto.setJoinedAt(participant.getJoinedAt());
        dto.setLastReadAt(participant.getLastReadAt());
        return dto;
    }

    private MessageDto convertMessageToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setMessageId(message.getMessageId());
        dto.setConversationId(message.getConversation().getConversationId());
        dto.setSenderId(message.getSender().getUserId());
        dto.setSenderName(message.getSender().getUsername());
        dto.setSenderType(message.getSender().getUserType().toString());
        dto.setMessageType(message.getMessageType());
        dto.setMessageText(message.getMessageText());
        dto.setImageUrl(message.getImageUrl());
        dto.setSentAt(message.getSentAt());
        return dto;
    }
}