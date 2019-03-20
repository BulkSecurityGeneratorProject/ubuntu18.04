package com.signzy.ubuntu.web.rest;
import com.signzy.ubuntu.service.DirectoryService;
import com.signzy.ubuntu.web.rest.errors.BadRequestAlertException;
import com.signzy.ubuntu.web.rest.util.HeaderUtil;
import com.signzy.ubuntu.service.dto.DirectoryDTO;
import com.signzy.ubuntu.service.dto.DirectoryCriteria;
import com.signzy.ubuntu.service.DirectoryQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Directory.
 */
@RestController
@RequestMapping("/api")
public class DirectoryResource {

    private final Logger log = LoggerFactory.getLogger(DirectoryResource.class);

    private static final String ENTITY_NAME = "directory";

    private final DirectoryService directoryService;

    private final DirectoryQueryService directoryQueryService;

    public DirectoryResource(DirectoryService directoryService, DirectoryQueryService directoryQueryService) {
        this.directoryService = directoryService;
        this.directoryQueryService = directoryQueryService;
    }

    /**
     * POST  /directories : Create a new directory.
     *
     * @param directoryDTO the directoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new directoryDTO, or with status 400 (Bad Request) if the directory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/directories")
    public ResponseEntity<DirectoryDTO> createDirectory(@RequestBody DirectoryDTO directoryDTO) throws URISyntaxException {
        log.debug("REST request to save Directory : {}", directoryDTO);
        try {
            String command = null;
            if(directoryDTO.getParent().equals("Desktop")){
                command = "mkdir Desktop/"+directoryDTO.getName();
            }else{
                command = "";
            }
            Process p = Runtime.getRuntime().exec(command);
            new BufferedReader(new InputStreamReader(p.getInputStream()));
            new BufferedReader(new InputStreamReader(p.getErrorStream()));
        }
        catch (IOException e) {
            System.out.println(e);
        }

        if (directoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new directory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DirectoryDTO result = directoryService.save(directoryDTO);
        return ResponseEntity.created(new URI("/api/directories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /directories : Updates an existing directory.
     *
     * @param directoryDTO the directoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated directoryDTO,
     * or with status 400 (Bad Request) if the directoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the directoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/directories")
    public ResponseEntity<DirectoryDTO> updateDirectory(@RequestBody DirectoryDTO directoryDTO) throws URISyntaxException {
        log.debug("REST request to update Directory : {}", directoryDTO);
        if (directoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DirectoryDTO result = directoryService.save(directoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, directoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /directories : get all the directories.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of directories in body
     */
    @GetMapping("/directories")
    public ResponseEntity<List<DirectoryDTO>> getAllDirectories(DirectoryCriteria criteria) {
        log.debug("REST request to get Directories by criteria: {}", criteria);
        List<DirectoryDTO> entityList = directoryQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /directories/count : count all the directories.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/directories/count")
    public ResponseEntity<Long> countDirectories(DirectoryCriteria criteria) {
        log.debug("REST request to count Directories by criteria: {}", criteria);
        return ResponseEntity.ok().body(directoryQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /directories/:id : get the "id" directory.
     *
     * @param id the id of the directoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the directoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/directories/{id}")
    public ResponseEntity<DirectoryDTO> getDirectory(@PathVariable Long id) {
        log.debug("REST request to get Directory : {}", id);
        Optional<DirectoryDTO> directoryDTO = directoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(directoryDTO);
    }

    /**
     * DELETE  /directories/:id : delete the "id" directory.
     *
     * @param id the id of the directoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/directories/{id}")
    public ResponseEntity<Void> deleteDirectory(@PathVariable Long id) {
        log.debug("REST request to delete Directory : {}", id);
        directoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
