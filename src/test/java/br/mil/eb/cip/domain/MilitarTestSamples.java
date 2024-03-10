package br.mil.eb.cip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MilitarTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Militar getMilitarSample1() {
        return new Militar()
            .id(1L)
            .identidade(1)
            .cpf(1)
            .postoGraduacao("postoGraduacao1")
            .nome("nome1")
            .nomeGuerra("nomeGuerra1")
            .email("email1");
    }

    public static Militar getMilitarSample2() {
        return new Militar()
            .id(2L)
            .identidade(2)
            .cpf(2)
            .postoGraduacao("postoGraduacao2")
            .nome("nome2")
            .nomeGuerra("nomeGuerra2")
            .email("email2");
    }

    public static Militar getMilitarRandomSampleGenerator() {
        return new Militar()
            .id(longCount.incrementAndGet())
            .identidade(intCount.incrementAndGet())
            .cpf(intCount.incrementAndGet())
            .postoGraduacao(UUID.randomUUID().toString())
            .nome(UUID.randomUUID().toString())
            .nomeGuerra(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
