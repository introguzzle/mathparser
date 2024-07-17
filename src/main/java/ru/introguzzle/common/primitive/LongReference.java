package ru.introguzzle.common.primitive;

public non-sealed class LongReference extends PrimitiveReference<Long> {
    public LongReference(Long value) {
        super(value);
    }

    public void increment() {
        setValue(getValue() + 1);
    }

    public void decrement() {
        setValue(getValue() - 1);
    }
}
