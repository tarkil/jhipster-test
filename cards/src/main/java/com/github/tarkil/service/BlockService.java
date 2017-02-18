package com.github.tarkil.service;

import com.github.tarkil.service.dto.BlockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Block.
 */
public interface BlockService {

    /**
     * Save a block.
     *
     * @param blockDTO the entity to save
     * @return the persisted entity
     */
    BlockDTO save(BlockDTO blockDTO);

    /**
     *  Get all the blocks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BlockDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" block.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BlockDTO findOne(Long id);

    /**
     *  Delete the "id" block.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the block corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BlockDTO> search(String query, Pageable pageable);
}
