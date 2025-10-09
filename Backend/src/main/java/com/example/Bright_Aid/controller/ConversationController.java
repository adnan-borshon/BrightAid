package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.ConversationDto;
import com.example.Bright_Aid.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    // Get all conversations for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConversationDto>> getUserConversations(@PathVariable Integer userId) {
        List<ConversationDto> conversations = conversationService.getUserConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    // Create or get conversation between two users for a project
    @PostMapping("/create")
    public ResponseEntity<ConversationDto> createConversation(
            @RequestParam Integer userId1,
            @RequestParam Integer userId2,
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) Integer ngoProjectId) {
        
        ConversationDto conversation = conversationService.createOrGetConversation(userId1, userId2, projectId, ngoProjectId);
        return ResponseEntity.ok(conversation);
    }

    // Mark conversation as read
    @PutMapping("/{conversationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Integer conversationId,
            @RequestParam Integer userId) {
        
        conversationService.markAsRead(conversationId, userId);
        return ResponseEntity.ok().build();
    }
}