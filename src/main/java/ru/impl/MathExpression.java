package ru.impl;

import org.jetbrains.annotations.NotNull;
import ru.contract.Expression;
import ru.variable.Variable;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MathExpression implements Expression, Serializable {

    @Serial
    private static final long serialVersionUID = -121848309212132L;

    @Override
    public int compareTo(@NotNull Expression o) {
        return this.getString().compareTo(o.getString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MathExpression that = (MathExpression) o;
        return position == that.position && Objects.equals(string, that.string) && Objects.equals(variables, that.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string, position, variables);
    }

    private static class Char {
        private static boolean isAllowed(char c) {
            return Character.isLetter(c) || c == '_';
        }
    }

    private final String string;
    private int position;
    private final List<Variable> variables;

    public MathExpression(@NotNull String string) {
        this(string, new ArrayList<>(), true);
    }

    public MathExpression(@NotNull String string, List<Variable> variables) {
        this(string, variables, true);
    }

    public MathExpression(String string, List<Variable> variables, boolean strip) {
        this.string = strip ? string.strip().replace(" ", "") : string;
        this.position = 0;
        this.variables = variables;
    }

    public int getLength() {
        return this.string.length();
    }

    @Override
    public char current() {
        return this.string.charAt(this.position);
    }

    @Override
    public List<Variable> getSymbols() {
        return this.variables;
    }

    @Override
    public boolean isCurrentDigit() {
        return Character.isDigit(this.current());
    }

    @Override
    public boolean isCurrentLetter() {
        return Char.isAllowed(this.current());
    }

    @Override
    public boolean isNextDigit() {
        return this.hasAhead() && Character.isDigit(this.peekNext());
    }

    @Override
    public boolean isNextLetter() {
        return this.hasAhead() && Char.isAllowed(this.peekNext());
    }

    public boolean hasAhead() {
        return this.position + 1 < this.getLength();
    }

    @Override
    public char next() {
        return this.string.charAt(this.position++);
    }

    @Override
    public boolean hasNext() {
        return this.position < this.getLength();
    }

    @Override
    public Expression reset() {
        this.position = 0;
        return this;
    }

    @Override
    public int getPosition() {
        return this.position;
    }

    @Override
    public String getString() {
        return this.string;
    }

    @Override
    public char peekNext() {
        return this.string.charAt(this.position + 1);
    }

    @Override
    public String toString() {
        return this.string;
    }

    public Expression join(Expression... expressions) {
        StringBuilder sb = new StringBuilder(this.string);

        for (Expression e: expressions) {
            sb.append(e.getString());
        }

        return new MathExpression(sb.toString());
    }

    @NotNull
    @Override
    public Iterator<Character> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return MathExpression.this.hasNext();
            }

            @Override
            public Character next() {
                return MathExpression.this.next();
            }
        };
    }
}
