package com.chat.web.rest;

import com.chat.ChatApp;

import com.chat.domain.Chatmessage;
import com.chat.repository.ChatmessageRepository;
import com.chat.service.ChatmessageService;
import com.chat.service.dto.ChatmessageDTO;
import com.chat.service.mapper.ChatmessageMapper;
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
 * Test class for the ChatmessageResource REST controller.
 *
 * @see ChatmessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChatApp.class)
public class ChatmessageResourceIntTest {

    private static final String DEFAULT_STORY = "AAAAAAAAAA";
    private static final String UPDATED_STORY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SENTTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SENTTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ChatmessageRepository chatmessageRepository;

    @Autowired
    private ChatmessageMapper chatmessageMapper;

    @Autowired
    private ChatmessageService chatmessageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChatmessageMockMvc;

    private Chatmessage chatmessage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChatmessageResource chatmessageResource = new ChatmessageResource(chatmessageService);
        this.restChatmessageMockMvc = MockMvcBuilders.standaloneSetup(chatmessageResource)
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
    public static Chatmessage createEntity(EntityManager em) {
        Chatmessage chatmessage = new Chatmessage()
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
    public void createChatmessage() throws Exception {
        int databaseSizeBeforeCreate = chatmessageRepository.findAll().size();

        // Create the Chatmessage
        ChatmessageDTO chatmessageDTO = chatmessageMapper.toDto(chatmessage);
        restChatmessageMockMvc.perform(post("/api/chatmessages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatmessageDTO)))
            .andExpect(status().isCreated());

        // Validate the Chatmessage in the database
        List<Chatmessage> chatmessageList = chatmessageRepository.findAll();
        assertThat(chatmessageList).hasSize(databaseSizeBeforeCreate + 1);
        Chatmessage testChatmessage = chatmessageList.get(chatmessageList.size() - 1);
        assertThat(testChatmessage.getStory()).isEqualTo(DEFAULT_STORY);
        assertThat(testChatmessage.getSenttime()).isEqualTo(DEFAULT_SENTTIME);
    }

    @Test
    @Transactional
    public void createChatmessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatmessageRepository.findAll().size();

        // Create the Chatmessage with an existing ID
        chatmessage.setId(1L);
        ChatmessageDTO chatmessageDTO = chatmessageMapper.toDto(chatmessage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatmessageMockMvc.perform(post("/api/chatmessages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatmessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Chatmessage> chatmessageList = chatmessageRepository.findAll();
        assertThat(chatmessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllChatmessages() throws Exception {
        // Initialize the database
        chatmessageRepository.saveAndFlush(chatmessage);

        // Get all the chatmessageList
        restChatmessageMockMvc.perform(get("/api/chatmessages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatmessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].story").value(hasItem(DEFAULT_STORY.toString())))
            .andExpect(jsonPath("$.[*].senttime").value(hasItem(sameInstant(DEFAULT_SENTTIME))));
    }

    @Test
    @Transactional
    public void getChatmessage() throws Exception {
        // Initialize the database
        chatmessageRepository.saveAndFlush(chatmessage);

        // Get the chatmessage
        restChatmessageMockMvc.perform(get("/api/chatmessages/{id}", chatmessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatmessage.getId().intValue()))
            .andExpect(jsonPath("$.story").value(DEFAULT_STORY.toString()))
            .andExpect(jsonPath("$.senttime").value(sameInstant(DEFAULT_SENTTIME)));
    }

    @Test
    @Transactional
    public void getNonExistingChatmessage() throws Exception {
        // Get the chatmessage
        restChatmessageMockMvc.perform(get("/api/chatmessages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatmessage() throws Exception {
        // Initialize the database
        chatmessageRepository.saveAndFlush(chatmessage);
        int databaseSizeBeforeUpdate = chatmessageRepository.findAll().size();

        // Update the chatmessage
        Chatmessage updatedChatmessage = chatmessageRepository.findOne(chatmessage.getId());
        updatedChatmessage
            .story(UPDATED_STORY)
            .senttime(UPDATED_SENTTIME);
        ChatmessageDTO chatmessageDTO = chatmessageMapper.toDto(updatedChatmessage);

        restChatmessageMockMvc.perform(put("/api/chatmessages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatmessageDTO)))
            .andExpect(status().isOk());

        // Validate the Chatmessage in the database
        List<Chatmessage> chatmessageList = chatmessageRepository.findAll();
        assertThat(chatmessageList).hasSize(databaseSizeBeforeUpdate);
        Chatmessage testChatmessage = chatmessageList.get(chatmessageList.size() - 1);
        assertThat(testChatmessage.getStory()).isEqualTo(UPDATED_STORY);
        assertThat(testChatmessage.getSenttime()).isEqualTo(UPDATED_SENTTIME);
    }

    @Test
    @Transactional
    public void updateNonExistingChatmessage() throws Exception {
        int databaseSizeBeforeUpdate = chatmessageRepository.findAll().size();

        // Create the Chatmessage
        ChatmessageDTO chatmessageDTO = chatmessageMapper.toDto(chatmessage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChatmessageMockMvc.perform(put("/api/chatmessages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatmessageDTO)))
            .andExpect(status().isCreated());

        // Validate the Chatmessage in the database
        List<Chatmessage> chatmessageList = chatmessageRepository.findAll();
        assertThat(chatmessageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteChatmessage() throws Exception {
        // Initialize the database
        chatmessageRepository.saveAndFlush(chatmessage);
        int databaseSizeBeforeDelete = chatmessageRepository.findAll().size();

        // Get the chatmessage
        restChatmessageMockMvc.perform(delete("/api/chatmessages/{id}", chatmessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Chatmessage> chatmessageList = chatmessageRepository.findAll();
        assertThat(chatmessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chatmessage.class);
        Chatmessage chatmessage1 = new Chatmessage();
        chatmessage1.setId(1L);
        Chatmessage chatmessage2 = new Chatmessage();
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
        TestUtil.equalsVerifier(ChatmessageDTO.class);
        ChatmessageDTO chatmessageDTO1 = new ChatmessageDTO();
        chatmessageDTO1.setId(1L);
        ChatmessageDTO chatmessageDTO2 = new ChatmessageDTO();
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
