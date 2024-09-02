package ru.introguzzle.mathparser.operator;

import org.intellij.lang.annotations.MagicConstant;

@FunctionalInterface
public interface Priorable {
    @MagicConstant(valuesFromClass = Priorities.class)
    int getPriority();
}
