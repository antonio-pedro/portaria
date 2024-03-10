package br.mil.eb.cip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Militar.
 */
@Entity
@Table(name = "militar")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Militar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "identidade", nullable = false)
    private Integer identidade;

    @NotNull
    @Column(name = "cpf", nullable = false)
    private Integer cpf;

    @NotNull
    @Column(name = "posto_graduacao", nullable = false)
    private String postoGraduacao;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "nome_guerra", nullable = false)
    private String nomeGuerra;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Lob
    @Column(name = "foto", nullable = false)
    private byte[] foto;

    @NotNull
    @Column(name = "foto_content_type", nullable = false)
    private String fotoContentType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "militars" }, allowSetters = true)
    private PostoGraduacao posto;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "militars" }, allowSetters = true)
    private OM om;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Militar id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdentidade() {
        return this.identidade;
    }

    public Militar identidade(Integer identidade) {
        this.setIdentidade(identidade);
        return this;
    }

    public void setIdentidade(Integer identidade) {
        this.identidade = identidade;
    }

    public Integer getCpf() {
        return this.cpf;
    }

    public Militar cpf(Integer cpf) {
        this.setCpf(cpf);
        return this;
    }

    public void setCpf(Integer cpf) {
        this.cpf = cpf;
    }

    public String getPostoGraduacao() {
        return this.postoGraduacao;
    }

    public Militar postoGraduacao(String postoGraduacao) {
        this.setPostoGraduacao(postoGraduacao);
        return this;
    }

    public void setPostoGraduacao(String postoGraduacao) {
        this.postoGraduacao = postoGraduacao;
    }

    public String getNome() {
        return this.nome;
    }

    public Militar nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeGuerra() {
        return this.nomeGuerra;
    }

    public Militar nomeGuerra(String nomeGuerra) {
        this.setNomeGuerra(nomeGuerra);
        return this;
    }

    public void setNomeGuerra(String nomeGuerra) {
        this.nomeGuerra = nomeGuerra;
    }

    public String getEmail() {
        return this.email;
    }

    public Militar email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getFoto() {
        return this.foto;
    }

    public Militar foto(byte[] foto) {
        this.setFoto(foto);
        return this;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return this.fotoContentType;
    }

    public Militar fotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
        return this;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public PostoGraduacao getPosto() {
        return this.posto;
    }

    public void setPosto(PostoGraduacao postoGraduacao) {
        this.posto = postoGraduacao;
    }

    public Militar posto(PostoGraduacao postoGraduacao) {
        this.setPosto(postoGraduacao);
        return this;
    }

    public OM getOm() {
        return this.om;
    }

    public void setOm(OM oM) {
        this.om = oM;
    }

    public Militar om(OM oM) {
        this.setOm(oM);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Militar)) {
            return false;
        }
        return getId() != null && getId().equals(((Militar) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Militar{" +
            "id=" + getId() +
            ", identidade=" + getIdentidade() +
            ", cpf=" + getCpf() +
            ", postoGraduacao='" + getPostoGraduacao() + "'" +
            ", nome='" + getNome() + "'" +
            ", nomeGuerra='" + getNomeGuerra() + "'" +
            ", email='" + getEmail() + "'" +
            ", foto='" + getFoto() + "'" +
            ", fotoContentType='" + getFotoContentType() + "'" +
            "}";
    }
}
