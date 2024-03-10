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
 * A OM.
 */
@Entity
@Table(name = "om")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OM implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @NotNull
    @Size(max = 30)
    @Column(name = "sigla", length = 30, nullable = false)
    private String sigla;

    @Column(name = "codom")
    private Integer codom;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "om")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "posto", "om" }, allowSetters = true)
    private Set<Militar> militars = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OM id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public OM nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return this.sigla;
    }

    public OM sigla(String sigla) {
        this.setSigla(sigla);
        return this;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Integer getCodom() {
        return this.codom;
    }

    public OM codom(Integer codom) {
        this.setCodom(codom);
        return this;
    }

    public void setCodom(Integer codom) {
        this.codom = codom;
    }

    public Set<Militar> getMilitars() {
        return this.militars;
    }

    public void setMilitars(Set<Militar> militars) {
        if (this.militars != null) {
            this.militars.forEach(i -> i.setOm(null));
        }
        if (militars != null) {
            militars.forEach(i -> i.setOm(this));
        }
        this.militars = militars;
    }

    public OM militars(Set<Militar> militars) {
        this.setMilitars(militars);
        return this;
    }

    public OM addMilitar(Militar militar) {
        this.militars.add(militar);
        militar.setOm(this);
        return this;
    }

    public OM removeMilitar(Militar militar) {
        this.militars.remove(militar);
        militar.setOm(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OM)) {
            return false;
        }
        return getId() != null && getId().equals(((OM) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OM{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sigla='" + getSigla() + "'" +
            ", codom=" + getCodom() +
            "}";
    }
}
