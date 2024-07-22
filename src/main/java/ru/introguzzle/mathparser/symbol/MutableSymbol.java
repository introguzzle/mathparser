package ru.introguzzle.mathparser.symbol;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public abstract class MutableSymbol implements Symbol<Double>, Serializable {

    @Serial
    private static final long serialVersionUID = 282184201759219L;
    private final String name;
    private transient double value;

    public MutableSymbol(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double getValue() {
        return value;
    }

    public MutableSymbol setValue(double value) {
        this.value = value;
        return this;
    }

    @Override
    public MutableSymbol clone() throws CloneNotSupportedException {
        return (MutableSymbol) super.clone();
    }

    @Override
    public String toString() {
        return describe() + "{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableSymbol mutableSymbol = (MutableSymbol) o;
        return Double.compare(value, mutableSymbol.value) == 0
                && Objects.equals(name, mutableSymbol.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
