# jmh-benchmark-text-scanning

A benchmark to compare different approaches for reading integers from a file:

- `Scanner(File source)` Scanner constructed based on FileReader
- `Scanner` constructed using `Scanner(Readable source)` constructor where `Readable` is `FileReader`
- `Scanner` constructed using `Scanner(Readable source)` constructor where `Readable` is `FileReader` wrapped in `BufferedReader`
- a bit reworked version of `MyScanner` class (original one is published on [codeforces](http://codeforces.com/blog/entry/7018))