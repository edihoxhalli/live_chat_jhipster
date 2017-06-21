package com.chat.service;

import com.chat.service.dto.ChatmessageDTO;
import java.util.List;

/**
 * Service Interface for managing Chatmessage.
 */
public interface ChatmessageService {

    /**
     * Save a chatmessage.
     *
     * @param chatmessageDTO the entity to save
     * @return the persisted entity
     */
    ChatmessageDTO save(ChatmessageDTO chatmessageDTO);

    /**
     *  Get all the chatmessages.
     *
     *  @return the list of entities
     */
    List<ChatmessageDTO> findAll();

    /**
     *  Get the "id" chatmessage.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChatmessageDTO findOne(Long id);

    /**
     *  Delete the "id" chatmessage.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
