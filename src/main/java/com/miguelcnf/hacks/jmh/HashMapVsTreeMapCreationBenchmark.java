package com.miguelcnf.hacks.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@State(value = Scope.Benchmark)
public class HashMapVsTreeMapCreationBenchmark {

    private final Map<String, String> srcMap = new HashMap<>();

    @Setup
    public void setup() {
        srcMap.put("b", "1");
        srcMap.put("a", "2");
        srcMap.put("c", "3");
    }

    @Benchmark
    public void hashMapCreation(Blackhole blackhole) {
        Map<String, String> hashMap = new HashMap<>();

        hashMap.put("b", "1");
        hashMap.put("a", "2");
        hashMap.put("c", "3");

        blackhole.consume(hashMap);
    }

    @Benchmark
    public void treeMapCreation(Blackhole blackhole) {
        Map<String, String> treeMap = new TreeMap<>();

        treeMap.put("b", "1");
        treeMap.put("a", "2");
        treeMap.put("c", "3");

        blackhole.consume(treeMap);
    }

    @Benchmark
    public void treeMapCreationFromHashMap(Blackhole blackhole) {
        Map<String, String> treeMap = new TreeMap<>(srcMap);

        blackhole.consume(treeMap);
    }


}
