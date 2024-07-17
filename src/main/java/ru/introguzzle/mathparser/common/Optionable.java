package ru.introguzzle.mathparser.common;

public interface Optionable<T extends Options> {
    void setOptions(T options);
    T getOptions();
}
