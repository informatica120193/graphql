package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Invoce;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Invoce entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoceRepository extends JpaRepository<Invoce, Long> {
}
