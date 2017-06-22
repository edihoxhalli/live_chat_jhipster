package com.chat.service;

import com.chat.service.dto.ChatMessageDTO;
import java.util.List;

/**
 * Service Interface for managing ChatMessage.
 */
public interface ChatMessageService {

    /**
     * Save a chatmessage.
     *
     * @param chatmessageDTO the entity to save
     * @return the persisted entity
     */
    ChatMessageDTO save(ChatMessageDTO chatmessageDTO);

    /**
     *  Get all the chatmessages.
     *
     *  @return the list of entities
     */
    List<ChatMessageDTO> findAll();

    /**
     *  Get all the chatmessages of a chat.
     *
     *  @return the list of entities
     */
    List<ChatMessageDTO> findAllByChat(Long chatId);
    /**
     *  Get the "id" chatmessage.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChatMessageDTO findOne(Long id);

    /**
     *  Delete the "id" chatmessage.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
