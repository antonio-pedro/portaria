package br.mil.eb.cip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PostoGraduacaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PostoGraduacao getPostoGraduacaoSample1() {
        return new PostoGraduacao().id(1L).descricao("descricao1").sigla("sigla1");
    }

    public static PostoGraduacao getPostoGraduacaoSample2() {
        return new PostoGraduacao().id(2L).descricao("descricao2").sigla("sigla2");
    }

    public static PostoGraduacao getPostoGraduacaoRandomSampleGenerator() {
        return new PostoGraduacao()
            .id(longCount.incrementAndGet())
            .descricao(UUID.randomUUID().toString())
            .sigla(UUID.randomUUID().toString());
    }
}
