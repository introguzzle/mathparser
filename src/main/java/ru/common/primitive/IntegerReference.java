package ru.common.primitive;

public non-sealed class IntegerReference extends PrimitiveReference<Integer> {
    public IntegerReference(Integer value) {
        super(value);
    }

    public void increment() {
        setValue(getValue() + 1);
    }

    public void decrement() {
        setValue(getValue() - 1);
    }
}
