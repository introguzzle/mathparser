package ru.introguzzle.mathparser.symbol;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public abstract class ImmutableSymbol<T extends Number> implements
        Symbol<T>,
        Serializable {

    @Serial
    private static final long serialVersionUID = 282184201759219L;

    private final String name;
    private final T value;

    public ImmutableSymbol(String name, T value) {
        this.name = name;
        this.value = value;
    }
    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public T getValue() {
        return value;
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
        ImmutableSymbol<?> immutableSymbol = (ImmutableSymbol<?>) o;
        return Objects.equals(name, immutableSymbol.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
