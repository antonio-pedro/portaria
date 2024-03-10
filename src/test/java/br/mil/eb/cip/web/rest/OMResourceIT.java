package br.mil.eb.cip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.mil.eb.cip.IntegrationTest;
import br.mil.eb.cip.domain.Militar;
import br.mil.eb.cip.domain.OM;
import br.mil.eb.cip.repository.OMRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OMResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OMResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLA = "AAAAAAAAAA";
    private static final String UPDATED_SIGLA = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODOM = 1;
    private static final Integer UPDATED_CODOM = 2;

    private static final String ENTITY_API_URL = "/api/oms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OMRepository oMRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOMMockMvc;

    private OM oM;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OM createEntity(EntityManager em) {
        OM oM = new OM().nome(DEFAULT_NOME).sigla(DEFAULT_SIGLA).codom(DEFAULT_CODOM);
        // Add required entity
        Militar militar;
        if (TestUtil.findAll(em, Militar.class).isEmpty()) {
            militar = MilitarResourceIT.createEntity(em);
            em.persist(militar);
            em.flush();
        } else {
            militar = TestUtil.findAll(em, Militar.class).get(0);
        }
        oM.getMilitars().add(militar);
        return oM;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OM createUpdatedEntity(EntityManager em) {
        OM oM = new OM().nome(UPDATED_NOME).sigla(UPDATED_SIGLA).codom(UPDATED_CODOM);
        // Add required entity
        Militar militar;
        if (TestUtil.findAll(em, Militar.class).isEmpty()) {
            militar = MilitarResourceIT.createUpdatedEntity(em);
            em.persist(militar);
            em.flush();
        } else {
            militar = TestUtil.findAll(em, Militar.class).get(0);
        }
        oM.getMilitars().add(militar);
        return oM;
    }

    @BeforeEach
    public void initTest() {
        oM = createEntity(em);
    }

    @Test
    @Transactional
    void createOM() throws Exception {
        int databaseSizeBeforeCreate = oMRepository.findAll().size();
        // Create the OM
        restOMMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oM)))
            .andExpect(status().isCreated());

        // Validate the OM in the database
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeCreate + 1);
        OM testOM = oMList.get(oMList.size() - 1);
        assertThat(testOM.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testOM.getSigla()).isEqualTo(DEFAULT_SIGLA);
        assertThat(testOM.getCodom()).isEqualTo(DEFAULT_CODOM);
    }

    @Test
    @Transactional
    void createOMWithExistingId() throws Exception {
        // Create the OM with an existing ID
        oM.setId(1L);

        int databaseSizeBeforeCreate = oMRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOMMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oM)))
            .andExpect(status().isBadRequest());

        // Validate the OM in the database
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = oMRepository.findAll().size();
        // set the field null
        oM.setNome(null);

        // Create the OM, which fails.

        restOMMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oM)))
            .andExpect(status().isBadRequest());

        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiglaIsRequired() throws Exception {
        int databaseSizeBeforeTest = oMRepository.findAll().size();
        // set the field null
        oM.setSigla(null);

        // Create the OM, which fails.

        restOMMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oM)))
            .andExpect(status().isBadRequest());

        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOMS() throws Exception {
        // Initialize the database
        oMRepository.saveAndFlush(oM);

        // Get all the oMList
        restOMMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oM.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].codom").value(hasItem(DEFAULT_CODOM)));
    }

    @Test
    @Transactional
    void getOM() throws Exception {
        // Initialize the database
        oMRepository.saveAndFlush(oM);

        // Get the oM
        restOMMockMvc
            .perform(get(ENTITY_API_URL_ID, oM.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oM.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA))
            .andExpect(jsonPath("$.codom").value(DEFAULT_CODOM));
    }

    @Test
    @Transactional
    void getNonExistingOM() throws Exception {
        // Get the oM
        restOMMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOM() throws Exception {
        // Initialize the database
        oMRepository.saveAndFlush(oM);

        int databaseSizeBeforeUpdate = oMRepository.findAll().size();

        // Update the oM
        OM updatedOM = oMRepository.findById(oM.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOM are not directly saved in db
        em.detach(updatedOM);
        updatedOM.nome(UPDATED_NOME).sigla(UPDATED_SIGLA).codom(UPDATED_CODOM);

        restOMMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOM.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOM))
            )
            .andExpect(status().isOk());

        // Validate the OM in the database
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeUpdate);
        OM testOM = oMList.get(oMList.size() - 1);
        assertThat(testOM.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testOM.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testOM.getCodom()).isEqualTo(UPDATED_CODOM);
    }

    @Test
    @Transactional
    void putNonExistingOM() throws Exception {
        int databaseSizeBeforeUpdate = oMRepository.findAll().size();
        oM.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOMMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oM.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oM))
            )
            .andExpect(status().isBadRequest());

        // Validate the OM in the database
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOM() throws Exception {
        int databaseSizeBeforeUpdate = oMRepository.findAll().size();
        oM.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOMMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oM))
            )
            .andExpect(status().isBadRequest());

        // Validate the OM in the database
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOM() throws Exception {
        int databaseSizeBeforeUpdate = oMRepository.findAll().size();
        oM.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOMMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oM)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OM in the database
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOMWithPatch() throws Exception {
        // Initialize the database
        oMRepository.saveAndFlush(oM);

        int databaseSizeBeforeUpdate = oMRepository.findAll().size();

        // Update the oM using partial update
        OM partialUpdatedOM = new OM();
        partialUpdatedOM.setId(oM.getId());

        partialUpdatedOM.sigla(UPDATED_SIGLA);

        restOMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOM.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOM))
            )
            .andExpect(status().isOk());

        // Validate the OM in the database
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeUpdate);
        OM testOM = oMList.get(oMList.size() - 1);
        assertThat(testOM.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testOM.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testOM.getCodom()).isEqualTo(DEFAULT_CODOM);
    }

    @Test
    @Transactional
    void fullUpdateOMWithPatch() throws Exception {
        // Initialize the database
        oMRepository.saveAndFlush(oM);

        int databaseSizeBeforeUpdate = oMRepository.findAll().size();

        // Update the oM using partial update
        OM partialUpdatedOM = new OM();
        partialUpdatedOM.setId(oM.getId());

        partialUpdatedOM.nome(UPDATED_NOME).sigla(UPDATED_SIGLA).codom(UPDATED_CODOM);

        restOMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOM.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOM))
            )
            .andExpect(status().isOk());

        // Validate the OM in the database
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeUpdate);
        OM testOM = oMList.get(oMList.size() - 1);
        assertThat(testOM.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testOM.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testOM.getCodom()).isEqualTo(UPDATED_CODOM);
    }

    @Test
    @Transactional
    void patchNonExistingOM() throws Exception {
        int databaseSizeBeforeUpdate = oMRepository.findAll().size();
        oM.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oM.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oM))
            )
            .andExpect(status().isBadRequest());

        // Validate the OM in the database
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOM() throws Exception {
        int databaseSizeBeforeUpdate = oMRepository.findAll().size();
        oM.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oM))
            )
            .andExpect(status().isBadRequest());

        // Validate the OM in the database
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOM() throws Exception {
        int databaseSizeBeforeUpdate = oMRepository.findAll().size();
        oM.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOMMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(oM)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OM in the database
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOM() throws Exception {
        // Initialize the database
        oMRepository.saveAndFlush(oM);

        int databaseSizeBeforeDelete = oMRepository.findAll().size();

        // Delete the oM
        restOMMockMvc.perform(delete(ENTITY_API_URL_ID, oM.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OM> oMList = oMRepository.findAll();
        assertThat(oMList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
