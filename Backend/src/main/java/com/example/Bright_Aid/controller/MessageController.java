package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.MessageDto;
import com.example.Bright_Aid.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // Send a text message
    @PostMapping("/text")
    public ResponseEntity<MessageDto> sendTextMessage(@RequestBody Map<String, Object> request) {
        Integer conversationId = (Integer) request.get("conversationId");
        Integer senderId = (Integer) request.get("senderId");
        String messageText = (String) request.get("messageText");
        
        MessageDto message = messageService.sendTextMessage(conversationId, senderId, messageText);
        return ResponseEntity.ok(message);
    }

    // Send an image message
    @PostMapping("/image")
    public ResponseEntity<MessageDto> sendImageMessage(@RequestBody Map<String, Object> request) {
        Integer conversationId = (Integer) request.get("conversationId");
        Integer senderId = (Integer) request.get("senderId");
        String imageUrl = (String) request.get("imageUrl");
        
        MessageDto message = messageService.sendImageMessage(conversationId, senderId, imageUrl);
        return ResponseEntity.ok(message);
    }

    // Get messages for a conversation
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageDto>> getConversationMessages(
            @PathVariable Integer conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        List<MessageDto> messages = messageService.getConversationMessages(conversationId, page, size);
        return ResponseEntity.ok(messages);
    }
}