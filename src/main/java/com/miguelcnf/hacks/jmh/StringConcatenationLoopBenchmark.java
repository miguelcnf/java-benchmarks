package com.miguelcnf.hacks.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class StringConcatenationLoopBenchmark {

    @Benchmark
    public void concatenateInline(Blackhole blackhole) {
        String result = "";

        for (int i=0; i<1000; i++) {
            result += i;
        }

        blackhole.consume(result);
    }

    @Benchmark
    public void concatenateWithStringBuilder(Blackhole blackhole) {
        StringBuilder builder = new StringBuilder();

        for (int i=0; i<1000; i++) {
            builder.append(i);
        }

        blackhole.consume(builder.toString());
    }

    @Benchmark
    public void concatenateWithStringBuffer(Blackhole blackhole) {
        StringBuffer buffer = new StringBuffer();

        for (int i=0; i<1000; i++) {
            buffer.append(i);
        }

        blackhole.consume(buffer.toString());
    }
}
