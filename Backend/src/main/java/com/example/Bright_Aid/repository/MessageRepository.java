package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    // Get messages for a conversation with pagination
    Page<Message> findByConversationConversationIdOrderBySentAtDesc(Integer conversationId, Pageable pageable);

    // Get latest message for a conversation
    @Query("SELECT m FROM Message m " +
           "WHERE m.conversation.conversationId = :conversationId " +
           "ORDER BY m.sentAt DESC " +
           "LIMIT 1")
    Optional<Message> findLatestByConversationId(@Param("conversationId") Integer conversationId);
}