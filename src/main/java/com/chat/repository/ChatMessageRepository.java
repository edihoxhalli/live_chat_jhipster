package com.chat.repository;

import com.chat.domain.Chatmessage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Chatmessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatmessageRepository extends JpaRepository<Chatmessage,Long> {

    @Query("select chat_message from Chatmessage chat_message where chat_message.user.login = ?#{principal.username}")
    List<Chatmessage> findByUserIsCurrentUser();
    
}
