package ru.introguzzle.generate;

import ru.introguzzle.common.Optionable;

public interface Generator<T> extends Optionable<GeneratorOptions> {
    T generate();
}
