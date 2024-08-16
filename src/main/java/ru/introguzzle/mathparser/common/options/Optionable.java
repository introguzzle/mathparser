package ru.introguzzle.mathparser.common.options;

public interface Optionable<O extends Options> {
    void setOptions(O options);
    O getOptions();
}
