package com.signzy.ubuntu.web.rest;

import com.signzy.ubuntu.Ubuntu18App;

import com.signzy.ubuntu.domain.Directory;
import com.signzy.ubuntu.repository.DirectoryRepository;
import com.signzy.ubuntu.service.DirectoryService;
import com.signzy.ubuntu.service.dto.DirectoryDTO;
import com.signzy.ubuntu.service.mapper.DirectoryMapper;
import com.signzy.ubuntu.web.rest.errors.ExceptionTranslator;
import com.signzy.ubuntu.service.dto.DirectoryCriteria;
import com.signzy.ubuntu.service.DirectoryQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.signzy.ubuntu.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DirectoryResource REST controller.
 *
 * @see DirectoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Ubuntu18App.class)
public class DirectoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT = "AAAAAAAAAA";
    private static final String UPDATED_PARENT = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DIRECTORY = false;
    private static final Boolean UPDATED_IS_DIRECTORY = true;

    private static final Instant DEFAULT_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private DirectoryMapper directoryMapper;

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    private DirectoryQueryService directoryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDirectoryMockMvc;

    private Directory directory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DirectoryResource directoryResource = new DirectoryResource(directoryService, directoryQueryService);
        this.restDirectoryMockMvc = MockMvcBuilders.standaloneSetup(directoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directory createEntity(EntityManager em) {
        Directory directory = new Directory()
            .name(DEFAULT_NAME)
            .parent(DEFAULT_PARENT)
            .type(DEFAULT_TYPE)
            .isDirectory(DEFAULT_IS_DIRECTORY)
            .timeStamp(DEFAULT_TIME_STAMP);
        return directory;
    }

    @Before
    public void initTest() {
        directory = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirectory() throws Exception {
        int databaseSizeBeforeCreate = directoryRepository.findAll().size();

        // Create the Directory
        DirectoryDTO directoryDTO = directoryMapper.toDto(directory);
        restDirectoryMockMvc.perform(post("/api/directories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeCreate + 1);
        Directory testDirectory = directoryList.get(directoryList.size() - 1);
        assertThat(testDirectory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDirectory.getParent()).isEqualTo(DEFAULT_PARENT);
        assertThat(testDirectory.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDirectory.isIsDirectory()).isEqualTo(DEFAULT_IS_DIRECTORY);
        assertThat(testDirectory.getTimeStamp()).isEqualTo(DEFAULT_TIME_STAMP);
    }

    @Test
    @Transactional
    public void createDirectoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = directoryRepository.findAll().size();

        // Create the Directory with an existing ID
        directory.setId(1L);
        DirectoryDTO directoryDTO = directoryMapper.toDto(directory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectoryMockMvc.perform(post("/api/directories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDirectories() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList
        restDirectoryMockMvc.perform(get("/api/directories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isDirectory").value(hasItem(DEFAULT_IS_DIRECTORY.booleanValue())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(DEFAULT_TIME_STAMP.toString())));
    }
    
    @Test
    @Transactional
    public void getDirectory() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get the directory
        restDirectoryMockMvc.perform(get("/api/directories/{id}", directory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(directory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.parent").value(DEFAULT_PARENT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.isDirectory").value(DEFAULT_IS_DIRECTORY.booleanValue()))
            .andExpect(jsonPath("$.timeStamp").value(DEFAULT_TIME_STAMP.toString()));
    }

    @Test
    @Transactional
    public void getAllDirectoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where name equals to DEFAULT_NAME
        defaultDirectoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the directoryList where name equals to UPDATED_NAME
        defaultDirectoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDirectoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDirectoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the directoryList where name equals to UPDATED_NAME
        defaultDirectoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDirectoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where name is not null
        defaultDirectoryShouldBeFound("name.specified=true");

        // Get all the directoryList where name is null
        defaultDirectoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllDirectoriesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where parent equals to DEFAULT_PARENT
        defaultDirectoryShouldBeFound("parent.equals=" + DEFAULT_PARENT);

        // Get all the directoryList where parent equals to UPDATED_PARENT
        defaultDirectoryShouldNotBeFound("parent.equals=" + UPDATED_PARENT);
    }

    @Test
    @Transactional
    public void getAllDirectoriesByParentIsInShouldWork() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where parent in DEFAULT_PARENT or UPDATED_PARENT
        defaultDirectoryShouldBeFound("parent.in=" + DEFAULT_PARENT + "," + UPDATED_PARENT);

        // Get all the directoryList where parent equals to UPDATED_PARENT
        defaultDirectoryShouldNotBeFound("parent.in=" + UPDATED_PARENT);
    }

    @Test
    @Transactional
    public void getAllDirectoriesByParentIsNullOrNotNull() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where parent is not null
        defaultDirectoryShouldBeFound("parent.specified=true");

        // Get all the directoryList where parent is null
        defaultDirectoryShouldNotBeFound("parent.specified=false");
    }

    @Test
    @Transactional
    public void getAllDirectoriesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where type equals to DEFAULT_TYPE
        defaultDirectoryShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the directoryList where type equals to UPDATED_TYPE
        defaultDirectoryShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDirectoriesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultDirectoryShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the directoryList where type equals to UPDATED_TYPE
        defaultDirectoryShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDirectoriesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where type is not null
        defaultDirectoryShouldBeFound("type.specified=true");

        // Get all the directoryList where type is null
        defaultDirectoryShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllDirectoriesByIsDirectoryIsEqualToSomething() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where isDirectory equals to DEFAULT_IS_DIRECTORY
        defaultDirectoryShouldBeFound("isDirectory.equals=" + DEFAULT_IS_DIRECTORY);

        // Get all the directoryList where isDirectory equals to UPDATED_IS_DIRECTORY
        defaultDirectoryShouldNotBeFound("isDirectory.equals=" + UPDATED_IS_DIRECTORY);
    }

    @Test
    @Transactional
    public void getAllDirectoriesByIsDirectoryIsInShouldWork() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where isDirectory in DEFAULT_IS_DIRECTORY or UPDATED_IS_DIRECTORY
        defaultDirectoryShouldBeFound("isDirectory.in=" + DEFAULT_IS_DIRECTORY + "," + UPDATED_IS_DIRECTORY);

        // Get all the directoryList where isDirectory equals to UPDATED_IS_DIRECTORY
        defaultDirectoryShouldNotBeFound("isDirectory.in=" + UPDATED_IS_DIRECTORY);
    }

    @Test
    @Transactional
    public void getAllDirectoriesByIsDirectoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where isDirectory is not null
        defaultDirectoryShouldBeFound("isDirectory.specified=true");

        // Get all the directoryList where isDirectory is null
        defaultDirectoryShouldNotBeFound("isDirectory.specified=false");
    }

    @Test
    @Transactional
    public void getAllDirectoriesByTimeStampIsEqualToSomething() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where timeStamp equals to DEFAULT_TIME_STAMP
        defaultDirectoryShouldBeFound("timeStamp.equals=" + DEFAULT_TIME_STAMP);

        // Get all the directoryList where timeStamp equals to UPDATED_TIME_STAMP
        defaultDirectoryShouldNotBeFound("timeStamp.equals=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllDirectoriesByTimeStampIsInShouldWork() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where timeStamp in DEFAULT_TIME_STAMP or UPDATED_TIME_STAMP
        defaultDirectoryShouldBeFound("timeStamp.in=" + DEFAULT_TIME_STAMP + "," + UPDATED_TIME_STAMP);

        // Get all the directoryList where timeStamp equals to UPDATED_TIME_STAMP
        defaultDirectoryShouldNotBeFound("timeStamp.in=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllDirectoriesByTimeStampIsNullOrNotNull() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList where timeStamp is not null
        defaultDirectoryShouldBeFound("timeStamp.specified=true");

        // Get all the directoryList where timeStamp is null
        defaultDirectoryShouldNotBeFound("timeStamp.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDirectoryShouldBeFound(String filter) throws Exception {
        restDirectoryMockMvc.perform(get("/api/directories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].isDirectory").value(hasItem(DEFAULT_IS_DIRECTORY.booleanValue())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(DEFAULT_TIME_STAMP.toString())));

        // Check, that the count call also returns 1
        restDirectoryMockMvc.perform(get("/api/directories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDirectoryShouldNotBeFound(String filter) throws Exception {
        restDirectoryMockMvc.perform(get("/api/directories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDirectoryMockMvc.perform(get("/api/directories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDirectory() throws Exception {
        // Get the directory
        restDirectoryMockMvc.perform(get("/api/directories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirectory() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();

        // Update the directory
        Directory updatedDirectory = directoryRepository.findById(directory.getId()).get();
        // Disconnect from session so that the updates on updatedDirectory are not directly saved in db
        em.detach(updatedDirectory);
        updatedDirectory
            .name(UPDATED_NAME)
            .parent(UPDATED_PARENT)
            .type(UPDATED_TYPE)
            .isDirectory(UPDATED_IS_DIRECTORY)
            .timeStamp(UPDATED_TIME_STAMP);
        DirectoryDTO directoryDTO = directoryMapper.toDto(updatedDirectory);

        restDirectoryMockMvc.perform(put("/api/directories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directoryDTO)))
            .andExpect(status().isOk());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
        Directory testDirectory = directoryList.get(directoryList.size() - 1);
        assertThat(testDirectory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDirectory.getParent()).isEqualTo(UPDATED_PARENT);
        assertThat(testDirectory.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDirectory.isIsDirectory()).isEqualTo(UPDATED_IS_DIRECTORY);
        assertThat(testDirectory.getTimeStamp()).isEqualTo(UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingDirectory() throws Exception {
        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();

        // Create the Directory
        DirectoryDTO directoryDTO = directoryMapper.toDto(directory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectoryMockMvc.perform(put("/api/directories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDirectory() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        int databaseSizeBeforeDelete = directoryRepository.findAll().size();

        // Delete the directory
        restDirectoryMockMvc.perform(delete("/api/directories/{id}", directory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Directory.class);
        Directory directory1 = new Directory();
        directory1.setId(1L);
        Directory directory2 = new Directory();
        directory2.setId(directory1.getId());
        assertThat(directory1).isEqualTo(directory2);
        directory2.setId(2L);
        assertThat(directory1).isNotEqualTo(directory2);
        directory1.setId(null);
        assertThat(directory1).isNotEqualTo(directory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DirectoryDTO.class);
        DirectoryDTO directoryDTO1 = new DirectoryDTO();
        directoryDTO1.setId(1L);
        DirectoryDTO directoryDTO2 = new DirectoryDTO();
        assertThat(directoryDTO1).isNotEqualTo(directoryDTO2);
        directoryDTO2.setId(directoryDTO1.getId());
        assertThat(directoryDTO1).isEqualTo(directoryDTO2);
        directoryDTO2.setId(2L);
        assertThat(directoryDTO1).isNotEqualTo(directoryDTO2);
        directoryDTO1.setId(null);
        assertThat(directoryDTO1).isNotEqualTo(directoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(directoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(directoryMapper.fromId(null)).isNull();
    }
}
