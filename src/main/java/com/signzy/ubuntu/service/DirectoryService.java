package com.signzy.ubuntu.service;

import com.signzy.ubuntu.service.dto.DirectoryDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Directory.
 */
public interface DirectoryService {

    /**
     * Save a directory.
     *
     * @param directoryDTO the entity to save
     * @return the persisted entity
     */
    DirectoryDTO save(DirectoryDTO directoryDTO);

    /**
     * Get all the directories.
     *
     * @return the list of entities
     */
    List<DirectoryDTO> findAll();


    /**
     * Get the "id" directory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DirectoryDTO> findOne(Long id);

    /**
     * Delete the "id" directory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
