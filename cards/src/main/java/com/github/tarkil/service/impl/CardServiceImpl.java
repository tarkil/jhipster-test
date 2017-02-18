package com.github.tarkil.service.impl;

import com.github.tarkil.service.CardService;
import com.github.tarkil.domain.Card;
import com.github.tarkil.repository.CardRepository;
import com.github.tarkil.repository.search.CardSearchRepository;
import com.github.tarkil.service.dto.CardDTO;
import com.github.tarkil.service.mapper.CardMapper;
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
 * Service Implementation for managing Card.
 */
@Service
@Transactional
public class CardServiceImpl implements CardService{

    private final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);
    
    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    private final CardSearchRepository cardSearchRepository;

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper, CardSearchRepository cardSearchRepository) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.cardSearchRepository = cardSearchRepository;
    }

    /**
     * Save a card.
     *
     * @param cardDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CardDTO save(CardDTO cardDTO) {
        log.debug("Request to save Card : {}", cardDTO);
        Card card = cardMapper.cardDTOToCard(cardDTO);
        card = cardRepository.save(card);
        CardDTO result = cardMapper.cardToCardDTO(card);
        cardSearchRepository.save(card);
        return result;
    }

    /**
     *  Get all the cards.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cards");
        Page<Card> result = cardRepository.findAll(pageable);
        return result.map(card -> cardMapper.cardToCardDTO(card));
    }

    /**
     *  Get one card by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CardDTO findOne(Long id) {
        log.debug("Request to get Card : {}", id);
        Card card = cardRepository.findOne(id);
        CardDTO cardDTO = cardMapper.cardToCardDTO(card);
        return cardDTO;
    }

    /**
     *  Delete the  card by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Card : {}", id);
        cardRepository.delete(id);
        cardSearchRepository.delete(id);
    }

    /**
     * Search for the card corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CardDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cards for query {}", query);
        Page<Card> result = cardSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(card -> cardMapper.cardToCardDTO(card));
    }
}
