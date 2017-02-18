package com.github.tarkil.web.rest;

import com.github.tarkil.CardsApp;

import com.github.tarkil.config.SecurityBeanOverrideConfiguration;

import com.github.tarkil.domain.Reprint;
import com.github.tarkil.repository.ReprintRepository;
import com.github.tarkil.service.ReprintService;
import com.github.tarkil.repository.search.ReprintSearchRepository;
import com.github.tarkil.service.dto.ReprintDTO;
import com.github.tarkil.service.mapper.ReprintMapper;
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
 * Test class for the ReprintResource REST controller.
 *
 * @see ReprintResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CardsApp.class, SecurityBeanOverrideConfiguration.class})
public class ReprintResourceIntTest {

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ARTIST = "AAAAAAAAAA";
    private static final String UPDATED_ARTIST = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Autowired
    private ReprintRepository reprintRepository;

    @Autowired
    private ReprintMapper reprintMapper;

    @Autowired
    private ReprintService reprintService;

    @Autowired
    private ReprintSearchRepository reprintSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReprintMockMvc;

    private Reprint reprint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReprintResource reprintResource = new ReprintResource(reprintService);
        this.restReprintMockMvc = MockMvcBuilders.standaloneSetup(reprintResource)
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
    public static Reprint createEntity(EntityManager em) {
        Reprint reprint = new Reprint()
                .image(DEFAULT_IMAGE)
                .artist(DEFAULT_ARTIST)
                .number(DEFAULT_NUMBER);
        return reprint;
    }

    @Before
    public void initTest() {
        reprintSearchRepository.deleteAll();
        reprint = createEntity(em);
    }

    @Test
    @Transactional
    public void createReprint() throws Exception {
        int databaseSizeBeforeCreate = reprintRepository.findAll().size();

        // Create the Reprint
        ReprintDTO reprintDTO = reprintMapper.reprintToReprintDTO(reprint);

        restReprintMockMvc.perform(post("/api/reprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reprintDTO)))
            .andExpect(status().isCreated());

        // Validate the Reprint in the database
        List<Reprint> reprintList = reprintRepository.findAll();
        assertThat(reprintList).hasSize(databaseSizeBeforeCreate + 1);
        Reprint testReprint = reprintList.get(reprintList.size() - 1);
        assertThat(testReprint.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testReprint.getArtist()).isEqualTo(DEFAULT_ARTIST);
        assertThat(testReprint.getNumber()).isEqualTo(DEFAULT_NUMBER);

        // Validate the Reprint in Elasticsearch
        Reprint reprintEs = reprintSearchRepository.findOne(testReprint.getId());
        assertThat(reprintEs).isEqualToComparingFieldByField(testReprint);
    }

    @Test
    @Transactional
    public void createReprintWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reprintRepository.findAll().size();

        // Create the Reprint with an existing ID
        Reprint existingReprint = new Reprint();
        existingReprint.setId(1L);
        ReprintDTO existingReprintDTO = reprintMapper.reprintToReprintDTO(existingReprint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReprintMockMvc.perform(post("/api/reprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingReprintDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Reprint> reprintList = reprintRepository.findAll();
        assertThat(reprintList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReprints() throws Exception {
        // Initialize the database
        reprintRepository.saveAndFlush(reprint);

        // Get all the reprintList
        restReprintMockMvc.perform(get("/api/reprints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].artist").value(hasItem(DEFAULT_ARTIST.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void getReprint() throws Exception {
        // Initialize the database
        reprintRepository.saveAndFlush(reprint);

        // Get the reprint
        restReprintMockMvc.perform(get("/api/reprints/{id}", reprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reprint.getId().intValue()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.artist").value(DEFAULT_ARTIST.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingReprint() throws Exception {
        // Get the reprint
        restReprintMockMvc.perform(get("/api/reprints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReprint() throws Exception {
        // Initialize the database
        reprintRepository.saveAndFlush(reprint);
        reprintSearchRepository.save(reprint);
        int databaseSizeBeforeUpdate = reprintRepository.findAll().size();

        // Update the reprint
        Reprint updatedReprint = reprintRepository.findOne(reprint.getId());
        updatedReprint
                .image(UPDATED_IMAGE)
                .artist(UPDATED_ARTIST)
                .number(UPDATED_NUMBER);
        ReprintDTO reprintDTO = reprintMapper.reprintToReprintDTO(updatedReprint);

        restReprintMockMvc.perform(put("/api/reprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reprintDTO)))
            .andExpect(status().isOk());

        // Validate the Reprint in the database
        List<Reprint> reprintList = reprintRepository.findAll();
        assertThat(reprintList).hasSize(databaseSizeBeforeUpdate);
        Reprint testReprint = reprintList.get(reprintList.size() - 1);
        assertThat(testReprint.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testReprint.getArtist()).isEqualTo(UPDATED_ARTIST);
        assertThat(testReprint.getNumber()).isEqualTo(UPDATED_NUMBER);

        // Validate the Reprint in Elasticsearch
        Reprint reprintEs = reprintSearchRepository.findOne(testReprint.getId());
        assertThat(reprintEs).isEqualToComparingFieldByField(testReprint);
    }

    @Test
    @Transactional
    public void updateNonExistingReprint() throws Exception {
        int databaseSizeBeforeUpdate = reprintRepository.findAll().size();

        // Create the Reprint
        ReprintDTO reprintDTO = reprintMapper.reprintToReprintDTO(reprint);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReprintMockMvc.perform(put("/api/reprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reprintDTO)))
            .andExpect(status().isCreated());

        // Validate the Reprint in the database
        List<Reprint> reprintList = reprintRepository.findAll();
        assertThat(reprintList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReprint() throws Exception {
        // Initialize the database
        reprintRepository.saveAndFlush(reprint);
        reprintSearchRepository.save(reprint);
        int databaseSizeBeforeDelete = reprintRepository.findAll().size();

        // Get the reprint
        restReprintMockMvc.perform(delete("/api/reprints/{id}", reprint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reprintExistsInEs = reprintSearchRepository.exists(reprint.getId());
        assertThat(reprintExistsInEs).isFalse();

        // Validate the database is empty
        List<Reprint> reprintList = reprintRepository.findAll();
        assertThat(reprintList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReprint() throws Exception {
        // Initialize the database
        reprintRepository.saveAndFlush(reprint);
        reprintSearchRepository.save(reprint);

        // Search the reprint
        restReprintMockMvc.perform(get("/api/_search/reprints?query=id:" + reprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].artist").value(hasItem(DEFAULT_ARTIST.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reprint.class);
    }
}
