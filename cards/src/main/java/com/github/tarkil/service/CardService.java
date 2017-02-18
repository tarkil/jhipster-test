package com.github.tarkil.service;

import com.github.tarkil.service.dto.CardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Card.
 */
public interface CardService {

    /**
     * Save a card.
     *
     * @param cardDTO the entity to save
     * @return the persisted entity
     */
    CardDTO save(CardDTO cardDTO);

    /**
     *  Get all the cards.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CardDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" card.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CardDTO findOne(Long id);

    /**
     *  Delete the "id" card.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the card corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CardDTO> search(String query, Pageable pageable);
}
