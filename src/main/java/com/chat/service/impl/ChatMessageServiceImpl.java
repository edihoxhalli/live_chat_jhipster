package com.chat.service.impl;

import com.chat.service.ChatMessageService;
import com.chat.domain.ChatMessage;
import com.chat.repository.ChatMessageRepository;
import com.chat.service.dto.ChatMessageDTO;
import com.chat.service.mapper.ChatMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ChatMessage.
 */
@Service
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService{

    private final Logger log = LoggerFactory.getLogger(ChatMessageServiceImpl.class);

    private final ChatMessageRepository chatmessageRepository;

    private final ChatMessageMapper chatmessageMapper;

    public ChatMessageServiceImpl(ChatMessageRepository chatmessageRepository, ChatMessageMapper chatmessageMapper) {
        this.chatmessageRepository = chatmessageRepository;
        this.chatmessageMapper = chatmessageMapper;
    }

    /**
     * Save a chatmessage.
     *
     * @param chatmessageDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ChatMessageDTO save(ChatMessageDTO chatmessageDTO) {
        log.debug("Request to save ChatMessage : {}", chatmessageDTO);
        ChatMessage chatmessage = chatmessageMapper.toEntity(chatmessageDTO);
        chatmessage = chatmessageRepository.save(chatmessage);
        return chatmessageMapper.toDto(chatmessage);
    }

    /**
     *  Get all the chatmessages.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> findAll() {
        log.debug("Request to get all ChatMessages");
        return chatmessageRepository.findAll().stream()
            .map(chatmessageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
    
    /**
     *  Get all the chatmessages of a chat.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> findAllByChat(Long chatId) {
        log.debug("Request to get all ChatMessages");
        return chatmessageRepository.findByChatId(chatId).stream()
            .map(chatmessageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one chatmessage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ChatMessageDTO findOne(Long id) {
        log.debug("Request to get ChatMessage : {}", id);
        ChatMessage chatmessage = chatmessageRepository.findOne(id);
        return chatmessageMapper.toDto(chatmessage);
    }

    /**
     *  Delete the  chatmessage by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChatMessage : {}", id);
        chatmessageRepository.delete(id);
    }
}
