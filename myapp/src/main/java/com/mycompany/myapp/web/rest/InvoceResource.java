package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Invoce;
import com.mycompany.myapp.repository.InvoceRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Invoce}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InvoceResource {

    private final Logger log = LoggerFactory.getLogger(InvoceResource.class);

    private static final String ENTITY_NAME = "invoce";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoceRepository invoceRepository;

    public InvoceResource(InvoceRepository invoceRepository) {
        this.invoceRepository = invoceRepository;
    }

    /**
     * {@code POST  /invoces} : Create a new invoce.
     *
     * @param invoce the invoce to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoce, or with status {@code 400 (Bad Request)} if the invoce has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoces")
    public ResponseEntity<Invoce> createInvoce(@RequestBody Invoce invoce) throws URISyntaxException {
        log.debug("REST request to save Invoce : {}", invoce);
        if (invoce.getId() != null) {
            throw new BadRequestAlertException("A new invoce cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Invoce result = invoceRepository.save(invoce);
        return ResponseEntity.created(new URI("/api/invoces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoces} : Updates an existing invoce.
     *
     * @param invoce the invoce to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoce,
     * or with status {@code 400 (Bad Request)} if the invoce is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoce couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoces")
    public ResponseEntity<Invoce> updateInvoce(@RequestBody Invoce invoce) throws URISyntaxException {
        log.debug("REST request to update Invoce : {}", invoce);
        if (invoce.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Invoce result = invoceRepository.save(invoce);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoce.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /invoces} : get all the invoces.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoces in body.
     */
    @GetMapping("/invoces")
    public List<Invoce> getAllInvoces() {
        log.debug("REST request to get all Invoces");
        return invoceRepository.findAll();
    }

    /**
     * {@code GET  /invoces/:id} : get the "id" invoce.
     *
     * @param id the id of the invoce to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoce, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoces/{id}")
    public ResponseEntity<Invoce> getInvoce(@PathVariable Long id) {
        log.debug("REST request to get Invoce : {}", id);
        Optional<Invoce> invoce = invoceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(invoce);
    }

    /**
     * {@code DELETE  /invoces/:id} : delete the "id" invoce.
     *
     * @param id the id of the invoce to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoces/{id}")
    public ResponseEntity<Void> deleteInvoce(@PathVariable Long id) {
        log.debug("REST request to delete Invoce : {}", id);
        invoceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
