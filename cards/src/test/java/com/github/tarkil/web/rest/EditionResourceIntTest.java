package com.github.tarkil.web.rest;

import com.github.tarkil.CardsApp;

import com.github.tarkil.config.SecurityBeanOverrideConfiguration;

import com.github.tarkil.domain.Edition;
import com.github.tarkil.repository.EditionRepository;
import com.github.tarkil.service.EditionService;
import com.github.tarkil.repository.search.EditionSearchRepository;
import com.github.tarkil.service.dto.EditionDTO;
import com.github.tarkil.service.mapper.EditionMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EditionResource REST controller.
 *
 * @see EditionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CardsApp.class, SecurityBeanOverrideConfiguration.class})
public class EditionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAUNCH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAUNCH_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private EditionRepository editionRepository;

    @Autowired
    private EditionMapper editionMapper;

    @Autowired
    private EditionService editionService;

    @Autowired
    private EditionSearchRepository editionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEditionMockMvc;

    private Edition edition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EditionResource editionResource = new EditionResource(editionService);
        this.restEditionMockMvc = MockMvcBuilders.standaloneSetup(editionResource)
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
    public static Edition createEntity(EntityManager em) {
        Edition edition = new Edition()
                .name(DEFAULT_NAME)
                .launchDate(DEFAULT_LAUNCH_DATE);
        return edition;
    }

    @Before
    public void initTest() {
        editionSearchRepository.deleteAll();
        edition = createEntity(em);
    }

    @Test
    @Transactional
    public void createEdition() throws Exception {
        int databaseSizeBeforeCreate = editionRepository.findAll().size();

        // Create the Edition
        EditionDTO editionDTO = editionMapper.editionToEditionDTO(edition);

        restEditionMockMvc.perform(post("/api/editions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(editionDTO)))
            .andExpect(status().isCreated());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeCreate + 1);
        Edition testEdition = editionList.get(editionList.size() - 1);
        assertThat(testEdition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEdition.getLaunchDate()).isEqualTo(DEFAULT_LAUNCH_DATE);

        // Validate the Edition in Elasticsearch
        Edition editionEs = editionSearchRepository.findOne(testEdition.getId());
        assertThat(editionEs).isEqualToComparingFieldByField(testEdition);
    }

    @Test
    @Transactional
    public void createEditionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = editionRepository.findAll().size();

        // Create the Edition with an existing ID
        Edition existingEdition = new Edition();
        existingEdition.setId(1L);
        EditionDTO existingEditionDTO = editionMapper.editionToEditionDTO(existingEdition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEditionMockMvc.perform(post("/api/editions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEditionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = editionRepository.findAll().size();
        // set the field null
        edition.setName(null);

        // Create the Edition, which fails.
        EditionDTO editionDTO = editionMapper.editionToEditionDTO(edition);

        restEditionMockMvc.perform(post("/api/editions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(editionDTO)))
            .andExpect(status().isBadRequest());

        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLaunchDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = editionRepository.findAll().size();
        // set the field null
        edition.setLaunchDate(null);

        // Create the Edition, which fails.
        EditionDTO editionDTO = editionMapper.editionToEditionDTO(edition);

        restEditionMockMvc.perform(post("/api/editions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(editionDTO)))
            .andExpect(status().isBadRequest());

        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEditions() throws Exception {
        // Initialize the database
        editionRepository.saveAndFlush(edition);

        // Get all the editionList
        restEditionMockMvc.perform(get("/api/editions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].launchDate").value(hasItem(DEFAULT_LAUNCH_DATE.toString())));
    }

    @Test
    @Transactional
    public void getEdition() throws Exception {
        // Initialize the database
        editionRepository.saveAndFlush(edition);

        // Get the edition
        restEditionMockMvc.perform(get("/api/editions/{id}", edition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(edition.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.launchDate").value(DEFAULT_LAUNCH_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEdition() throws Exception {
        // Get the edition
        restEditionMockMvc.perform(get("/api/editions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEdition() throws Exception {
        // Initialize the database
        editionRepository.saveAndFlush(edition);
        editionSearchRepository.save(edition);
        int databaseSizeBeforeUpdate = editionRepository.findAll().size();

        // Update the edition
        Edition updatedEdition = editionRepository.findOne(edition.getId());
        updatedEdition
                .name(UPDATED_NAME)
                .launchDate(UPDATED_LAUNCH_DATE);
        EditionDTO editionDTO = editionMapper.editionToEditionDTO(updatedEdition);

        restEditionMockMvc.perform(put("/api/editions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(editionDTO)))
            .andExpect(status().isOk());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeUpdate);
        Edition testEdition = editionList.get(editionList.size() - 1);
        assertThat(testEdition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEdition.getLaunchDate()).isEqualTo(UPDATED_LAUNCH_DATE);

        // Validate the Edition in Elasticsearch
        Edition editionEs = editionSearchRepository.findOne(testEdition.getId());
        assertThat(editionEs).isEqualToComparingFieldByField(testEdition);
    }

    @Test
    @Transactional
    public void updateNonExistingEdition() throws Exception {
        int databaseSizeBeforeUpdate = editionRepository.findAll().size();

        // Create the Edition
        EditionDTO editionDTO = editionMapper.editionToEditionDTO(edition);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEditionMockMvc.perform(put("/api/editions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(editionDTO)))
            .andExpect(status().isCreated());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEdition() throws Exception {
        // Initialize the database
        editionRepository.saveAndFlush(edition);
        editionSearchRepository.save(edition);
        int databaseSizeBeforeDelete = editionRepository.findAll().size();

        // Get the edition
        restEditionMockMvc.perform(delete("/api/editions/{id}", edition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean editionExistsInEs = editionSearchRepository.exists(edition.getId());
        assertThat(editionExistsInEs).isFalse();

        // Validate the database is empty
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEdition() throws Exception {
        // Initialize the database
        editionRepository.saveAndFlush(edition);
        editionSearchRepository.save(edition);

        // Search the edition
        restEditionMockMvc.perform(get("/api/_search/editions?query=id:" + edition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].launchDate").value(hasItem(DEFAULT_LAUNCH_DATE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Edition.class);
    }
}
