package com.chat.service.impl;

import com.chat.service.ChatmessageService;
import com.chat.domain.Chatmessage;
import com.chat.repository.ChatmessageRepository;
import com.chat.service.dto.ChatmessageDTO;
import com.chat.service.mapper.ChatmessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Chatmessage.
 */
@Service
@Transactional
public class ChatmessageServiceImpl implements ChatmessageService{

    private final Logger log = LoggerFactory.getLogger(ChatmessageServiceImpl.class);

    private final ChatmessageRepository chatmessageRepository;

    private final ChatmessageMapper chatmessageMapper;

    public ChatmessageServiceImpl(ChatmessageRepository chatmessageRepository, ChatmessageMapper chatmessageMapper) {
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
    public ChatmessageDTO save(ChatmessageDTO chatmessageDTO) {
        log.debug("Request to save Chatmessage : {}", chatmessageDTO);
        Chatmessage chatmessage = chatmessageMapper.toEntity(chatmessageDTO);
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
    public List<ChatmessageDTO> findAll() {
        log.debug("Request to get all Chatmessages");
        return chatmessageRepository.findAll().stream()
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
    public ChatmessageDTO findOne(Long id) {
        log.debug("Request to get Chatmessage : {}", id);
        Chatmessage chatmessage = chatmessageRepository.findOne(id);
        return chatmessageMapper.toDto(chatmessage);
    }

    /**
     *  Delete the  chatmessage by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Chatmessage : {}", id);
        chatmessageRepository.delete(id);
    }
}
