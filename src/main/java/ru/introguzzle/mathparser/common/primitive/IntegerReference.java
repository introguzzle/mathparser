package ru.introguzzle.mathparser.common.primitive;

import java.util.Objects;

public final class IntegerReference {
    private int value;

    public IntegerReference(int value) {
        this.value = value;
    }

    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }

    public int decrementAndGet() {
        return --value;
    }

    public int getAndDecrement() {
        return value--;
    }

    public int incrementAndGet() {
        return ++value;
    }

    public int getAndIncrement() {
        return value++;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegerReference that)) return false;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return getValue() + "";
    }
}
