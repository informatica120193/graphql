package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "commencement_date")
    private Instant commencementDate;

    @OneToOne
    @JoinColumn(unique = true)
    private Candidate candidate;

    @OneToMany(mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Invoce> invoces = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCommencementDate() {
        return commencementDate;
    }

    public Member commencementDate(Instant commencementDate) {
        this.commencementDate = commencementDate;
        return this;
    }

    public void setCommencementDate(Instant commencementDate) {
        this.commencementDate = commencementDate;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public Member candidate(Candidate candidate) {
        this.candidate = candidate;
        return this;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Set<Invoce> getInvoces() {
        return invoces;
    }

    public Member invoces(Set<Invoce> invoces) {
        this.invoces = invoces;
        return this;
    }

    public Member addInvoce(Invoce invoce) {
        this.invoces.add(invoce);
        invoce.setMember(this);
        return this;
    }

    public Member removeInvoce(Invoce invoce) {
        this.invoces.remove(invoce);
        invoce.setMember(null);
        return this;
    }

    public void setInvoces(Set<Invoce> invoces) {
        this.invoces = invoces;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return id != null && id.equals(((Member) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", commencementDate='" + getCommencementDate() + "'" +
            "}";
    }
}
