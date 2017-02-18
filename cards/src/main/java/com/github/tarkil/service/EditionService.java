package com.github.tarkil.service;

import com.github.tarkil.service.dto.EditionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Edition.
 */
public interface EditionService {

    /**
     * Save a edition.
     *
     * @param editionDTO the entity to save
     * @return the persisted entity
     */
    EditionDTO save(EditionDTO editionDTO);

    /**
     *  Get all the editions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EditionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" edition.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EditionDTO findOne(Long id);

    /**
     *  Delete the "id" edition.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the edition corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EditionDTO> search(String query, Pageable pageable);
}
