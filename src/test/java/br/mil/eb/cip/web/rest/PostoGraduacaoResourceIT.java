package br.mil.eb.cip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.mil.eb.cip.IntegrationTest;
import br.mil.eb.cip.domain.Militar;
import br.mil.eb.cip.domain.PostoGraduacao;
import br.mil.eb.cip.repository.PostoGraduacaoRepository;
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
 * Integration tests for the {@link PostoGraduacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostoGraduacaoResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLA = "AAAAAAAAAA";
    private static final String UPDATED_SIGLA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/posto-graduacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PostoGraduacaoRepository postoGraduacaoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostoGraduacaoMockMvc;

    private PostoGraduacao postoGraduacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostoGraduacao createEntity(EntityManager em) {
        PostoGraduacao postoGraduacao = new PostoGraduacao().descricao(DEFAULT_DESCRICAO).sigla(DEFAULT_SIGLA);
        // Add required entity
        Militar militar;
        if (TestUtil.findAll(em, Militar.class).isEmpty()) {
            militar = MilitarResourceIT.createEntity(em);
            em.persist(militar);
            em.flush();
        } else {
            militar = TestUtil.findAll(em, Militar.class).get(0);
        }
        postoGraduacao.getMilitars().add(militar);
        return postoGraduacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostoGraduacao createUpdatedEntity(EntityManager em) {
        PostoGraduacao postoGraduacao = new PostoGraduacao().descricao(UPDATED_DESCRICAO).sigla(UPDATED_SIGLA);
        // Add required entity
        Militar militar;
        if (TestUtil.findAll(em, Militar.class).isEmpty()) {
            militar = MilitarResourceIT.createUpdatedEntity(em);
            em.persist(militar);
            em.flush();
        } else {
            militar = TestUtil.findAll(em, Militar.class).get(0);
        }
        postoGraduacao.getMilitars().add(militar);
        return postoGraduacao;
    }

    @BeforeEach
    public void initTest() {
        postoGraduacao = createEntity(em);
    }

    @Test
    @Transactional
    void createPostoGraduacao() throws Exception {
        int databaseSizeBeforeCreate = postoGraduacaoRepository.findAll().size();
        // Create the PostoGraduacao
        restPostoGraduacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postoGraduacao))
            )
            .andExpect(status().isCreated());

        // Validate the PostoGraduacao in the database
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeCreate + 1);
        PostoGraduacao testPostoGraduacao = postoGraduacaoList.get(postoGraduacaoList.size() - 1);
        assertThat(testPostoGraduacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testPostoGraduacao.getSigla()).isEqualTo(DEFAULT_SIGLA);
    }

    @Test
    @Transactional
    void createPostoGraduacaoWithExistingId() throws Exception {
        // Create the PostoGraduacao with an existing ID
        postoGraduacao.setId(1L);

        int databaseSizeBeforeCreate = postoGraduacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostoGraduacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postoGraduacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostoGraduacao in the database
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = postoGraduacaoRepository.findAll().size();
        // set the field null
        postoGraduacao.setDescricao(null);

        // Create the PostoGraduacao, which fails.

        restPostoGraduacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postoGraduacao))
            )
            .andExpect(status().isBadRequest());

        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiglaIsRequired() throws Exception {
        int databaseSizeBeforeTest = postoGraduacaoRepository.findAll().size();
        // set the field null
        postoGraduacao.setSigla(null);

        // Create the PostoGraduacao, which fails.

        restPostoGraduacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postoGraduacao))
            )
            .andExpect(status().isBadRequest());

        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostoGraduacaos() throws Exception {
        // Initialize the database
        postoGraduacaoRepository.saveAndFlush(postoGraduacao);

        // Get all the postoGraduacaoList
        restPostoGraduacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postoGraduacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)));
    }

    @Test
    @Transactional
    void getPostoGraduacao() throws Exception {
        // Initialize the database
        postoGraduacaoRepository.saveAndFlush(postoGraduacao);

        // Get the postoGraduacao
        restPostoGraduacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, postoGraduacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postoGraduacao.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA));
    }

    @Test
    @Transactional
    void getNonExistingPostoGraduacao() throws Exception {
        // Get the postoGraduacao
        restPostoGraduacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostoGraduacao() throws Exception {
        // Initialize the database
        postoGraduacaoRepository.saveAndFlush(postoGraduacao);

        int databaseSizeBeforeUpdate = postoGraduacaoRepository.findAll().size();

        // Update the postoGraduacao
        PostoGraduacao updatedPostoGraduacao = postoGraduacaoRepository.findById(postoGraduacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostoGraduacao are not directly saved in db
        em.detach(updatedPostoGraduacao);
        updatedPostoGraduacao.descricao(UPDATED_DESCRICAO).sigla(UPDATED_SIGLA);

        restPostoGraduacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPostoGraduacao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPostoGraduacao))
            )
            .andExpect(status().isOk());

        // Validate the PostoGraduacao in the database
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeUpdate);
        PostoGraduacao testPostoGraduacao = postoGraduacaoList.get(postoGraduacaoList.size() - 1);
        assertThat(testPostoGraduacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testPostoGraduacao.getSigla()).isEqualTo(UPDATED_SIGLA);
    }

    @Test
    @Transactional
    void putNonExistingPostoGraduacao() throws Exception {
        int databaseSizeBeforeUpdate = postoGraduacaoRepository.findAll().size();
        postoGraduacao.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostoGraduacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postoGraduacao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postoGraduacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostoGraduacao in the database
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostoGraduacao() throws Exception {
        int databaseSizeBeforeUpdate = postoGraduacaoRepository.findAll().size();
        postoGraduacao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostoGraduacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postoGraduacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostoGraduacao in the database
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostoGraduacao() throws Exception {
        int databaseSizeBeforeUpdate = postoGraduacaoRepository.findAll().size();
        postoGraduacao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostoGraduacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postoGraduacao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostoGraduacao in the database
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostoGraduacaoWithPatch() throws Exception {
        // Initialize the database
        postoGraduacaoRepository.saveAndFlush(postoGraduacao);

        int databaseSizeBeforeUpdate = postoGraduacaoRepository.findAll().size();

        // Update the postoGraduacao using partial update
        PostoGraduacao partialUpdatedPostoGraduacao = new PostoGraduacao();
        partialUpdatedPostoGraduacao.setId(postoGraduacao.getId());

        restPostoGraduacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostoGraduacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostoGraduacao))
            )
            .andExpect(status().isOk());

        // Validate the PostoGraduacao in the database
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeUpdate);
        PostoGraduacao testPostoGraduacao = postoGraduacaoList.get(postoGraduacaoList.size() - 1);
        assertThat(testPostoGraduacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testPostoGraduacao.getSigla()).isEqualTo(DEFAULT_SIGLA);
    }

    @Test
    @Transactional
    void fullUpdatePostoGraduacaoWithPatch() throws Exception {
        // Initialize the database
        postoGraduacaoRepository.saveAndFlush(postoGraduacao);

        int databaseSizeBeforeUpdate = postoGraduacaoRepository.findAll().size();

        // Update the postoGraduacao using partial update
        PostoGraduacao partialUpdatedPostoGraduacao = new PostoGraduacao();
        partialUpdatedPostoGraduacao.setId(postoGraduacao.getId());

        partialUpdatedPostoGraduacao.descricao(UPDATED_DESCRICAO).sigla(UPDATED_SIGLA);

        restPostoGraduacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostoGraduacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostoGraduacao))
            )
            .andExpect(status().isOk());

        // Validate the PostoGraduacao in the database
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeUpdate);
        PostoGraduacao testPostoGraduacao = postoGraduacaoList.get(postoGraduacaoList.size() - 1);
        assertThat(testPostoGraduacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testPostoGraduacao.getSigla()).isEqualTo(UPDATED_SIGLA);
    }

    @Test
    @Transactional
    void patchNonExistingPostoGraduacao() throws Exception {
        int databaseSizeBeforeUpdate = postoGraduacaoRepository.findAll().size();
        postoGraduacao.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostoGraduacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postoGraduacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postoGraduacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostoGraduacao in the database
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostoGraduacao() throws Exception {
        int databaseSizeBeforeUpdate = postoGraduacaoRepository.findAll().size();
        postoGraduacao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostoGraduacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postoGraduacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostoGraduacao in the database
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostoGraduacao() throws Exception {
        int databaseSizeBeforeUpdate = postoGraduacaoRepository.findAll().size();
        postoGraduacao.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostoGraduacaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(postoGraduacao))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostoGraduacao in the database
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostoGraduacao() throws Exception {
        // Initialize the database
        postoGraduacaoRepository.saveAndFlush(postoGraduacao);

        int databaseSizeBeforeDelete = postoGraduacaoRepository.findAll().size();

        // Delete the postoGraduacao
        restPostoGraduacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, postoGraduacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostoGraduacao> postoGraduacaoList = postoGraduacaoRepository.findAll();
        assertThat(postoGraduacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
