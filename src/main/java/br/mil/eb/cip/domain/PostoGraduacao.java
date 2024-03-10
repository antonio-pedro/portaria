package br.mil.eb.cip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PostoGraduacao.
 */
@Entity
@Table(name = "posto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostoGraduacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull
    @Column(name = "sigla", nullable = false)
    private String sigla;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "posto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "posto", "om" }, allowSetters = true)
    private Set<Militar> militars = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostoGraduacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public PostoGraduacao descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return this.sigla;
    }

    public PostoGraduacao sigla(String sigla) {
        this.setSigla(sigla);
        return this;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Set<Militar> getMilitars() {
        return this.militars;
    }

    public void setMilitars(Set<Militar> militars) {
        if (this.militars != null) {
            this.militars.forEach(i -> i.setPosto(null));
        }
        if (militars != null) {
            militars.forEach(i -> i.setPosto(this));
        }
        this.militars = militars;
    }

    public PostoGraduacao militars(Set<Militar> militars) {
        this.setMilitars(militars);
        return this;
    }

    public PostoGraduacao addMilitar(Militar militar) {
        this.militars.add(militar);
        militar.setPosto(this);
        return this;
    }

    public PostoGraduacao removeMilitar(Militar militar) {
        this.militars.remove(militar);
        militar.setPosto(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostoGraduacao)) {
            return false;
        }
        return getId() != null && getId().equals(((PostoGraduacao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostoGraduacao{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", sigla='" + getSigla() + "'" +
            "}";
    }
}
