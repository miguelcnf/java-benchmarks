package com.miguelcnf.hacks.jmh;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@State(value = Scope.Benchmark)
public class JsonSmileKryoSerializationBenchmark {

    private static final String PATTERN = "test.{name}.metric,client={client},context=demo,tag={tag} 100 {date}";
    private List<Domain> domains;

    private static final ObjectMapper jsonSerializerMapper = new ObjectMapper();
    private static final ObjectWriter jsonSerializerWriter = new ObjectMapper().writer();
    private static final ObjectMapper jsonSerializerAfterBurnerMapper = new ObjectMapper().registerModule(new AfterburnerModule());
    private static final ObjectWriter jsonSerializerAfterBurnerWriter = new ObjectMapper().registerModule(new AfterburnerModule()).writer();
    private static final ObjectMapper jsonSerializerSmileMapper = new ObjectMapper(new SmileFactory());
    private static final ObjectWriter jsonSerializerSmileWriter = new ObjectMapper(new SmileFactory()).writer();
    private static final ObjectMapper jsonSerializerAfterBurnerSmileMapper = new ObjectMapper(new SmileFactory()).registerModule(new AfterburnerModule());
    private static final ObjectWriter jsonSerializerAfterBurnerSmileWriter = new ObjectMapper(new SmileFactory()).registerModule(new AfterburnerModule()).writer();

    /**
     * When using in a multi threaded environment we should wrap this in a threadlocal
     */
    private static final Kryo kryo = new Kryo();
    private static final CollectionSerializer serializer = new CollectionSerializer();

    static {
        serializer.setElementClass(Domain.class, new Serializer<JsonSmileKryoSerializationBenchmark.Domain>() {
            @Override
            public void write(Kryo kryo, Output output, Domain object) {
                output.writeString(object.getMetric());
                output.writeString(object.getToken());
            }

            @Override
            public Domain read(Kryo kryo, Input input, Class<JsonSmileKryoSerializationBenchmark.Domain> type) {
                return new Domain(input.readString(), input.readString());
            }
        });
    }

    @Setup
    public void setup() {
        domains = new ArrayList<>();
        String uuid = UUID.randomUUID().toString();
        for (int i = 0; i <= 10000; i++) {
            String metric = PATTERN.replace("{name}", Long.toHexString(Double.doubleToLongBits(Math.random())));
            metric = metric.replace("{client}", Long.toHexString(Double.doubleToLongBits(Math.random())));
            metric = metric.replace("{tag}", Long.toHexString(Double.doubleToLongBits(Math.random())));
            metric = metric.replace("{date}", String.valueOf(System.currentTimeMillis()));
            domains.add(new Domain(metric, uuid));
        }
    }

    @Benchmark
    public void jacksonJsonSerializationObjectMapper(Blackhole blackhole) throws JsonProcessingException {
        String serializedResult = jsonSerializerMapper.writeValueAsString(domains);
        blackhole.consume(serializedResult);
    }

    @Benchmark
    public void jacksonJsonSerializationObjectWriter(Blackhole blackhole) throws JsonProcessingException {
        String serializedResult = jsonSerializerWriter.writeValueAsString(domains);
        blackhole.consume(serializedResult);
    }

    @Benchmark
    public void jacksonAfterBurnerSerializationObjectMapper(Blackhole blackhole) throws JsonProcessingException {
        String serializedResult = jsonSerializerAfterBurnerMapper.writeValueAsString(domains);
        blackhole.consume(serializedResult);
    }

    @Benchmark
    public void jacksonAfterBurnerSerializationObjectWriter(Blackhole blackhole) throws JsonProcessingException {
        String serializedResult = jsonSerializerAfterBurnerWriter.writeValueAsString(domains);
        blackhole.consume(serializedResult);
    }

    @Benchmark
    public void jacksonSmileSerializationObjectMapper(Blackhole blackhole) throws JsonProcessingException {
        byte[] serializedResult = jsonSerializerSmileMapper.writeValueAsBytes(domains);
        blackhole.consume(serializedResult);
    }

    @Benchmark
    public void jacksonSmileSerializationObjectWriter(Blackhole blackhole) throws JsonProcessingException {
        byte[] serializedResult = jsonSerializerSmileWriter.writeValueAsBytes(domains);
        blackhole.consume(serializedResult);
    }

    @Benchmark
    public void jacksonSmileSerializationAfterBurnerObjectMapper(Blackhole blackhole) throws JsonProcessingException {
        byte[] serializedResult = jsonSerializerAfterBurnerSmileMapper.writeValueAsBytes(domains);
        blackhole.consume(serializedResult);
    }

    @Benchmark
    public void jacksonSmileSerializationAfterBurnerObjectWriter(Blackhole blackhole) throws IOException {
        byte[] serializedResult = jsonSerializerAfterBurnerSmileWriter.writeValueAsBytes(domains);
        blackhole.consume(serializedResult);
    }

    /**
     * I must be doing something wrong since this is far slower then expected
     */
    @Benchmark
    public void kryoSerialization(Blackhole blackhole) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        serializer.write(kryo, output, domains);
        blackhole.consume(output.getBuffer());
    }


    @SuppressWarnings("unused")
    private static final class Domain {
        private String metric;
        private String token;

        public Domain() {

        }

        public Domain(String metric, String token) {
            this.metric = metric;
            this.token = token;
        }

        public void setMetric(String metric) {
            this.metric = metric;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMetric() {
            return metric;
        }

        public String getToken() {
            return token;
        }

        @Override
        public String toString() {
            return "Domain{" + "metric='" + metric + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }
}
