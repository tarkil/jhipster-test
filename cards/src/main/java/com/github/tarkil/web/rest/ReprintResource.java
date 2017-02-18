package com.github.tarkil.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.github.tarkil.service.ReprintService;
import com.github.tarkil.web.rest.util.HeaderUtil;
import com.github.tarkil.web.rest.util.PaginationUtil;
import com.github.tarkil.service.dto.ReprintDTO;
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
 * REST controller for managing Reprint.
 */
@RestController
@RequestMapping("/api")
public class ReprintResource {

    private final Logger log = LoggerFactory.getLogger(ReprintResource.class);

    private static final String ENTITY_NAME = "reprint";
        
    private final ReprintService reprintService;

    public ReprintResource(ReprintService reprintService) {
        this.reprintService = reprintService;
    }

    /**
     * POST  /reprints : Create a new reprint.
     *
     * @param reprintDTO the reprintDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reprintDTO, or with status 400 (Bad Request) if the reprint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reprints")
    @Timed
    public ResponseEntity<ReprintDTO> createReprint(@Valid @RequestBody ReprintDTO reprintDTO) throws URISyntaxException {
        log.debug("REST request to save Reprint : {}", reprintDTO);
        if (reprintDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reprint cannot already have an ID")).body(null);
        }
        ReprintDTO result = reprintService.save(reprintDTO);
        return ResponseEntity.created(new URI("/api/reprints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reprints : Updates an existing reprint.
     *
     * @param reprintDTO the reprintDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reprintDTO,
     * or with status 400 (Bad Request) if the reprintDTO is not valid,
     * or with status 500 (Internal Server Error) if the reprintDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reprints")
    @Timed
    public ResponseEntity<ReprintDTO> updateReprint(@Valid @RequestBody ReprintDTO reprintDTO) throws URISyntaxException {
        log.debug("REST request to update Reprint : {}", reprintDTO);
        if (reprintDTO.getId() == null) {
            return createReprint(reprintDTO);
        }
        ReprintDTO result = reprintService.save(reprintDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reprintDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reprints : get all the reprints.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reprints in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/reprints")
    @Timed
    public ResponseEntity<List<ReprintDTO>> getAllReprints(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Reprints");
        Page<ReprintDTO> page = reprintService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reprints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reprints/:id : get the "id" reprint.
     *
     * @param id the id of the reprintDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reprintDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reprints/{id}")
    @Timed
    public ResponseEntity<ReprintDTO> getReprint(@PathVariable Long id) {
        log.debug("REST request to get Reprint : {}", id);
        ReprintDTO reprintDTO = reprintService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reprintDTO));
    }

    /**
     * DELETE  /reprints/:id : delete the "id" reprint.
     *
     * @param id the id of the reprintDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reprints/{id}")
    @Timed
    public ResponseEntity<Void> deleteReprint(@PathVariable Long id) {
        log.debug("REST request to delete Reprint : {}", id);
        reprintService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reprints?query=:query : search for the reprint corresponding
     * to the query.
     *
     * @param query the query of the reprint search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/reprints")
    @Timed
    public ResponseEntity<List<ReprintDTO>> searchReprints(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Reprints for query {}", query);
        Page<ReprintDTO> page = reprintService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reprints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
