package br.mil.eb.cip.web.rest;

import br.mil.eb.cip.domain.OM;
import br.mil.eb.cip.repository.OMRepository;
import br.mil.eb.cip.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.mil.eb.cip.domain.OM}.
 */
@RestController
@RequestMapping("/api/oms")
@Transactional
public class OMResource {

    private final Logger log = LoggerFactory.getLogger(OMResource.class);

    private static final String ENTITY_NAME = "oM";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OMRepository oMRepository;

    public OMResource(OMRepository oMRepository) {
        this.oMRepository = oMRepository;
    }

    /**
     * {@code POST  /oms} : Create a new oM.
     *
     * @param oM the oM to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oM, or with status {@code 400 (Bad Request)} if the oM has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OM> createOM(@Valid @RequestBody OM oM) throws URISyntaxException {
        log.debug("REST request to save OM : {}", oM);
        if (oM.getId() != null) {
            throw new BadRequestAlertException("A new oM cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OM result = oMRepository.save(oM);
        return ResponseEntity
            .created(new URI("/api/oms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /oms/:id} : Updates an existing oM.
     *
     * @param id the id of the oM to save.
     * @param oM the oM to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oM,
     * or with status {@code 400 (Bad Request)} if the oM is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oM couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OM> updateOM(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody OM oM)
        throws URISyntaxException {
        log.debug("REST request to update OM : {}, {}", id, oM);
        if (oM.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oM.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oMRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OM result = oMRepository.save(oM);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oM.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /oms/:id} : Partial updates given fields of an existing oM, field will ignore if it is null
     *
     * @param id the id of the oM to save.
     * @param oM the oM to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oM,
     * or with status {@code 400 (Bad Request)} if the oM is not valid,
     * or with status {@code 404 (Not Found)} if the oM is not found,
     * or with status {@code 500 (Internal Server Error)} if the oM couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OM> partialUpdateOM(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody OM oM)
        throws URISyntaxException {
        log.debug("REST request to partial update OM partially : {}, {}", id, oM);
        if (oM.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oM.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oMRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OM> result = oMRepository
            .findById(oM.getId())
            .map(existingOM -> {
                if (oM.getNome() != null) {
                    existingOM.setNome(oM.getNome());
                }
                if (oM.getSigla() != null) {
                    existingOM.setSigla(oM.getSigla());
                }
                if (oM.getCodom() != null) {
                    existingOM.setCodom(oM.getCodom());
                }

                return existingOM;
            })
            .map(oMRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oM.getId().toString())
        );
    }

    /**
     * {@code GET  /oms} : get all the oMS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oMS in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OM>> getAllOMS(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of OMS");
        Page<OM> page = oMRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /oms/:id} : get the "id" oM.
     *
     * @param id the id of the oM to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oM, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OM> getOM(@PathVariable("id") Long id) {
        log.debug("REST request to get OM : {}", id);
        Optional<OM> oM = oMRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(oM);
    }

    /**
     * {@code DELETE  /oms/:id} : delete the "id" oM.
     *
     * @param id the id of the oM to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOM(@PathVariable("id") Long id) {
        log.debug("REST request to delete OM : {}", id);
        oMRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
