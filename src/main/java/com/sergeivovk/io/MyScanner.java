package com.sergeivovk.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

public class MyScanner implements Closeable{
    private BufferedReader bufferedReader;
    private StringTokenizer stringTokenizer;

    public MyScanner(Reader reader) {
        bufferedReader = new BufferedReader(reader);
    }

    public String nextString() {
        while (stringTokenizer == null || !stringTokenizer.hasMoreElements()) {
            try {
                stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            } catch (IOException e) {
                throw new RuntimeException("Unable to read next element", e);
            }
        }
        return stringTokenizer.nextToken();
    }

    public int nextInt() {
        return Integer.parseInt(nextString());
    }

    public long nextLong() {
        return Long.parseLong(nextString());
    }

    public double nextDouble() {
        return Double.parseDouble(nextString());
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close();
    }
}