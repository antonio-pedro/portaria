package br.mil.eb.cip.domain;

import static br.mil.eb.cip.domain.MilitarTestSamples.*;
import static br.mil.eb.cip.domain.OMTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.mil.eb.cip.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OMTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OM.class);
        OM oM1 = getOMSample1();
        OM oM2 = new OM();
        assertThat(oM1).isNotEqualTo(oM2);

        oM2.setId(oM1.getId());
        assertThat(oM1).isEqualTo(oM2);

        oM2 = getOMSample2();
        assertThat(oM1).isNotEqualTo(oM2);
    }

    @Test
    void militarTest() throws Exception {
        OM oM = getOMRandomSampleGenerator();
        Militar militarBack = getMilitarRandomSampleGenerator();

        oM.addMilitar(militarBack);
        assertThat(oM.getMilitars()).containsOnly(militarBack);
        assertThat(militarBack.getOm()).isEqualTo(oM);

        oM.removeMilitar(militarBack);
        assertThat(oM.getMilitars()).doesNotContain(militarBack);
        assertThat(militarBack.getOm()).isNull();

        oM.militars(new HashSet<>(Set.of(militarBack)));
        assertThat(oM.getMilitars()).containsOnly(militarBack);
        assertThat(militarBack.getOm()).isEqualTo(oM);

        oM.setMilitars(new HashSet<>());
        assertThat(oM.getMilitars()).doesNotContain(militarBack);
        assertThat(militarBack.getOm()).isNull();
    }
}
