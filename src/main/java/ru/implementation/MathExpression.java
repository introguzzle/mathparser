package ru.implementation;

import org.jetbrains.annotations.NotNull;
import ru.main.Expression;
import ru.variable.Variable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MathExpression implements Expression {

    private final String string;
    private int position;
    private final List<Variable> variables;

    public MathExpression(@NotNull String string) {
        this.string = string.strip().replace(" ", "");
        this.position = 0;
        this.variables = new ArrayList<>();
    }

    public MathExpression(@NotNull String string, List<Variable> variables) {
        this.string = string.strip().replace(" ", "");
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
        return Character.isLetter(this.current());
    }

    @Override
    public boolean isNextDigit() {
        return this.hasAhead() && Character.isDigit(this.peekNext());
    }

    @Override
    public boolean isNextLetter() {
        return this.hasAhead() && Character.isLetter(this.peekNext());
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
