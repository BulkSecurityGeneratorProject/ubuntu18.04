package com.signzy.ubuntu.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.signzy.ubuntu.domain.Directory;
import com.signzy.ubuntu.domain.*; // for static metamodels
import com.signzy.ubuntu.repository.DirectoryRepository;
import com.signzy.ubuntu.service.dto.DirectoryCriteria;
import com.signzy.ubuntu.service.dto.DirectoryDTO;
import com.signzy.ubuntu.service.mapper.DirectoryMapper;

/**
 * Service for executing complex queries for Directory entities in the database.
 * The main input is a {@link DirectoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DirectoryDTO} or a {@link Page} of {@link DirectoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DirectoryQueryService extends QueryService<Directory> {

    private final Logger log = LoggerFactory.getLogger(DirectoryQueryService.class);

    private final DirectoryRepository directoryRepository;

    private final DirectoryMapper directoryMapper;

    public DirectoryQueryService(DirectoryRepository directoryRepository, DirectoryMapper directoryMapper) {
        this.directoryRepository = directoryRepository;
        this.directoryMapper = directoryMapper;
    }

    /**
     * Return a {@link List} of {@link DirectoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DirectoryDTO> findByCriteria(DirectoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Directory> specification = createSpecification(criteria);
        return directoryMapper.toDto(directoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DirectoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DirectoryDTO> findByCriteria(DirectoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Directory> specification = createSpecification(criteria);
        return directoryRepository.findAll(specification, page)
            .map(directoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DirectoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Directory> specification = createSpecification(criteria);
        return directoryRepository.count(specification);
    }

    /**
     * Function to convert DirectoryCriteria to a {@link Specification}
     */
    private Specification<Directory> createSpecification(DirectoryCriteria criteria) {
        Specification<Directory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Directory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Directory_.name));
            }
            if (criteria.getParent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParent(), Directory_.parent));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Directory_.type));
            }
            if (criteria.getIsDirectory() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDirectory(), Directory_.isDirectory));
            }
            if (criteria.getTimeStamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeStamp(), Directory_.timeStamp));
            }
        }
        return specification;
    }
}
