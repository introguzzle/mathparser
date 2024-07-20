package ru.introguzzle.mathparser.expression;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class MathExpression implements Expression, Serializable {

    private final String string;

    public MathExpression(@NotNull String string) {
        this.string = string;
    }

    public int getLength() {
        return string.length();
    }

    @Override
    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }

    @NotNull
    @Override
    public ExpressionIterator iterator() {
        return new ExpressionIterator(this);
    }

    @Serial
    private static final long serialVersionUID = -121848309212132L;

    @Override
    public int compareTo(@NotNull Expression o) {
        return getString().compareTo(o.getString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MathExpression that = (MathExpression) o;
        return Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }
}
