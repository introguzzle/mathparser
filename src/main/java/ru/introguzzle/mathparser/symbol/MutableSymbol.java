package ru.introguzzle.mathparser.symbol;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public abstract class MutableSymbol<N extends Number> implements Symbol<N>, Serializable {

    @Serial
    private static final long serialVersionUID = 282184201759219L;
    private final String name;
    private N value;

    public MutableSymbol(String name, N value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public N getValue() {
        return value;
    }

    public MutableSymbol<N> setValue(N value) {
        this.value = value;
        return this;
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
        MutableSymbol<?> mutableSymbol = (MutableSymbol<?>) o;
        return Objects.equals(name, mutableSymbol.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
