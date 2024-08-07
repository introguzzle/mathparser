package ru.introguzzle.mathparser.expression;

import java.util.Iterator;

public class ExpressionIterator implements Iterator<Character> {
    private final Expression expression;
    private int cursor = 0;

    public int getCursor() {
        return cursor;
    }

    public ExpressionIterator(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean hasNext() {
        return cursor < expression.getLength();
    }

    @Override
    public Character next() {
        return at(cursor++);
    }

    public Character peekNext() {
        if (!hasNext()) {
            return null;
        }

        return at(cursor + 1);
    }

    public Character at(int index) {
        if (index < expression.getLength()) {
            return expression.getString().charAt(index);
        }

        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public char current() {
        return at(cursor);
    }
}
