package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.MessageDto;
import com.example.Bright_Aid.Entity.*;
import com.example.Bright_Aid.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    // Send a text message
    @Transactional
    public MessageDto sendTextMessage(Integer conversationId, Integer senderId, String messageText) {
        return sendMessage(conversationId, senderId, Message.MessageType.TEXT, messageText, null);
    }

    // Send an image message
    @Transactional
    public MessageDto sendImageMessage(Integer conversationId, Integer senderId, String imageUrl) {
        return sendMessage(conversationId, senderId, Message.MessageType.IMAGE, null, imageUrl);
    }

    private MessageDto sendMessage(Integer conversationId, Integer senderId, Message.MessageType messageType, 
                                  String messageText, String imageUrl) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found: " + conversationId));
        
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found: " + senderId));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .messageType(messageType)
                .messageText(messageText)
                .imageUrl(imageUrl)
                .sentAt(LocalDateTime.now())
                .build();

        message = messageRepository.save(message);

        // Update conversation's last message time
        conversation.setLastMessageAt(message.getSentAt());
        conversationRepository.save(conversation);

        return convertToDto(message);
    }

    // Get messages for a conversation with pagination
    public List<MessageDto> getConversationMessages(Integer conversationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages = messageRepository.findByConversationConversationIdOrderBySentAtDesc(conversationId, pageable);
        
        return messages.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MessageDto convertToDto(Message message) {
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