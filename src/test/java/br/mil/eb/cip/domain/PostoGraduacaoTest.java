package br.mil.eb.cip.domain;

import static br.mil.eb.cip.domain.MilitarTestSamples.*;
import static br.mil.eb.cip.domain.PostoGraduacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.mil.eb.cip.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PostoGraduacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostoGraduacao.class);
        PostoGraduacao postoGraduacao1 = getPostoGraduacaoSample1();
        PostoGraduacao postoGraduacao2 = new PostoGraduacao();
        assertThat(postoGraduacao1).isNotEqualTo(postoGraduacao2);

        postoGraduacao2.setId(postoGraduacao1.getId());
        assertThat(postoGraduacao1).isEqualTo(postoGraduacao2);

        postoGraduacao2 = getPostoGraduacaoSample2();
        assertThat(postoGraduacao1).isNotEqualTo(postoGraduacao2);
    }

    @Test
    void militarTest() throws Exception {
        PostoGraduacao postoGraduacao = getPostoGraduacaoRandomSampleGenerator();
        Militar militarBack = getMilitarRandomSampleGenerator();

        postoGraduacao.addMilitar(militarBack);
        assertThat(postoGraduacao.getMilitars()).containsOnly(militarBack);
        assertThat(militarBack.getPosto()).isEqualTo(postoGraduacao);

        postoGraduacao.removeMilitar(militarBack);
        assertThat(postoGraduacao.getMilitars()).doesNotContain(militarBack);
        assertThat(militarBack.getPosto()).isNull();

        postoGraduacao.militars(new HashSet<>(Set.of(militarBack)));
        assertThat(postoGraduacao.getMilitars()).containsOnly(militarBack);
        assertThat(militarBack.getPosto()).isEqualTo(postoGraduacao);

        postoGraduacao.setMilitars(new HashSet<>());
        assertThat(postoGraduacao.getMilitars()).doesNotContain(militarBack);
        assertThat(militarBack.getPosto()).isNull();
    }
}
