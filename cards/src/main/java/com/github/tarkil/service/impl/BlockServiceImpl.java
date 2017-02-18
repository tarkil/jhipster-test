package com.github.tarkil.service.impl;

import com.github.tarkil.service.BlockService;
import com.github.tarkil.domain.Block;
import com.github.tarkil.repository.BlockRepository;
import com.github.tarkil.repository.search.BlockSearchRepository;
import com.github.tarkil.service.dto.BlockDTO;
import com.github.tarkil.service.mapper.BlockMapper;
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
 * Service Implementation for managing Block.
 */
@Service
@Transactional
public class BlockServiceImpl implements BlockService{

    private final Logger log = LoggerFactory.getLogger(BlockServiceImpl.class);
    
    private final BlockRepository blockRepository;

    private final BlockMapper blockMapper;

    private final BlockSearchRepository blockSearchRepository;

    public BlockServiceImpl(BlockRepository blockRepository, BlockMapper blockMapper, BlockSearchRepository blockSearchRepository) {
        this.blockRepository = blockRepository;
        this.blockMapper = blockMapper;
        this.blockSearchRepository = blockSearchRepository;
    }

    /**
     * Save a block.
     *
     * @param blockDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BlockDTO save(BlockDTO blockDTO) {
        log.debug("Request to save Block : {}", blockDTO);
        Block block = blockMapper.blockDTOToBlock(blockDTO);
        block = blockRepository.save(block);
        BlockDTO result = blockMapper.blockToBlockDTO(block);
        blockSearchRepository.save(block);
        return result;
    }

    /**
     *  Get all the blocks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BlockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Blocks");
        Page<Block> result = blockRepository.findAll(pageable);
        return result.map(block -> blockMapper.blockToBlockDTO(block));
    }

    /**
     *  Get one block by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BlockDTO findOne(Long id) {
        log.debug("Request to get Block : {}", id);
        Block block = blockRepository.findOne(id);
        BlockDTO blockDTO = blockMapper.blockToBlockDTO(block);
        return blockDTO;
    }

    /**
     *  Delete the  block by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Block : {}", id);
        blockRepository.delete(id);
        blockSearchRepository.delete(id);
    }

    /**
     * Search for the block corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BlockDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Blocks for query {}", query);
        Page<Block> result = blockSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(block -> blockMapper.blockToBlockDTO(block));
    }
}
