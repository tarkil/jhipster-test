package com.github.tarkil.service.impl;

import com.github.tarkil.service.CardTypeService;
import com.github.tarkil.domain.CardType;
import com.github.tarkil.repository.CardTypeRepository;
import com.github.tarkil.repository.search.CardTypeSearchRepository;
import com.github.tarkil.service.dto.CardTypeDTO;
import com.github.tarkil.service.mapper.CardTypeMapper;
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
 * Service Implementation for managing CardType.
 */
@Service
@Transactional
public class CardTypeServiceImpl implements CardTypeService{

    private final Logger log = LoggerFactory.getLogger(CardTypeServiceImpl.class);
    
    private final CardTypeRepository cardTypeRepository;

    private final CardTypeMapper cardTypeMapper;

    private final CardTypeSearchRepository cardTypeSearchRepository;

    public CardTypeServiceImpl(CardTypeRepository cardTypeRepository, CardTypeMapper cardTypeMapper, CardTypeSearchRepository cardTypeSearchRepository) {
        this.cardTypeRepository = cardTypeRepository;
        this.cardTypeMapper = cardTypeMapper;
        this.cardTypeSearchRepository = cardTypeSearchRepository;
    }

    /**
     * Save a cardType.
     *
     * @param cardTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CardTypeDTO save(CardTypeDTO cardTypeDTO) {
        log.debug("Request to save CardType : {}", cardTypeDTO);
        CardType cardType = cardTypeMapper.cardTypeDTOToCardType(cardTypeDTO);
        cardType = cardTypeRepository.save(cardType);
        CardTypeDTO result = cardTypeMapper.cardTypeToCardTypeDTO(cardType);
        cardTypeSearchRepository.save(cardType);
        return result;
    }

    /**
     *  Get all the cardTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CardTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CardTypes");
        Page<CardType> result = cardTypeRepository.findAll(pageable);
        return result.map(cardType -> cardTypeMapper.cardTypeToCardTypeDTO(cardType));
    }

    /**
     *  Get one cardType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CardTypeDTO findOne(Long id) {
        log.debug("Request to get CardType : {}", id);
        CardType cardType = cardTypeRepository.findOne(id);
        CardTypeDTO cardTypeDTO = cardTypeMapper.cardTypeToCardTypeDTO(cardType);
        return cardTypeDTO;
    }

    /**
     *  Delete the  cardType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardType : {}", id);
        cardTypeRepository.delete(id);
        cardTypeSearchRepository.delete(id);
    }

    /**
     * Search for the cardType corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CardTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CardTypes for query {}", query);
        Page<CardType> result = cardTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(cardType -> cardTypeMapper.cardTypeToCardTypeDTO(cardType));
    }
}
