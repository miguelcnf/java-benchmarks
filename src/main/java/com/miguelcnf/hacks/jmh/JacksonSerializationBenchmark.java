package com.miguelcnf.hacks.jmh;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;

@State(value = Scope.Benchmark)
public class JacksonSerializationBenchmark {

    private static final String TEST_STRING = "test";
    private List<String> list;
    private ListHolder listHolder;

    @Setup
    public void setup() {
        list = new ArrayList<String>(1000);
        for (int i = 0; i < 1000; i++) {
            list.add(TEST_STRING);
        }

        listHolder = new ListHolder();
    }

    @Benchmark
    public void JSONSerializeSingleListWith1000Objects(Blackhole blackhole) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String serializedResult = mapper.writeValueAsString(list);

        blackhole.consume(serializedResult);
    }

    @Benchmark
    public void JSONSerialize1000ListsWithSingleObject(Blackhole blackhole) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String serializedResult = mapper.writeValueAsString(listHolder);

        blackhole.consume(serializedResult);
    }

    private class ListHolder {
        private List<List<String>> lists = new ArrayList<List<String>>(1000);

        ListHolder() {
            for (int i = 0; i < 1000; i++) {
                List<String> list = new ArrayList<String>(1);
                list.add(TEST_STRING);

                lists.add(list);
            }
        }

        public List<List<String>> getLists() {
            return lists;
        }
    }
}
