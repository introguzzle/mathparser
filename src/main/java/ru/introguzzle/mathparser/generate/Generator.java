package ru.introguzzle.mathparser.generate;

import ru.introguzzle.mathparser.common.Optionable;

public interface Generator<T> extends Optionable<GeneratorOptions> {
    T generate();
}
