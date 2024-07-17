package ru.introguzzle.mathparser.expression;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

public class MathExpression implements Expression, Serializable {

    private static class Char {
        private static boolean isAllowed(char c) {
            return Character.isLetter(c) || c == '_';
        }
    }

    private final String string;
    private int cursor;

    public MathExpression(@NotNull String string) {
        this(string, true);
    }

    public MathExpression(@NotNull String string, boolean compact) {
        this.string = compact ? string.strip().replace(" ", "") : string;
    }

    public int getLength() {
        return this.string.length();
    }

    @Override
    public char current() {
        return this.string.charAt(this.cursor);
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
        return this.cursor + 1 < this.getLength();
    }

    @Override
    public char next() {
        return this.string.charAt(this.cursor++);
    }

    @Override
    public boolean hasNext() {
        return this.cursor < this.getLength();
    }

    @Override
    public Expression reset() {
        this.cursor = 0;
        return this;
    }

    @Override
    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getCursor() {
        return this.cursor;
    }

    @Override
    public String getString() {
        return this.string;
    }

    @Override
    public char peekNext() {
        return this.string.charAt(this.cursor + 1);
    }

    @Override
    public String toString() {
        return this.string;
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
        return cursor == that.cursor && Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string, cursor);
    }
}
