package ru.introguzzle.symbol;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public abstract class ImmutableSymbol implements Symbol<Double>, Serializable {

    @Serial
    private static final long serialVersionUID = 282184201759219L;

    private final String name;
    private final transient double value;

    public ImmutableSymbol(String name, double value) {
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

    @Override
    public String toString() {
        return "Variable{" +
                "representation='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableSymbol immutableSymbol = (ImmutableSymbol) o;
        return Double.compare(value, immutableSymbol.value) == 0
                && Objects.equals(name, immutableSymbol.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
