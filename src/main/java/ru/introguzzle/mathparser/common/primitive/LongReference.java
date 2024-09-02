package ru.introguzzle.mathparser.common.primitive;

import java.util.Objects;

public final class LongReference {
    private long value;

    public LongReference(long value) {
        this.value = value;
    }

    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }

    public long decrementAndGet() {
        return --value;
    }

    public long getAndDecrement() {
        return value--;
    }

    public long incrementAndGet() {
        return ++value;
    }

    public long getAndIncrement() {
        return value++;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongReference that = (LongReference) o;
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
