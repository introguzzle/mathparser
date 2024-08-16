package ru.introguzzle.mathparser.generate;

import ru.introguzzle.mathparser.common.options.Optionable;
import ru.introguzzle.mathparser.expression.Expression;

public interface Generator<T extends Expression> extends Optionable<GeneratorOptions> {
    T generate();
}
