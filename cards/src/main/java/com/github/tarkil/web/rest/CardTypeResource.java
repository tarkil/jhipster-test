package com.github.tarkil.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.github.tarkil.service.CardTypeService;
import com.github.tarkil.web.rest.util.HeaderUtil;
import com.github.tarkil.web.rest.util.PaginationUtil;
import com.github.tarkil.service.dto.CardTypeDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CardType.
 */
@RestController
@RequestMapping("/api")
public class CardTypeResource {

    private final Logger log = LoggerFactory.getLogger(CardTypeResource.class);

    private static final String ENTITY_NAME = "cardType";
        
    private final CardTypeService cardTypeService;

    public CardTypeResource(CardTypeService cardTypeService) {
        this.cardTypeService = cardTypeService;
    }

    /**
     * POST  /card-types : Create a new cardType.
     *
     * @param cardTypeDTO the cardTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cardTypeDTO, or with status 400 (Bad Request) if the cardType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/card-types")
    @Timed
    public ResponseEntity<CardTypeDTO> createCardType(@Valid @RequestBody CardTypeDTO cardTypeDTO) throws URISyntaxException {
        log.debug("REST request to save CardType : {}", cardTypeDTO);
        if (cardTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cardType cannot already have an ID")).body(null);
        }
        CardTypeDTO result = cardTypeService.save(cardTypeDTO);
        return ResponseEntity.created(new URI("/api/card-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /card-types : Updates an existing cardType.
     *
     * @param cardTypeDTO the cardTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cardTypeDTO,
     * or with status 400 (Bad Request) if the cardTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the cardTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/card-types")
    @Timed
    public ResponseEntity<CardTypeDTO> updateCardType(@Valid @RequestBody CardTypeDTO cardTypeDTO) throws URISyntaxException {
        log.debug("REST request to update CardType : {}", cardTypeDTO);
        if (cardTypeDTO.getId() == null) {
            return createCardType(cardTypeDTO);
        }
        CardTypeDTO result = cardTypeService.save(cardTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cardTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /card-types : get all the cardTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cardTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/card-types")
    @Timed
    public ResponseEntity<List<CardTypeDTO>> getAllCardTypes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CardTypes");
        Page<CardTypeDTO> page = cardTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/card-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /card-types/:id : get the "id" cardType.
     *
     * @param id the id of the cardTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cardTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/card-types/{id}")
    @Timed
    public ResponseEntity<CardTypeDTO> getCardType(@PathVariable Long id) {
        log.debug("REST request to get CardType : {}", id);
        CardTypeDTO cardTypeDTO = cardTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cardTypeDTO));
    }

    /**
     * DELETE  /card-types/:id : delete the "id" cardType.
     *
     * @param id the id of the cardTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/card-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteCardType(@PathVariable Long id) {
        log.debug("REST request to delete CardType : {}", id);
        cardTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/card-types?query=:query : search for the cardType corresponding
     * to the query.
     *
     * @param query the query of the cardType search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/card-types")
    @Timed
    public ResponseEntity<List<CardTypeDTO>> searchCardTypes(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CardTypes for query {}", query);
        Page<CardTypeDTO> page = cardTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/card-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
