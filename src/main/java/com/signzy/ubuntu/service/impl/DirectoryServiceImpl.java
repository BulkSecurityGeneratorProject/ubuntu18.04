package com.signzy.ubuntu.service.impl;

import com.signzy.ubuntu.service.DirectoryService;
import com.signzy.ubuntu.domain.Directory;
import com.signzy.ubuntu.repository.DirectoryRepository;
import com.signzy.ubuntu.service.dto.DirectoryDTO;
import com.signzy.ubuntu.service.mapper.DirectoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Directory.
 */
@Service
@Transactional
public class DirectoryServiceImpl implements DirectoryService {

    private final Logger log = LoggerFactory.getLogger(DirectoryServiceImpl.class);

    private final DirectoryRepository directoryRepository;

    private final DirectoryMapper directoryMapper;

    public DirectoryServiceImpl(DirectoryRepository directoryRepository, DirectoryMapper directoryMapper) {
        this.directoryRepository = directoryRepository;
        this.directoryMapper = directoryMapper;
    }

    /**
     * Save a directory.
     *
     * @param directoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DirectoryDTO save(DirectoryDTO directoryDTO) {
        log.debug("Request to save Directory : {}", directoryDTO);
        Directory directory = directoryMapper.toEntity(directoryDTO);
        directory.setTimeStamp(Instant.now());
        directory.setParent("Desktop");
        directory.setType("Directory");
        directory.setIsDirectory(true);
        directory = directoryRepository.save(directory);
        return directoryMapper.toDto(directory);
    }

    /**
     * Get all the directories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<DirectoryDTO> findAll() {
        log.debug("Request to get all Directories");
        return directoryRepository.findAll().stream()
            .map(directoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one directory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DirectoryDTO> findOne(Long id) {
        log.debug("Request to get Directory : {}", id);
        return directoryRepository.findById(id)
            .map(directoryMapper::toDto);
    }

    /**
     * Delete the directory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Directory : {}", id);
        directoryRepository.deleteById(id);
    }
}
