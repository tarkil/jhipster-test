package com.github.tarkil.service;

import com.github.tarkil.service.dto.ReprintDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Reprint.
 */
public interface ReprintService {

    /**
     * Save a reprint.
     *
     * @param reprintDTO the entity to save
     * @return the persisted entity
     */
    ReprintDTO save(ReprintDTO reprintDTO);

    /**
     *  Get all the reprints.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ReprintDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" reprint.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ReprintDTO findOne(Long id);

    /**
     *  Delete the "id" reprint.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the reprint corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ReprintDTO> search(String query, Pageable pageable);
}
