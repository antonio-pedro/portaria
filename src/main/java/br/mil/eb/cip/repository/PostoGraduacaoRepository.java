package br.mil.eb.cip.repository;

import br.mil.eb.cip.domain.PostoGraduacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostoGraduacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostoGraduacaoRepository extends JpaRepository<PostoGraduacao, Long> {}
