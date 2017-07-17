package com.miguelcnf.hacks.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;

public class ListCreationBenchmark {

    private static final String TEST_STRING = "test";

    @Benchmark
    public void createSingleListWith1000Objects(Blackhole blackhole) {
        List<String> list = new ArrayList<String>(1000);

        for (int i=0; i<1000; i++) {
            list.add(TEST_STRING);
        }

        blackhole.consume(list);
    }

    @Benchmark
    public void create1000ListsWithSingleObject(Blackhole blackhole) {
        for (int i = 0; i < 1000; i++) {
            List<String> list = new ArrayList<String>(1);

            list.add(TEST_STRING);

            blackhole.consume(list);
        }
    }
}
