package ru.contract.generator;

public interface Generator<T> {
    T generate();
    Generator<? extends T> setOptions(GeneratorOptions options);
}
