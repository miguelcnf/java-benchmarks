package com.miguelcnf.hacks.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import java.util.regex.Pattern;

public class StringMatch {

    @SuppressWarnings("all")
    @Benchmark
    public void startsWith(Blackhole blackhole) {
        String subject = "something.to.match";

        blackhole.consume(subject.startsWith("something.to."));

        blackhole.consume(subject);
    }

    private static final Pattern pattern = Pattern.compile("something\\.to\\..*");

    @SuppressWarnings("all")
    @Benchmark
    public void patternMatch(Blackhole blackhole) {
        String subject = "something.to.match";

        blackhole.consume(pattern.matcher(subject).matches());

        blackhole.consume(subject);
    }
}
