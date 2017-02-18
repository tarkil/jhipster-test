package com.github.tarkil.web.rest;

import com.github.tarkil.CardsApp;

import com.github.tarkil.config.SecurityBeanOverrideConfiguration;

import com.github.tarkil.domain.Card;
import com.github.tarkil.repository.CardRepository;
import com.github.tarkil.service.CardService;
import com.github.tarkil.repository.search.CardSearchRepository;
import com.github.tarkil.service.dto.CardDTO;
import com.github.tarkil.service.mapper.CardMapper;
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

import com.github.tarkil.domain.enumeration.Rarity;
/**
 * Test class for the CardResource REST controller.
 *
 * @see CardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CardsApp.class, SecurityBeanOverrideConfiguration.class})
public class CardResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COST = "AAAAAAAAAA";
    private static final String UPDATED_COST = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_FLAVOUR = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_FLAVOUR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ATTACK = 1;
    private static final Integer UPDATED_ATTACK = 2;

    private static final Integer DEFAULT_DEFENSE = 1;
    private static final Integer UPDATED_DEFENSE = 2;

    private static final String DEFAULT_TRAIT = "AAAAAAAAAA";
    private static final String UPDATED_TRAIT = "BBBBBBBBBB";

    private static final Rarity DEFAULT_RARITY = Rarity.MYTHIC;
    private static final Rarity UPDATED_RARITY = Rarity.RARE;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private CardService cardService;

    @Autowired
    private CardSearchRepository cardSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCardMockMvc;

    private Card card;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CardResource cardResource = new CardResource(cardService);
        this.restCardMockMvc = MockMvcBuilders.standaloneSetup(cardResource)
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
    public static Card createEntity(EntityManager em) {
        Card card = new Card()
                .name(DEFAULT_NAME)
                .cost(DEFAULT_COST)
                .textFlavour(DEFAULT_TEXT_FLAVOUR)
                .description(DEFAULT_DESCRIPTION)
                .attack(DEFAULT_ATTACK)
                .defense(DEFAULT_DEFENSE)
                .trait(DEFAULT_TRAIT)
                .rarity(DEFAULT_RARITY);
        return card;
    }

    @Before
    public void initTest() {
        cardSearchRepository.deleteAll();
        card = createEntity(em);
    }

    @Test
    @Transactional
    public void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card
        CardDTO cardDTO = cardMapper.cardToCardDTO(card);

        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCard.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testCard.getTextFlavour()).isEqualTo(DEFAULT_TEXT_FLAVOUR);
        assertThat(testCard.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCard.getAttack()).isEqualTo(DEFAULT_ATTACK);
        assertThat(testCard.getDefense()).isEqualTo(DEFAULT_DEFENSE);
        assertThat(testCard.getTrait()).isEqualTo(DEFAULT_TRAIT);
        assertThat(testCard.getRarity()).isEqualTo(DEFAULT_RARITY);

        // Validate the Card in Elasticsearch
        Card cardEs = cardSearchRepository.findOne(testCard.getId());
        assertThat(cardEs).isEqualToComparingFieldByField(testCard);
    }

    @Test
    @Transactional
    public void createCardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card with an existing ID
        Card existingCard = new Card();
        existingCard.setId(1L);
        CardDTO existingCardDTO = cardMapper.cardToCardDTO(existingCard);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setName(null);

        // Create the Card, which fails.
        CardDTO cardDTO = cardMapper.cardToCardDTO(card);

        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCards() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList
        restCardMockMvc.perform(get("/api/cards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.toString())))
            .andExpect(jsonPath("$.[*].textFlavour").value(hasItem(DEFAULT_TEXT_FLAVOUR.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].attack").value(hasItem(DEFAULT_ATTACK)))
            .andExpect(jsonPath("$.[*].defense").value(hasItem(DEFAULT_DEFENSE)))
            .andExpect(jsonPath("$.[*].trait").value(hasItem(DEFAULT_TRAIT.toString())))
            .andExpect(jsonPath("$.[*].rarity").value(hasItem(DEFAULT_RARITY.toString())));
    }

    @Test
    @Transactional
    public void getCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.toString()))
            .andExpect(jsonPath("$.textFlavour").value(DEFAULT_TEXT_FLAVOUR.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.attack").value(DEFAULT_ATTACK))
            .andExpect(jsonPath("$.defense").value(DEFAULT_DEFENSE))
            .andExpect(jsonPath("$.trait").value(DEFAULT_TRAIT.toString()))
            .andExpect(jsonPath("$.rarity").value(DEFAULT_RARITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);
        cardSearchRepository.save(card);
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        Card updatedCard = cardRepository.findOne(card.getId());
        updatedCard
                .name(UPDATED_NAME)
                .cost(UPDATED_COST)
                .textFlavour(UPDATED_TEXT_FLAVOUR)
                .description(UPDATED_DESCRIPTION)
                .attack(UPDATED_ATTACK)
                .defense(UPDATED_DEFENSE)
                .trait(UPDATED_TRAIT)
                .rarity(UPDATED_RARITY);
        CardDTO cardDTO = cardMapper.cardToCardDTO(updatedCard);

        restCardMockMvc.perform(put("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCard.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testCard.getTextFlavour()).isEqualTo(UPDATED_TEXT_FLAVOUR);
        assertThat(testCard.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCard.getAttack()).isEqualTo(UPDATED_ATTACK);
        assertThat(testCard.getDefense()).isEqualTo(UPDATED_DEFENSE);
        assertThat(testCard.getTrait()).isEqualTo(UPDATED_TRAIT);
        assertThat(testCard.getRarity()).isEqualTo(UPDATED_RARITY);

        // Validate the Card in Elasticsearch
        Card cardEs = cardSearchRepository.findOne(testCard.getId());
        assertThat(cardEs).isEqualToComparingFieldByField(testCard);
    }

    @Test
    @Transactional
    public void updateNonExistingCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Create the Card
        CardDTO cardDTO = cardMapper.cardToCardDTO(card);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCardMockMvc.perform(put("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);
        cardSearchRepository.save(card);
        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Get the card
        restCardMockMvc.perform(delete("/api/cards/{id}", card.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cardExistsInEs = cardSearchRepository.exists(card.getId());
        assertThat(cardExistsInEs).isFalse();

        // Validate the database is empty
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);
        cardSearchRepository.save(card);

        // Search the card
        restCardMockMvc.perform(get("/api/_search/cards?query=id:" + card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.toString())))
            .andExpect(jsonPath("$.[*].textFlavour").value(hasItem(DEFAULT_TEXT_FLAVOUR.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].attack").value(hasItem(DEFAULT_ATTACK)))
            .andExpect(jsonPath("$.[*].defense").value(hasItem(DEFAULT_DEFENSE)))
            .andExpect(jsonPath("$.[*].trait").value(hasItem(DEFAULT_TRAIT.toString())))
            .andExpect(jsonPath("$.[*].rarity").value(hasItem(DEFAULT_RARITY.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Card.class);
    }
}
