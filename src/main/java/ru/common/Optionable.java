package ru.common;

public interface Optionable<T extends Options> {
    void setOptions(T options);
    T getOptions();
}
