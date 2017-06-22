package com.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chat.domain.ChatMessage;

/**
 * Spring Data JPA repository for the ChatMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    @Query("select chat_message from ChatMessage chat_message where chat_message.user.login = ?#{principal.username}")
    List<ChatMessage> findByUserIsCurrentUser();

    @Query("select chat_message from ChatMessage chat_message where chat_message.chat.id = :#{id} order by chat_message.senttime asc")
    List<ChatMessage> findByChatId(@Param("id") Long id);
    
}
