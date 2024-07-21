package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Token implements Serializable, Comparable<Token> {

    @Serial
    private static final long serialVersionUID = -54892981192L;
    private final Type type;
    private final String data;

    public Token(Type type, CharSequence data) {
        this.type = type;
        this.data = data.toString();
    }

    public Token(Type type, Character data) {
        this.type = type;
        this.data = data.toString();
    }

    public Type getType() {
        return this.type;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + getType() + ", data= '" + getData() + "'}";
    }

    @Override
    public int compareTo(@NotNull Token o) {
        return this.data.compareTo(o.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data, this.type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type && data.equals(token.data);
    }

    public boolean typeEquals(Token anotherToken) {
        return this.type == anotherToken.type;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
