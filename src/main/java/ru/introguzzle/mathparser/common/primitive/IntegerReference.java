package ru.introguzzle.mathparser.common.primitive;

public class IntegerReference extends PrimitiveReference<Integer> {
    public IntegerReference(Integer value) {
        super(value);
    }

    public void increment() {
        setValue(getValue() + 1);
    }

    public void decrement() {
        setValue(getValue() - 1);
    }

    public int decrementAndGet() {
        decrement();
        return getValue();
    }

    public int getAndDecrement() {
        int oldValue = getValue();
        decrement();
        return oldValue;
    }

    public int incrementAndGet() {
        increment();
        return getValue();
    }

    public int getAndIncrement() {
        int oldValue = getValue();
        increment();
        return oldValue;
    }

    @Override
    public String toString() {
        return getValue() + "";
    }
}
