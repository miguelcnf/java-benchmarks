# java-benchmarks
Set of java benchmarks for my own pleasure.

#### Disclaimer
Do not assume that these benchmarks are correct, most likely they're not.

Feel free to think about them, point issues or suggest fixes.

I'll appreciate that.

### Compile

```
mvn clean package
```

### Run all benchmarks

```
java -jar target/benchmarks.jar
```

### Run single benchmark

```
java -jar target/benchmarks.jar StringCreation*
```

### Benchmark options

Check [JMH](http://openjdk.java.net/projects/code-tools/jmh/) options with:

```
java -jar target/benchmarks.jar  -h
```
