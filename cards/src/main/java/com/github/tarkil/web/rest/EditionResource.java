package com.github.tarkil.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.github.tarkil.service.EditionService;
import com.github.tarkil.web.rest.util.HeaderUtil;
import com.github.tarkil.web.rest.util.PaginationUtil;
import com.github.tarkil.service.dto.EditionDTO;
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
 * REST controller for managing Edition.
 */
@RestController
@RequestMapping("/api")
public class EditionResource {

    private final Logger log = LoggerFactory.getLogger(EditionResource.class);

    private static final String ENTITY_NAME = "edition";
        
    private final EditionService editionService;

    public EditionResource(EditionService editionService) {
        this.editionService = editionService;
    }

    /**
     * POST  /editions : Create a new edition.
     *
     * @param editionDTO the editionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new editionDTO, or with status 400 (Bad Request) if the edition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/editions")
    @Timed
    public ResponseEntity<EditionDTO> createEdition(@Valid @RequestBody EditionDTO editionDTO) throws URISyntaxException {
        log.debug("REST request to save Edition : {}", editionDTO);
        if (editionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new edition cannot already have an ID")).body(null);
        }
        EditionDTO result = editionService.save(editionDTO);
        return ResponseEntity.created(new URI("/api/editions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /editions : Updates an existing edition.
     *
     * @param editionDTO the editionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated editionDTO,
     * or with status 400 (Bad Request) if the editionDTO is not valid,
     * or with status 500 (Internal Server Error) if the editionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/editions")
    @Timed
    public ResponseEntity<EditionDTO> updateEdition(@Valid @RequestBody EditionDTO editionDTO) throws URISyntaxException {
        log.debug("REST request to update Edition : {}", editionDTO);
        if (editionDTO.getId() == null) {
            return createEdition(editionDTO);
        }
        EditionDTO result = editionService.save(editionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, editionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /editions : get all the editions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of editions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/editions")
    @Timed
    public ResponseEntity<List<EditionDTO>> getAllEditions(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Editions");
        Page<EditionDTO> page = editionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/editions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /editions/:id : get the "id" edition.
     *
     * @param id the id of the editionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the editionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/editions/{id}")
    @Timed
    public ResponseEntity<EditionDTO> getEdition(@PathVariable Long id) {
        log.debug("REST request to get Edition : {}", id);
        EditionDTO editionDTO = editionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(editionDTO));
    }

    /**
     * DELETE  /editions/:id : delete the "id" edition.
     *
     * @param id the id of the editionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/editions/{id}")
    @Timed
    public ResponseEntity<Void> deleteEdition(@PathVariable Long id) {
        log.debug("REST request to delete Edition : {}", id);
        editionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/editions?query=:query : search for the edition corresponding
     * to the query.
     *
     * @param query the query of the edition search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/editions")
    @Timed
    public ResponseEntity<List<EditionDTO>> searchEditions(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Editions for query {}", query);
        Page<EditionDTO> page = editionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/editions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
