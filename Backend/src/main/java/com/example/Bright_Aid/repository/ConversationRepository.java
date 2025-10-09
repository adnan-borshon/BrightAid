package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    // Find conversations for a specific user
    @Query("SELECT DISTINCT c FROM Conversation c " +
           "JOIN c.participants p " +
           "WHERE p.user.userId = :userId " +
           "ORDER BY c.lastMessageAt DESC")
    List<Conversation> findByUserId(@Param("userId") Integer userId);

    // Find conversation between two users for a specific project
    @Query("SELECT c FROM Conversation c " +
           "JOIN c.participants p1 " +
           "JOIN c.participants p2 " +
           "WHERE p1.user.userId = :userId1 " +
           "AND p2.user.userId = :userId2 " +
           "AND c.project.projectId = :projectId")
    Optional<Conversation> findByUsersAndProject(@Param("userId1") Integer userId1, 
                                                @Param("userId2") Integer userId2, 
                                                @Param("projectId") Integer projectId);

    // Find conversation between two users for a specific NGO project
    @Query("SELECT c FROM Conversation c " +
           "JOIN c.participants p1 " +
           "JOIN c.participants p2 " +
           "WHERE p1.user.userId = :userId1 " +
           "AND p2.user.userId = :userId2 " +
           "AND c.ngoProject.ngoProjectId = :ngoProjectId")
    Optional<Conversation> findByUsersAndNgoProject(@Param("userId1") Integer userId1, 
                                                   @Param("userId2") Integer userId2, 
                                                   @Param("ngoProjectId") Integer ngoProjectId);
}