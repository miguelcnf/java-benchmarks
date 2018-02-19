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
public class HashMapVsTreeMapToStringBenchmark {

    private final Map<String, String> hashMap = new HashMap<>();
    private final Map<String, String> treeMap = new TreeMap<>();

    @Setup
    public void setup() {
        hashMap.put("b", "1");
        hashMap.put("a", "2");
        hashMap.put("c", "3");

        treeMap.put("b", "1");
        treeMap.put("a", "2");
        treeMap.put("c", "3");
    }

    @Benchmark
    public void toStringHashMap(Blackhole blackhole) {
        blackhole.consume(hashMap.toString());
    }

    @Benchmark
    public void toStringTreeMap(Blackhole blackhole) {
        blackhole.consume(treeMap.toString());
    }
}
