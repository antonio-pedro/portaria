package br.mil.eb.cip.repository;

import br.mil.eb.cip.domain.OM;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OM entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OMRepository extends JpaRepository<OM, Long> {}
