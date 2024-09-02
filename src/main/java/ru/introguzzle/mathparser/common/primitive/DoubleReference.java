package ru.introguzzle.mathparser.common.primitive;

import java.util.Objects;

public final class DoubleReference {
    private double value;

    public DoubleReference(double value) {
        this.value = value;
    }

    public void increment() {
        setValue(value + 1);
    }

    public void decrement() {
        setValue(value - 1);
    }

    public double decrementAndGet() {
        decrement();
        return getValue();
    }

    public double getAndDecrement() {
        double oldValue = getValue();
        decrement();
        return oldValue;
    }

    public double incrementAndGet() {
        increment();
        return getValue();
    }

    public double getAndIncrement() {
        double oldValue = getValue();
        increment();
        return oldValue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleReference that = (DoubleReference) o;
        return Double.compare(value, that.value) == 0;
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
