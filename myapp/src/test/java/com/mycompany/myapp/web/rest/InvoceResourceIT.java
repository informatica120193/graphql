package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyappApp;
import com.mycompany.myapp.domain.Invoce;
import com.mycompany.myapp.repository.InvoceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InvoceResource} REST controller.
 */
@SpringBootTest(classes = MyappApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class InvoceResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_VALUE = 1L;
    private static final Long UPDATED_VALUE = 2L;

    @Autowired
    private InvoceRepository invoceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoceMockMvc;

    private Invoce invoce;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoce createEntity(EntityManager em) {
        Invoce invoce = new Invoce()
            .date(DEFAULT_DATE)
            .value(DEFAULT_VALUE);
        return invoce;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoce createUpdatedEntity(EntityManager em) {
        Invoce invoce = new Invoce()
            .date(UPDATED_DATE)
            .value(UPDATED_VALUE);
        return invoce;
    }

    @BeforeEach
    public void initTest() {
        invoce = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoce() throws Exception {
        int databaseSizeBeforeCreate = invoceRepository.findAll().size();

        // Create the Invoce
        restInvoceMockMvc.perform(post("/api/invoces")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invoce)))
            .andExpect(status().isCreated());

        // Validate the Invoce in the database
        List<Invoce> invoceList = invoceRepository.findAll();
        assertThat(invoceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoce testInvoce = invoceList.get(invoceList.size() - 1);
        assertThat(testInvoce.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testInvoce.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createInvoceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoceRepository.findAll().size();

        // Create the Invoce with an existing ID
        invoce.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoceMockMvc.perform(post("/api/invoces")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invoce)))
            .andExpect(status().isBadRequest());

        // Validate the Invoce in the database
        List<Invoce> invoceList = invoceRepository.findAll();
        assertThat(invoceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInvoces() throws Exception {
        // Initialize the database
        invoceRepository.saveAndFlush(invoce);

        // Get all the invoceList
        restInvoceMockMvc.perform(get("/api/invoces?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoce.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())));
    }
    
    @Test
    @Transactional
    public void getInvoce() throws Exception {
        // Initialize the database
        invoceRepository.saveAndFlush(invoce);

        // Get the invoce
        restInvoceMockMvc.perform(get("/api/invoces/{id}", invoce.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoce.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoce() throws Exception {
        // Get the invoce
        restInvoceMockMvc.perform(get("/api/invoces/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoce() throws Exception {
        // Initialize the database
        invoceRepository.saveAndFlush(invoce);

        int databaseSizeBeforeUpdate = invoceRepository.findAll().size();

        // Update the invoce
        Invoce updatedInvoce = invoceRepository.findById(invoce.getId()).get();
        // Disconnect from session so that the updates on updatedInvoce are not directly saved in db
        em.detach(updatedInvoce);
        updatedInvoce
            .date(UPDATED_DATE)
            .value(UPDATED_VALUE);

        restInvoceMockMvc.perform(put("/api/invoces")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoce)))
            .andExpect(status().isOk());

        // Validate the Invoce in the database
        List<Invoce> invoceList = invoceRepository.findAll();
        assertThat(invoceList).hasSize(databaseSizeBeforeUpdate);
        Invoce testInvoce = invoceList.get(invoceList.size() - 1);
        assertThat(testInvoce.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testInvoce.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoce() throws Exception {
        int databaseSizeBeforeUpdate = invoceRepository.findAll().size();

        // Create the Invoce

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoceMockMvc.perform(put("/api/invoces")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invoce)))
            .andExpect(status().isBadRequest());

        // Validate the Invoce in the database
        List<Invoce> invoceList = invoceRepository.findAll();
        assertThat(invoceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoce() throws Exception {
        // Initialize the database
        invoceRepository.saveAndFlush(invoce);

        int databaseSizeBeforeDelete = invoceRepository.findAll().size();

        // Delete the invoce
        restInvoceMockMvc.perform(delete("/api/invoces/{id}", invoce.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoce> invoceList = invoceRepository.findAll();
        assertThat(invoceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
