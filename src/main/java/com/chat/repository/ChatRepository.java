package com.chat.repository;

import com.chat.domain.Chat;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Chat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {

    @Query("select chat from Chat chat where chat.user1.login = ?#{principal.username}")
    List<Chat> findByUser1IsCurrentUser();

    @Query("select chat from Chat chat where chat.user2.login = ?#{principal.username}")
    List<Chat> findByUser2IsCurrentUser();
    
}
