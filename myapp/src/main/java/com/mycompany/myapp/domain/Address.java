package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "residence")
    private String residence;

    @OneToMany(mappedBy = "address")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Applicant> applicants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public Address street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public Address houseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Address zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getResidence() {
        return residence;
    }

    public Address residence(String residence) {
        this.residence = residence;
        return this;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public Set<Applicant> getApplicants() {
        return applicants;
    }

    public Address applicants(Set<Applicant> applicants) {
        this.applicants = applicants;
        return this;
    }

    public Address addApplicant(Applicant applicant) {
        this.applicants.add(applicant);
        applicant.setAddress(this);
        return this;
    }

    public Address removeApplicant(Applicant applicant) {
        this.applicants.remove(applicant);
        applicant.setAddress(null);
        return this;
    }

    public void setApplicants(Set<Applicant> applicants) {
        this.applicants = applicants;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", street='" + getStreet() + "'" +
            ", houseNumber='" + getHouseNumber() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", residence='" + getResidence() + "'" +
            "}";
    }
}
