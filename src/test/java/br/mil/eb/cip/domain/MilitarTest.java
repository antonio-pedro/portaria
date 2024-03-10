package br.mil.eb.cip.domain;

import static br.mil.eb.cip.domain.MilitarTestSamples.*;
import static br.mil.eb.cip.domain.OMTestSamples.*;
import static br.mil.eb.cip.domain.PostoGraduacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.mil.eb.cip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MilitarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Militar.class);
        Militar militar1 = getMilitarSample1();
        Militar militar2 = new Militar();
        assertThat(militar1).isNotEqualTo(militar2);

        militar2.setId(militar1.getId());
        assertThat(militar1).isEqualTo(militar2);

        militar2 = getMilitarSample2();
        assertThat(militar1).isNotEqualTo(militar2);
    }

    @Test
    void postoTest() throws Exception {
        Militar militar = getMilitarRandomSampleGenerator();
        PostoGraduacao postoGraduacaoBack = getPostoGraduacaoRandomSampleGenerator();

        militar.setPosto(postoGraduacaoBack);
        assertThat(militar.getPosto()).isEqualTo(postoGraduacaoBack);

        militar.posto(null);
        assertThat(militar.getPosto()).isNull();
    }

    @Test
    void omTest() throws Exception {
        Militar militar = getMilitarRandomSampleGenerator();
        OM oMBack = getOMRandomSampleGenerator();

        militar.setOm(oMBack);
        assertThat(militar.getOm()).isEqualTo(oMBack);

        militar.om(null);
        assertThat(militar.getOm()).isNull();
    }
}
