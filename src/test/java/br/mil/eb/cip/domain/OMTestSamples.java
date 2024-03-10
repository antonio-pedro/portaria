package br.mil.eb.cip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OMTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OM getOMSample1() {
        return new OM().id(1L).nome("nome1").sigla("sigla1").codom(1);
    }

    public static OM getOMSample2() {
        return new OM().id(2L).nome("nome2").sigla("sigla2").codom(2);
    }

    public static OM getOMRandomSampleGenerator() {
        return new OM()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .sigla(UUID.randomUUID().toString())
            .codom(intCount.incrementAndGet());
    }
}
