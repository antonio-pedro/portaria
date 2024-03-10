package br.mil.eb.cip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.mil.eb.cip.IntegrationTest;
import br.mil.eb.cip.domain.Militar;
import br.mil.eb.cip.domain.OM;
import br.mil.eb.cip.domain.PostoGraduacao;
import br.mil.eb.cip.repository.MilitarRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MilitarResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MilitarResourceIT {

    private static final Integer DEFAULT_IDENTIDADE = 1;
    private static final Integer UPDATED_IDENTIDADE = 2;

    private static final Integer DEFAULT_CPF = 1;
    private static final Integer UPDATED_CPF = 2;

    private static final String DEFAULT_POSTO_GRADUACAO = "AAAAAAAAAA";
    private static final String UPDATED_POSTO_GRADUACAO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_GUERRA = "AAAAAAAAAA";
    private static final String UPDATED_NOME_GUERRA = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/militars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MilitarRepository militarRepository;

    @Mock
    private MilitarRepository militarRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMilitarMockMvc;

    private Militar militar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Militar createEntity(EntityManager em) {
        Militar militar = new Militar()
            .identidade(DEFAULT_IDENTIDADE)
            .cpf(DEFAULT_CPF)
            .postoGraduacao(DEFAULT_POSTO_GRADUACAO)
            .nome(DEFAULT_NOME)
            .nomeGuerra(DEFAULT_NOME_GUERRA)
            .email(DEFAULT_EMAIL)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE);
        // Add required entity
        PostoGraduacao postoGraduacao;
        if (TestUtil.findAll(em, PostoGraduacao.class).isEmpty()) {
            postoGraduacao = PostoGraduacaoResourceIT.createEntity(em);
            em.persist(postoGraduacao);
            em.flush();
        } else {
            postoGraduacao = TestUtil.findAll(em, PostoGraduacao.class).get(0);
        }
        militar.setPosto(postoGraduacao);
        // Add required entity
        OM oM;
        if (TestUtil.findAll(em, OM.class).isEmpty()) {
            oM = OMResourceIT.createEntity(em);
            em.persist(oM);
            em.flush();
        } else {
            oM = TestUtil.findAll(em, OM.class).get(0);
        }
        militar.setOm(oM);
        return militar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Militar createUpdatedEntity(EntityManager em) {
        Militar militar = new Militar()
            .identidade(UPDATED_IDENTIDADE)
            .cpf(UPDATED_CPF)
            .postoGraduacao(UPDATED_POSTO_GRADUACAO)
            .nome(UPDATED_NOME)
            .nomeGuerra(UPDATED_NOME_GUERRA)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        // Add required entity
        PostoGraduacao postoGraduacao;
        if (TestUtil.findAll(em, PostoGraduacao.class).isEmpty()) {
            postoGraduacao = PostoGraduacaoResourceIT.createUpdatedEntity(em);
            em.persist(postoGraduacao);
            em.flush();
        } else {
            postoGraduacao = TestUtil.findAll(em, PostoGraduacao.class).get(0);
        }
        militar.setPosto(postoGraduacao);
        // Add required entity
        OM oM;
        if (TestUtil.findAll(em, OM.class).isEmpty()) {
            oM = OMResourceIT.createUpdatedEntity(em);
            em.persist(oM);
            em.flush();
        } else {
            oM = TestUtil.findAll(em, OM.class).get(0);
        }
        militar.setOm(oM);
        return militar;
    }

    @BeforeEach
    public void initTest() {
        militar = createEntity(em);
    }

    @Test
    @Transactional
    void createMilitar() throws Exception {
        int databaseSizeBeforeCreate = militarRepository.findAll().size();
        // Create the Militar
        restMilitarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(militar)))
            .andExpect(status().isCreated());

        // Validate the Militar in the database
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeCreate + 1);
        Militar testMilitar = militarList.get(militarList.size() - 1);
        assertThat(testMilitar.getIdentidade()).isEqualTo(DEFAULT_IDENTIDADE);
        assertThat(testMilitar.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testMilitar.getPostoGraduacao()).isEqualTo(DEFAULT_POSTO_GRADUACAO);
        assertThat(testMilitar.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMilitar.getNomeGuerra()).isEqualTo(DEFAULT_NOME_GUERRA);
        assertThat(testMilitar.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMilitar.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testMilitar.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createMilitarWithExistingId() throws Exception {
        // Create the Militar with an existing ID
        militar.setId(1L);

        int databaseSizeBeforeCreate = militarRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMilitarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(militar)))
            .andExpect(status().isBadRequest());

        // Validate the Militar in the database
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = militarRepository.findAll().size();
        // set the field null
        militar.setIdentidade(null);

        // Create the Militar, which fails.

        restMilitarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(militar)))
            .andExpect(status().isBadRequest());

        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCpfIsRequired() throws Exception {
        int databaseSizeBeforeTest = militarRepository.findAll().size();
        // set the field null
        militar.setCpf(null);

        // Create the Militar, which fails.

        restMilitarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(militar)))
            .andExpect(status().isBadRequest());

        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostoGraduacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = militarRepository.findAll().size();
        // set the field null
        militar.setPostoGraduacao(null);

        // Create the Militar, which fails.

        restMilitarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(militar)))
            .andExpect(status().isBadRequest());

        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = militarRepository.findAll().size();
        // set the field null
        militar.setNome(null);

        // Create the Militar, which fails.

        restMilitarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(militar)))
            .andExpect(status().isBadRequest());

        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeGuerraIsRequired() throws Exception {
        int databaseSizeBeforeTest = militarRepository.findAll().size();
        // set the field null
        militar.setNomeGuerra(null);

        // Create the Militar, which fails.

        restMilitarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(militar)))
            .andExpect(status().isBadRequest());

        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = militarRepository.findAll().size();
        // set the field null
        militar.setEmail(null);

        // Create the Militar, which fails.

        restMilitarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(militar)))
            .andExpect(status().isBadRequest());

        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMilitars() throws Exception {
        // Initialize the database
        militarRepository.saveAndFlush(militar);

        // Get all the militarList
        restMilitarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(militar.getId().intValue())))
            .andExpect(jsonPath("$.[*].identidade").value(hasItem(DEFAULT_IDENTIDADE)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].postoGraduacao").value(hasItem(DEFAULT_POSTO_GRADUACAO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].nomeGuerra").value(hasItem(DEFAULT_NOME_GUERRA)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FOTO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMilitarsWithEagerRelationshipsIsEnabled() throws Exception {
        when(militarRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMilitarMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(militarRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMilitarsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(militarRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMilitarMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(militarRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMilitar() throws Exception {
        // Initialize the database
        militarRepository.saveAndFlush(militar);

        // Get the militar
        restMilitarMockMvc
            .perform(get(ENTITY_API_URL_ID, militar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(militar.getId().intValue()))
            .andExpect(jsonPath("$.identidade").value(DEFAULT_IDENTIDADE))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.postoGraduacao").value(DEFAULT_POSTO_GRADUACAO))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.nomeGuerra").value(DEFAULT_NOME_GUERRA))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64.getEncoder().encodeToString(DEFAULT_FOTO)));
    }

    @Test
    @Transactional
    void getNonExistingMilitar() throws Exception {
        // Get the militar
        restMilitarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMilitar() throws Exception {
        // Initialize the database
        militarRepository.saveAndFlush(militar);

        int databaseSizeBeforeUpdate = militarRepository.findAll().size();

        // Update the militar
        Militar updatedMilitar = militarRepository.findById(militar.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMilitar are not directly saved in db
        em.detach(updatedMilitar);
        updatedMilitar
            .identidade(UPDATED_IDENTIDADE)
            .cpf(UPDATED_CPF)
            .postoGraduacao(UPDATED_POSTO_GRADUACAO)
            .nome(UPDATED_NOME)
            .nomeGuerra(UPDATED_NOME_GUERRA)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restMilitarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMilitar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMilitar))
            )
            .andExpect(status().isOk());

        // Validate the Militar in the database
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeUpdate);
        Militar testMilitar = militarList.get(militarList.size() - 1);
        assertThat(testMilitar.getIdentidade()).isEqualTo(UPDATED_IDENTIDADE);
        assertThat(testMilitar.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testMilitar.getPostoGraduacao()).isEqualTo(UPDATED_POSTO_GRADUACAO);
        assertThat(testMilitar.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMilitar.getNomeGuerra()).isEqualTo(UPDATED_NOME_GUERRA);
        assertThat(testMilitar.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMilitar.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testMilitar.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingMilitar() throws Exception {
        int databaseSizeBeforeUpdate = militarRepository.findAll().size();
        militar.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMilitarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, militar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(militar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Militar in the database
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMilitar() throws Exception {
        int databaseSizeBeforeUpdate = militarRepository.findAll().size();
        militar.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilitarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(militar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Militar in the database
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMilitar() throws Exception {
        int databaseSizeBeforeUpdate = militarRepository.findAll().size();
        militar.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilitarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(militar)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Militar in the database
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMilitarWithPatch() throws Exception {
        // Initialize the database
        militarRepository.saveAndFlush(militar);

        int databaseSizeBeforeUpdate = militarRepository.findAll().size();

        // Update the militar using partial update
        Militar partialUpdatedMilitar = new Militar();
        partialUpdatedMilitar.setId(militar.getId());

        partialUpdatedMilitar.nome(UPDATED_NOME).nomeGuerra(UPDATED_NOME_GUERRA);

        restMilitarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMilitar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMilitar))
            )
            .andExpect(status().isOk());

        // Validate the Militar in the database
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeUpdate);
        Militar testMilitar = militarList.get(militarList.size() - 1);
        assertThat(testMilitar.getIdentidade()).isEqualTo(DEFAULT_IDENTIDADE);
        assertThat(testMilitar.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testMilitar.getPostoGraduacao()).isEqualTo(DEFAULT_POSTO_GRADUACAO);
        assertThat(testMilitar.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMilitar.getNomeGuerra()).isEqualTo(UPDATED_NOME_GUERRA);
        assertThat(testMilitar.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMilitar.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testMilitar.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateMilitarWithPatch() throws Exception {
        // Initialize the database
        militarRepository.saveAndFlush(militar);

        int databaseSizeBeforeUpdate = militarRepository.findAll().size();

        // Update the militar using partial update
        Militar partialUpdatedMilitar = new Militar();
        partialUpdatedMilitar.setId(militar.getId());

        partialUpdatedMilitar
            .identidade(UPDATED_IDENTIDADE)
            .cpf(UPDATED_CPF)
            .postoGraduacao(UPDATED_POSTO_GRADUACAO)
            .nome(UPDATED_NOME)
            .nomeGuerra(UPDATED_NOME_GUERRA)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restMilitarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMilitar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMilitar))
            )
            .andExpect(status().isOk());

        // Validate the Militar in the database
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeUpdate);
        Militar testMilitar = militarList.get(militarList.size() - 1);
        assertThat(testMilitar.getIdentidade()).isEqualTo(UPDATED_IDENTIDADE);
        assertThat(testMilitar.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testMilitar.getPostoGraduacao()).isEqualTo(UPDATED_POSTO_GRADUACAO);
        assertThat(testMilitar.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMilitar.getNomeGuerra()).isEqualTo(UPDATED_NOME_GUERRA);
        assertThat(testMilitar.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMilitar.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testMilitar.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingMilitar() throws Exception {
        int databaseSizeBeforeUpdate = militarRepository.findAll().size();
        militar.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMilitarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, militar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(militar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Militar in the database
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMilitar() throws Exception {
        int databaseSizeBeforeUpdate = militarRepository.findAll().size();
        militar.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilitarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(militar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Militar in the database
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMilitar() throws Exception {
        int databaseSizeBeforeUpdate = militarRepository.findAll().size();
        militar.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilitarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(militar)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Militar in the database
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMilitar() throws Exception {
        // Initialize the database
        militarRepository.saveAndFlush(militar);

        int databaseSizeBeforeDelete = militarRepository.findAll().size();

        // Delete the militar
        restMilitarMockMvc
            .perform(delete(ENTITY_API_URL_ID, militar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Militar> militarList = militarRepository.findAll();
        assertThat(militarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
