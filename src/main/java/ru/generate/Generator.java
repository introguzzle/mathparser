package ru.generate;

import ru.common.Optionable;

public interface Generator<T> extends Optionable<GeneratorOptions> {
    T generate();
}
