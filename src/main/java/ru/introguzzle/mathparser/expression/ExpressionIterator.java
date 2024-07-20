package ru.introguzzle.mathparser.expression;

import java.util.Iterator;

public class ExpressionIterator implements Iterator<Character> {

    private final Expression expression;
    private int cursor = 0;

    public int getCursor() {
        return cursor;
    }

    private static class Char {
        private static boolean isLetter(Character c) {
            if (c == null) {
                return false;
            }

            return c == '_' || Character.isLetter(c);
        }

        private static boolean isDigit(Character c) {
            if (c == null) {
                return false;
            }

            return c == '.' || Character.isDigit(c);
        }
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

    public boolean isDigit() {
        return Char.isDigit(at(cursor));
    }

    public boolean isLetter() {
        return Char.isLetter(at(cursor));
    }

    public boolean isNextDigit() {
        if (!hasNext()) {
            return false;
        }

        return Char.isDigit(peekNext());
    }

    public boolean isNextLetter() {
        if (!hasNext()) {
            return false;
        }

        return Char.isLetter(peekNext());
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
