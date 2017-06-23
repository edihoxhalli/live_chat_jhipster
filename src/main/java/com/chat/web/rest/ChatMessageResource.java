package com.chat.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.chat.domain.User;
import com.chat.security.SecurityUtils;
import com.chat.service.ChatMessageService;
import com.chat.service.UserService;
import com.chat.web.rest.util.HeaderUtil;
import com.chat.service.dto.ChatMessageDTO;
import com.chat.service.dto.ChatMessageDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ChatMessage.
 */
@RestController
@RequestMapping("/api")
public class ChatMessageResource {

    private final Logger log = LoggerFactory.getLogger(ChatMessageResource.class);

    private static final String ENTITY_NAME = "chatmessage";

    private final ChatMessageService chatmessageService;
    
    private final UserService userService;

    public ChatMessageResource(ChatMessageService chatmessageService, UserService userService) {
        this.chatmessageService = chatmessageService;
        this.userService = userService;
    }

    /**
     * POST  /chatmessages : Create a new chatmessage.
     *
     * @param chatmessageDTO the chatmessageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chatmessageDTO, or with status 400 (Bad Request) if the chatmessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chatmessages")
    @Timed
    public ResponseEntity<ChatMessageDTO> createChatMessage(@RequestBody ChatMessageDTO chatmessageDTO) throws URISyntaxException {
        log.debug("REST request to save ChatMessage : {}", chatmessageDTO);
        if (chatmessageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new chatmessage cannot already have an ID")).body(null);
        }
        ChatMessageDTO result = chatmessageService.save(chatmessageDTO);
        return ResponseEntity.created(new URI("/api/chatmessages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /chatmessages : Create a new chatmessage.
     *
     * @param chatmessageDTO the chatmessageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chatmessageDTO, or with status 400 (Bad Request) if the chatmessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chatmessages-send")
    @Timed
    public ResponseEntity<ChatMessageDTO> sendChatMessage(@RequestBody ChatMessageDTO chatmessageDTO) throws URISyntaxException {
        log.debug("REST request to save ChatMessage : {}", chatmessageDTO);
        if (chatmessageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new chatmessage cannot already have an ID")).body(null);
        }
        chatmessageDTO.setSenttime(ZonedDateTime.now());
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        User currentlyLoggedInUser =  userService.getUserWithAuthoritiesByLogin(currentUserLogin).get();
        chatmessageDTO.setUserId(currentlyLoggedInUser.getId());
        chatmessageDTO.setUserLogin(currentlyLoggedInUser.getLogin());
        ChatMessageDTO result = chatmessageService.save(chatmessageDTO);
        if(result.getUserLogin() == null)
        	result.setUserLogin(currentlyLoggedInUser.getLogin());
        return ResponseEntity.created(new URI("/api/chatmessages-send/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * PUT  /chatmessages : Updates an existing chatmessage.
     *
     * @param chatmessageDTO the chatmessageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chatmessageDTO,
     * or with status 400 (Bad Request) if the chatmessageDTO is not valid,
     * or with status 500 (Internal Server Error) if the chatmessageDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chatmessages")
    @Timed
    public ResponseEntity<ChatMessageDTO> updateChatMessage(@RequestBody ChatMessageDTO chatmessageDTO) throws URISyntaxException {
        log.debug("REST request to update ChatMessage : {}", chatmessageDTO);
        if (chatmessageDTO.getId() == null) {
            return createChatMessage(chatmessageDTO);
        }
        ChatMessageDTO result = chatmessageService.save(chatmessageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chatmessageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chatmessages : get all the chatmessages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of chatmessages in body
     */
    @GetMapping("/chatmessages")
    @Timed
    public List<ChatMessageDTO> getAllChatMessages() {
        log.debug("REST request to get all ChatMessages");
        return chatmessageService.findAll();
    }
    
    /**
     * GET  /chatmessages : get all the chatmessages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of chatmessages in body
     */
    @GetMapping("/chatmessages-by-chat/{id}")
    @Timed
    public List<ChatMessageDTO> getAllChatMessagesByIdChat(@PathVariable Long id) {
        log.debug("REST request to get all ChatMessages");
        return chatmessageService.findAllByChat(id);
    }

    /**
     * GET  /chatmessages/:id : get the "id" chatmessage.
     *
     * @param id the id of the chatmessageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chatmessageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chatmessages/{id}")
    @Timed
    public ResponseEntity<ChatMessageDTO> getChatMessage(@PathVariable Long id) {
        log.debug("REST request to get ChatMessage : {}", id);
        ChatMessageDTO chatmessageDTO = chatmessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chatmessageDTO));
    }

    /**
     * DELETE  /chatmessages/:id : delete the "id" chatmessage.
     *
     * @param id the id of the chatmessageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chatmessages/{id}")
    @Timed
    public ResponseEntity<Void> deleteChatMessage(@PathVariable Long id) {
        log.debug("REST request to delete ChatMessage : {}", id);
        chatmessageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
