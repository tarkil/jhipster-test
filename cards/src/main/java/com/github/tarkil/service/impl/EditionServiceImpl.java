package com.github.tarkil.service.impl;

import com.github.tarkil.service.EditionService;
import com.github.tarkil.domain.Edition;
import com.github.tarkil.repository.EditionRepository;
import com.github.tarkil.repository.search.EditionSearchRepository;
import com.github.tarkil.service.dto.EditionDTO;
import com.github.tarkil.service.mapper.EditionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Edition.
 */
@Service
@Transactional
public class EditionServiceImpl implements EditionService{

    private final Logger log = LoggerFactory.getLogger(EditionServiceImpl.class);
    
    private final EditionRepository editionRepository;

    private final EditionMapper editionMapper;

    private final EditionSearchRepository editionSearchRepository;

    public EditionServiceImpl(EditionRepository editionRepository, EditionMapper editionMapper, EditionSearchRepository editionSearchRepository) {
        this.editionRepository = editionRepository;
        this.editionMapper = editionMapper;
        this.editionSearchRepository = editionSearchRepository;
    }

    /**
     * Save a edition.
     *
     * @param editionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EditionDTO save(EditionDTO editionDTO) {
        log.debug("Request to save Edition : {}", editionDTO);
        Edition edition = editionMapper.editionDTOToEdition(editionDTO);
        edition = editionRepository.save(edition);
        EditionDTO result = editionMapper.editionToEditionDTO(edition);
        editionSearchRepository.save(edition);
        return result;
    }

    /**
     *  Get all the editions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EditionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Editions");
        Page<Edition> result = editionRepository.findAll(pageable);
        return result.map(edition -> editionMapper.editionToEditionDTO(edition));
    }

    /**
     *  Get one edition by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EditionDTO findOne(Long id) {
        log.debug("Request to get Edition : {}", id);
        Edition edition = editionRepository.findOneWithEagerRelationships(id);
        EditionDTO editionDTO = editionMapper.editionToEditionDTO(edition);
        return editionDTO;
    }

    /**
     *  Delete the  edition by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Edition : {}", id);
        editionRepository.delete(id);
        editionSearchRepository.delete(id);
    }

    /**
     * Search for the edition corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EditionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Editions for query {}", query);
        Page<Edition> result = editionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(edition -> editionMapper.editionToEditionDTO(edition));
    }
}
