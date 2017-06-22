package com.chat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.domain.User;
import com.chat.security.SecurityUtils;
import com.chat.service.ChatService;
import com.chat.service.UserService;
import com.chat.service.dto.ChatDTO;
import com.chat.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Chat.
 */
@RestController
@RequestMapping("/api")
public class ChatResource {

    private final Logger log = LoggerFactory.getLogger(ChatResource.class);

    private static final String ENTITY_NAME = "chat";

    private final ChatService chatService;
    private final UserService userService;

    public ChatResource(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    /**
     * POST  /chats : Create a new chat.
     *
     * @param chatDTO the chatDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chatDTO, or with status 400 (Bad Request) if the chat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chats")
    @Timed
    public ResponseEntity<ChatDTO> createChat(@RequestBody ChatDTO chatDTO) throws URISyntaxException {
        log.debug("REST request to save Chat : {}", chatDTO);
        if (chatDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new chat cannot already have an ID")).body(null);
        }
        if(chatDTO.getCreationtime() == null){
        	chatDTO.setCreationtime(ZonedDateTime.now());
        }
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        User currentlyLoggedInUser =  userService.getUserWithAuthoritiesByLogin(currentUserLogin).get();
        chatDTO.setUser1Id(currentlyLoggedInUser.getId());
        chatDTO.setUser1Login(currentlyLoggedInUser.getLogin());
        ChatDTO result = chatService.save(chatDTO);
        return ResponseEntity.created(new URI("/api/chats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chats : Updates an existing chat.
     *
     * @param chatDTO the chatDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chatDTO,
     * or with status 400 (Bad Request) if the chatDTO is not valid,
     * or with status 500 (Internal Server Error) if the chatDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chats")
    @Timed
    public ResponseEntity<ChatDTO> updateChat(@RequestBody ChatDTO chatDTO) throws URISyntaxException {
        log.debug("REST request to update Chat : {}", chatDTO);
        if (chatDTO.getId() == null) {
            return createChat(chatDTO);
        }
        ChatDTO result = chatService.save(chatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chatDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chats : get all the chats.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of chats in body
     */
    @GetMapping("/chats")
    @Timed
    public List<ChatDTO> getAllChats() {
        log.debug("REST request to get all Chats");
        return chatService.findAll();
    }
    
    /**
     * GET  /chats : get all the chats.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of chats in body
     */
    @GetMapping("/chats-user")
    @Timed
    public List<ChatDTO> getAllChatsByUser() {
        log.debug("REST request to get all Chats");
        return chatService.findAllByUser();
    }

    /**
     * GET  /chats/:id : get the "id" chat.
     *
     * @param id the id of the chatDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chatDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chats/{id}")
    @Timed
    public ResponseEntity<ChatDTO> getChat(@PathVariable Long id) {
        log.debug("REST request to get Chat : {}", id);
        ChatDTO chatDTO = chatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chatDTO));
    }

    /**
     * DELETE  /chats/:id : delete the "id" chat.
     *
     * @param id the id of the chatDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chats/{id}")
    @Timed
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        log.debug("REST request to delete Chat : {}", id);
        chatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
