package com.github.tarkil.web.rest;

import com.github.tarkil.CardsApp;

import com.github.tarkil.config.SecurityBeanOverrideConfiguration;

import com.github.tarkil.domain.CardType;
import com.github.tarkil.repository.CardTypeRepository;
import com.github.tarkil.service.CardTypeService;
import com.github.tarkil.repository.search.CardTypeSearchRepository;
import com.github.tarkil.service.dto.CardTypeDTO;
import com.github.tarkil.service.mapper.CardTypeMapper;
import com.github.tarkil.web.rest.errors.ExceptionTranslator;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CardTypeResource REST controller.
 *
 * @see CardTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CardsApp.class, SecurityBeanOverrideConfiguration.class})
public class CardTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CardTypeRepository cardTypeRepository;

    @Autowired
    private CardTypeMapper cardTypeMapper;

    @Autowired
    private CardTypeService cardTypeService;

    @Autowired
    private CardTypeSearchRepository cardTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCardTypeMockMvc;

    private CardType cardType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CardTypeResource cardTypeResource = new CardTypeResource(cardTypeService);
        this.restCardTypeMockMvc = MockMvcBuilders.standaloneSetup(cardTypeResource)
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
    public static CardType createEntity(EntityManager em) {
        CardType cardType = new CardType()
                .name(DEFAULT_NAME);
        return cardType;
    }

    @Before
    public void initTest() {
        cardTypeSearchRepository.deleteAll();
        cardType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCardType() throws Exception {
        int databaseSizeBeforeCreate = cardTypeRepository.findAll().size();

        // Create the CardType
        CardTypeDTO cardTypeDTO = cardTypeMapper.cardTypeToCardTypeDTO(cardType);

        restCardTypeMockMvc.perform(post("/api/card-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the CardType in the database
        List<CardType> cardTypeList = cardTypeRepository.findAll();
        assertThat(cardTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CardType testCardType = cardTypeList.get(cardTypeList.size() - 1);
        assertThat(testCardType.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the CardType in Elasticsearch
        CardType cardTypeEs = cardTypeSearchRepository.findOne(testCardType.getId());
        assertThat(cardTypeEs).isEqualToComparingFieldByField(testCardType);
    }

    @Test
    @Transactional
    public void createCardTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cardTypeRepository.findAll().size();

        // Create the CardType with an existing ID
        CardType existingCardType = new CardType();
        existingCardType.setId(1L);
        CardTypeDTO existingCardTypeDTO = cardTypeMapper.cardTypeToCardTypeDTO(existingCardType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardTypeMockMvc.perform(post("/api/card-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCardTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CardType> cardTypeList = cardTypeRepository.findAll();
        assertThat(cardTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardTypeRepository.findAll().size();
        // set the field null
        cardType.setName(null);

        // Create the CardType, which fails.
        CardTypeDTO cardTypeDTO = cardTypeMapper.cardTypeToCardTypeDTO(cardType);

        restCardTypeMockMvc.perform(post("/api/card-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardTypeDTO)))
            .andExpect(status().isBadRequest());

        List<CardType> cardTypeList = cardTypeRepository.findAll();
        assertThat(cardTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCardTypes() throws Exception {
        // Initialize the database
        cardTypeRepository.saveAndFlush(cardType);

        // Get all the cardTypeList
        restCardTypeMockMvc.perform(get("/api/card-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCardType() throws Exception {
        // Initialize the database
        cardTypeRepository.saveAndFlush(cardType);

        // Get the cardType
        restCardTypeMockMvc.perform(get("/api/card-types/{id}", cardType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cardType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCardType() throws Exception {
        // Get the cardType
        restCardTypeMockMvc.perform(get("/api/card-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCardType() throws Exception {
        // Initialize the database
        cardTypeRepository.saveAndFlush(cardType);
        cardTypeSearchRepository.save(cardType);
        int databaseSizeBeforeUpdate = cardTypeRepository.findAll().size();

        // Update the cardType
        CardType updatedCardType = cardTypeRepository.findOne(cardType.getId());
        updatedCardType
                .name(UPDATED_NAME);
        CardTypeDTO cardTypeDTO = cardTypeMapper.cardTypeToCardTypeDTO(updatedCardType);

        restCardTypeMockMvc.perform(put("/api/card-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardTypeDTO)))
            .andExpect(status().isOk());

        // Validate the CardType in the database
        List<CardType> cardTypeList = cardTypeRepository.findAll();
        assertThat(cardTypeList).hasSize(databaseSizeBeforeUpdate);
        CardType testCardType = cardTypeList.get(cardTypeList.size() - 1);
        assertThat(testCardType.getName()).isEqualTo(UPDATED_NAME);

        // Validate the CardType in Elasticsearch
        CardType cardTypeEs = cardTypeSearchRepository.findOne(testCardType.getId());
        assertThat(cardTypeEs).isEqualToComparingFieldByField(testCardType);
    }

    @Test
    @Transactional
    public void updateNonExistingCardType() throws Exception {
        int databaseSizeBeforeUpdate = cardTypeRepository.findAll().size();

        // Create the CardType
        CardTypeDTO cardTypeDTO = cardTypeMapper.cardTypeToCardTypeDTO(cardType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCardTypeMockMvc.perform(put("/api/card-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the CardType in the database
        List<CardType> cardTypeList = cardTypeRepository.findAll();
        assertThat(cardTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCardType() throws Exception {
        // Initialize the database
        cardTypeRepository.saveAndFlush(cardType);
        cardTypeSearchRepository.save(cardType);
        int databaseSizeBeforeDelete = cardTypeRepository.findAll().size();

        // Get the cardType
        restCardTypeMockMvc.perform(delete("/api/card-types/{id}", cardType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cardTypeExistsInEs = cardTypeSearchRepository.exists(cardType.getId());
        assertThat(cardTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<CardType> cardTypeList = cardTypeRepository.findAll();
        assertThat(cardTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCardType() throws Exception {
        // Initialize the database
        cardTypeRepository.saveAndFlush(cardType);
        cardTypeSearchRepository.save(cardType);

        // Search the cardType
        restCardTypeMockMvc.perform(get("/api/_search/card-types?query=id:" + cardType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardType.class);
    }
}
