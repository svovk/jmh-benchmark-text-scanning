package com.sergeivovk.jmh.textscanning;

import com.sergeivovk.io.MyScanner;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class TextScanBenchmark {

    private static final int DATA_SET_SAMPLE_COUNT = 4096;

    @State(Scope.Thread)
    public static class BenchmarkState {
        File file;

        @Setup(Level.Iteration)
        public void setUp() throws IOException {

            file = File.createTempFile("TextScanBenchmark", ".in");
            Random random = new Random();
            try (OutputStreamWriter fw = new FileWriter(file)) {
                for (int i = 0; i < DATA_SET_SAMPLE_COUNT; i++){
                    fw.append(Long.toString(random.nextLong()));
                    if (random.nextBoolean()) {
                        fw.append("\n");
                    }else{
                        fw.append(" ");
                    }
                }
            }
        }
    }

    @Benchmark
    @OperationsPerInvocation(DATA_SET_SAMPLE_COUNT)
    public void mesureScanner(Blackhole blackhole, BenchmarkState state) throws IOException {
        try (Scanner scanner = new Scanner(state.file)) {
            consume(scanner, blackhole, DATA_SET_SAMPLE_COUNT);
        }
    }

    @Benchmark
    @OperationsPerInvocation(DATA_SET_SAMPLE_COUNT)
    public void mesureFileReaderScanner(Blackhole blackhole, BenchmarkState state) throws IOException {
        try (Scanner scanner = new Scanner(new FileReader(state.file))) {
            consume(scanner, blackhole, DATA_SET_SAMPLE_COUNT);
        }
    }

    @Benchmark
    @OperationsPerInvocation(DATA_SET_SAMPLE_COUNT)
    public void mesureBufferedFileReaderScanner(Blackhole blackhole, BenchmarkState state) throws IOException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(state.file)))) {
            consume(scanner, blackhole, DATA_SET_SAMPLE_COUNT);
        }
    }

    @Benchmark
    @OperationsPerInvocation(DATA_SET_SAMPLE_COUNT)
    public void mesureMyScanner(Blackhole blackhole, BenchmarkState state) throws IOException {
        try (MyScanner myScanner = new MyScanner(new FileReader(state.file))) {
            consume(myScanner, blackhole, DATA_SET_SAMPLE_COUNT);
        }
    }

    private void consume(MyScanner scanner, Blackhole blackhole, int count) throws IOException {
        for (int i=0; i < count; i++){
            blackhole.consume(scanner.nextLong());
        }
    }

    private void consume(Scanner scanner, Blackhole blackhole, int count) throws IOException {
        for (int i=0; i < count; i++){
            blackhole.consume(scanner.nextLong());
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + TextScanBenchmark.class.getSimpleName() + ".*")
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}