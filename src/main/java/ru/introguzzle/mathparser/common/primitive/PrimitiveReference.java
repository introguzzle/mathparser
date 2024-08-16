package ru.introguzzle.mathparser.common.primitive;

public class PrimitiveReference<T> {
    private T value;
    public PrimitiveReference(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
