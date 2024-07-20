package ru.introguzzle.mathparser.symbol;

import ru.introguzzle.mathparser.common.Nameable;

public interface Symbol<T extends Number> extends Nameable {
    T getValue();
}
