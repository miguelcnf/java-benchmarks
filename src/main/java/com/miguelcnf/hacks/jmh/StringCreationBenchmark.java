package com.miguelcnf.hacks.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class StringCreationBenchmark {

    @Benchmark
    public void createStringLiteral(Blackhole blackhole) {
        String result = "Foo";

        blackhole.consume(result);
    }

    @Benchmark
    public void createStringObject(Blackhole blackhole) {
        String result = new String("Foo");

        blackhole.consume(result);
    }
}
