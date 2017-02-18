package com.github.tarkil.service.impl;

import com.github.tarkil.service.ReprintService;
import com.github.tarkil.domain.Reprint;
import com.github.tarkil.repository.ReprintRepository;
import com.github.tarkil.repository.search.ReprintSearchRepository;
import com.github.tarkil.service.dto.ReprintDTO;
import com.github.tarkil.service.mapper.ReprintMapper;
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
 * Service Implementation for managing Reprint.
 */
@Service
@Transactional
public class ReprintServiceImpl implements ReprintService{

    private final Logger log = LoggerFactory.getLogger(ReprintServiceImpl.class);
    
    private final ReprintRepository reprintRepository;

    private final ReprintMapper reprintMapper;

    private final ReprintSearchRepository reprintSearchRepository;

    public ReprintServiceImpl(ReprintRepository reprintRepository, ReprintMapper reprintMapper, ReprintSearchRepository reprintSearchRepository) {
        this.reprintRepository = reprintRepository;
        this.reprintMapper = reprintMapper;
        this.reprintSearchRepository = reprintSearchRepository;
    }

    /**
     * Save a reprint.
     *
     * @param reprintDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReprintDTO save(ReprintDTO reprintDTO) {
        log.debug("Request to save Reprint : {}", reprintDTO);
        Reprint reprint = reprintMapper.reprintDTOToReprint(reprintDTO);
        reprint = reprintRepository.save(reprint);
        ReprintDTO result = reprintMapper.reprintToReprintDTO(reprint);
        reprintSearchRepository.save(reprint);
        return result;
    }

    /**
     *  Get all the reprints.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReprintDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reprints");
        Page<Reprint> result = reprintRepository.findAll(pageable);
        return result.map(reprint -> reprintMapper.reprintToReprintDTO(reprint));
    }

    /**
     *  Get one reprint by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ReprintDTO findOne(Long id) {
        log.debug("Request to get Reprint : {}", id);
        Reprint reprint = reprintRepository.findOne(id);
        ReprintDTO reprintDTO = reprintMapper.reprintToReprintDTO(reprint);
        return reprintDTO;
    }

    /**
     *  Delete the  reprint by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reprint : {}", id);
        reprintRepository.delete(id);
        reprintSearchRepository.delete(id);
    }

    /**
     * Search for the reprint corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReprintDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Reprints for query {}", query);
        Page<Reprint> result = reprintSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(reprint -> reprintMapper.reprintToReprintDTO(reprint));
    }
}
