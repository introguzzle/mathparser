package ru.introguzzle.mathparser.common.primitive;

public class LongReference extends PrimitiveReference<Long> {
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
