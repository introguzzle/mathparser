package ru.introguzzle.mathparser.common.primitive;

public abstract sealed class PrimitiveReference<T> permits
        IntegerReference,
        BooleanReference,
        DoubleReference,
        LongReference {
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
