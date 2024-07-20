package ru.introguzzle.mathparser.common;

public interface Optionable<O extends Options> {
    void setOptions(O options);
    O getOptions();
}
