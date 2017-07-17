package com.miguelcnf.hacks.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class StreamDistinctFilterBenchmark {

    @State(Scope.Thread)
    public static class MyState {
        private final String TEST_STRING = "test";
        private final Pattern PATTERN = Pattern.compile("^" + TEST_STRING + "1");
        private List<String> UNIQUE_STRINGS;
        private List<String> DUPLICATED_STRINGS;

        @Setup(Level.Iteration)
        public void createListWith1000DuplicatedStrings() {
            List<String> variables = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
            List<String> list = new ArrayList<>(1000);

            for (int i = 0; i < 1000; i++) {
                list.add(TEST_STRING + variables.parallelStream().findAny().orElse("rand"));
            }

            DUPLICATED_STRINGS = list;
        }

        @Setup(Level.Trial)
        public void createListWith1000UniqueStrings() {
            List<String> list = new ArrayList<>(1000);

            for (int i = 0; i < 1000; i++) {
                list.add(TEST_STRING + i);
            }

            UNIQUE_STRINGS = list;
        }
    }

    @Benchmark
    public void distinctFirstUnique(MyState myState, Blackhole blackhole) {
        myState.UNIQUE_STRINGS.stream()
                .distinct()
                .filter(string -> myState.PATTERN.matcher(string).find())
                .forEach(blackhole::consume);
    }

    @Benchmark
    public void filterFirstUnique(MyState myState, Blackhole blackhole) {
        myState.UNIQUE_STRINGS.stream()
                .filter(string -> myState.PATTERN.matcher(string).find())
                .distinct()
                .forEach(blackhole::consume);
    }

    @Benchmark
    public void distinctFirstDuplicated(MyState myState, Blackhole blackhole) {
        myState.DUPLICATED_STRINGS.stream()
                .distinct()
                .filter(string -> myState.PATTERN.matcher(string).find())
                .forEach(blackhole::consume);
    }

    @Benchmark
    public void filterFirstDuplicated(MyState myState, Blackhole blackhole) {
        myState.DUPLICATED_STRINGS.stream()
                .filter(string -> myState.PATTERN.matcher(string).find())
                .distinct()
                .forEach(blackhole::consume);
    }
}
