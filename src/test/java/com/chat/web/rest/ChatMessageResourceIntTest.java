package com.chat.web.rest;

import com.chat.ChatApp;

import com.chat.domain.ChatMessage;
import com.chat.repository.ChatMessageRepository;
import com.chat.service.ChatMessageService;
import com.chat.service.dto.ChatMessageDTO;
import com.chat.service.mapper.ChatMessageMapper;
import com.chat.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.chat.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ChatMessageResource REST controller.
 *
 * @see ChatMessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChatApp.class)
public class ChatMessageResourceIntTest {

    private static final String DEFAULT_STORY = "AAAAAAAAAA";
    private static final String UPDATED_STORY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SENTTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SENTTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ChatMessageRepository chatmessageRepository;

    @Autowired
    private ChatMessageMapper chatmessageMapper;

    @Autowired
    private ChatMessageService chatmessageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChatMessageMockMvc;

    private ChatMessage chatmessage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChatMessageResource chatmessageResource = new ChatMessageResource(chatmessageService, null);
        this.restChatMessageMockMvc = MockMvcBuilders.standaloneSetup(chatmessageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver) 
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatMessage createEntity(EntityManager em) {
        ChatMessage chatmessage = new ChatMessage()
            .story(DEFAULT_STORY)
            .senttime(DEFAULT_SENTTIME);
        return chatmessage;
    }

    @Before
    public void initTest() {
        chatmessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatMessage() throws Exception {
        int databaseSizeBeforeCreate = chatmessageRepository.findAll().size();

        // Create the ChatMessage
        ChatMessageDTO chatmessageDTO = chatmessageMapper.toDto(chatmessage);
        restChatMessageMockMvc.perform(post("/api/chatmessages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatmessageDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatmessageList = chatmessageRepository.findAll();
        assertThat(chatmessageList).hasSize(databaseSizeBeforeCreate + 1);
        ChatMessage testChatMessage = chatmessageList.get(chatmessageList.size() - 1);
        assertThat(testChatMessage.getStory()).isEqualTo(DEFAULT_STORY);
        assertThat(testChatMessage.getSenttime()).isEqualTo(DEFAULT_SENTTIME);
    }

    @Test
    @Transactional
    public void createChatMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatmessageRepository.findAll().size();

        // Create the ChatMessage with an existing ID
        chatmessage.setId(1L);
        ChatMessageDTO chatmessageDTO = chatmessageMapper.toDto(chatmessage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatMessageMockMvc.perform(post("/api/chatmessages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatmessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ChatMessage> chatmessageList = chatmessageRepository.findAll();
        assertThat(chatmessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllChatMessages() throws Exception {
        // Initialize the database
        chatmessageRepository.saveAndFlush(chatmessage);

        // Get all the chatmessageList
        restChatMessageMockMvc.perform(get("/api/chatmessages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatmessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].story").value(hasItem(DEFAULT_STORY.toString())))
            .andExpect(jsonPath("$.[*].senttime").value(hasItem(sameInstant(DEFAULT_SENTTIME))));
    }

    @Test
    @Transactional
    public void getChatMessage() throws Exception {
        // Initialize the database
        chatmessageRepository.saveAndFlush(chatmessage);

        // Get the chatmessage
        restChatMessageMockMvc.perform(get("/api/chatmessages/{id}", chatmessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatmessage.getId().intValue()))
            .andExpect(jsonPath("$.story").value(DEFAULT_STORY.toString()))
            .andExpect(jsonPath("$.senttime").value(sameInstant(DEFAULT_SENTTIME)));
    }

    @Test
    @Transactional
    public void getNonExistingChatMessage() throws Exception {
        // Get the chatmessage
        restChatMessageMockMvc.perform(get("/api/chatmessages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatMessage() throws Exception {
        // Initialize the database
        chatmessageRepository.saveAndFlush(chatmessage);
        int databaseSizeBeforeUpdate = chatmessageRepository.findAll().size();

        // Update the chatmessage
        ChatMessage updatedChatMessage = chatmessageRepository.findOne(chatmessage.getId());
        updatedChatMessage
            .story(UPDATED_STORY)
            .senttime(UPDATED_SENTTIME);
        ChatMessageDTO chatmessageDTO = chatmessageMapper.toDto(updatedChatMessage);

        restChatMessageMockMvc.perform(put("/api/chatmessages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatmessageDTO)))
            .andExpect(status().isOk());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatmessageList = chatmessageRepository.findAll();
        assertThat(chatmessageList).hasSize(databaseSizeBeforeUpdate);
        ChatMessage testChatMessage = chatmessageList.get(chatmessageList.size() - 1);
        assertThat(testChatMessage.getStory()).isEqualTo(UPDATED_STORY);
        assertThat(testChatMessage.getSenttime()).isEqualTo(UPDATED_SENTTIME);
    }

    @Test
    @Transactional
    public void updateNonExistingChatMessage() throws Exception {
        int databaseSizeBeforeUpdate = chatmessageRepository.findAll().size();

        // Create the ChatMessage
        ChatMessageDTO chatmessageDTO = chatmessageMapper.toDto(chatmessage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChatMessageMockMvc.perform(put("/api/chatmessages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatmessageDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatmessageList = chatmessageRepository.findAll();
        assertThat(chatmessageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteChatMessage() throws Exception {
        // Initialize the database
        chatmessageRepository.saveAndFlush(chatmessage);
        int databaseSizeBeforeDelete = chatmessageRepository.findAll().size();

        // Get the chatmessage
        restChatMessageMockMvc.perform(delete("/api/chatmessages/{id}", chatmessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ChatMessage> chatmessageList = chatmessageRepository.findAll();
        assertThat(chatmessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessage.class);
        ChatMessage chatmessage1 = new ChatMessage();
        chatmessage1.setId(1L);
        ChatMessage chatmessage2 = new ChatMessage();
        chatmessage2.setId(chatmessage1.getId());
        assertThat(chatmessage1).isEqualTo(chatmessage2);
        chatmessage2.setId(2L);
        assertThat(chatmessage1).isNotEqualTo(chatmessage2);
        chatmessage1.setId(null);
        assertThat(chatmessage1).isNotEqualTo(chatmessage2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessageDTO.class);
        ChatMessageDTO chatmessageDTO1 = new ChatMessageDTO();
        chatmessageDTO1.setId(1L);
        ChatMessageDTO chatmessageDTO2 = new ChatMessageDTO();
        assertThat(chatmessageDTO1).isNotEqualTo(chatmessageDTO2);
        chatmessageDTO2.setId(chatmessageDTO1.getId());
        assertThat(chatmessageDTO1).isEqualTo(chatmessageDTO2);
        chatmessageDTO2.setId(2L);
        assertThat(chatmessageDTO1).isNotEqualTo(chatmessageDTO2);
        chatmessageDTO1.setId(null);
        assertThat(chatmessageDTO1).isNotEqualTo(chatmessageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chatmessageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chatmessageMapper.fromId(null)).isNull();
    }
}
