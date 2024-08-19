package ru.introguzzle.mathparser.generate;

import ru.introguzzle.mathparser.common.options.Configurable;
import ru.introguzzle.mathparser.expression.Expression;

public interface Generator<T extends Expression> extends Configurable<GeneratorOptions> {
    T generate();
}
