package com.chat.service.impl;

import com.chat.service.ChatService;
import com.chat.domain.Chat;
import com.chat.repository.ChatRepository;
import com.chat.service.dto.ChatDTO;
import com.chat.service.mapper.ChatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Chat.
 */
@Service
@Transactional
public class ChatServiceImpl implements ChatService{

    private final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private final ChatRepository chatRepository;

    private final ChatMapper chatMapper;

    public ChatServiceImpl(ChatRepository chatRepository, ChatMapper chatMapper) {
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
    }

    /**
     * Save a chat.
     *
     * @param chatDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ChatDTO save(ChatDTO chatDTO) {
        log.debug("Request to save Chat : {}", chatDTO);
        Chat chat = chatMapper.toEntity(chatDTO);
        chat = chatRepository.save(chat);
        return chatMapper.toDto(chat);
    }

    /**
     *  Get all the chats.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChatDTO> findAll() {
        log.debug("Request to get all Chats");
        return chatRepository.findAll().stream()
            .map(chatMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one chat by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ChatDTO findOne(Long id) {
        log.debug("Request to get Chat : {}", id);
        Chat chat = chatRepository.findOne(id);
        return chatMapper.toDto(chat);
    }

    /**
     *  Delete the  chat by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Chat : {}", id);
        chatRepository.delete(id);
    }
}
