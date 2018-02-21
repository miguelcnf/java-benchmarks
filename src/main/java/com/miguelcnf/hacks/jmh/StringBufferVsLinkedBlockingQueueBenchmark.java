package com.miguelcnf.hacks.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Threads(value = 4)
public class StringBufferVsLinkedBlockingQueueBenchmark {

    @Benchmark
    public void concatenateWithStringBuffer(Blackhole blackhole) {
        StringBuffer buffer = new StringBuffer();

        for (Integer i=0; i<1000; i++) {
            buffer.append(i.toString());
        }

        blackhole.consume(buffer.toString());
    }

    @Benchmark
    public void concatenateWithLinkedBlockingQueue(Blackhole blackhole) {
        Queue<String> queue = new LinkedBlockingQueue<>();

        for (Integer i=0; i<1000; i++) {
            queue.add(i.toString());
        }

        blackhole.consume(queue.parallelStream().collect(Collectors.joining()));
    }
}
