package ru.introguzzle.mathparser.tokenize.token;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class SimpleToken implements
        Token,
        Serializable,
        Comparable<Token> {

    @Serial
    private static final long serialVersionUID = -54892981192L;
    private final Type type;
    private final String data;

    public SimpleToken(Type type, CharSequence data) {
        this.type = type;
        this.data = data.toString();
    }

    public SimpleToken(Type type, Character data) {
        this.type = type;
        this.data = data.toString();
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + getType() + ", data= '" + getData() + "'}";
    }

    @Override
    public int compareTo(@NotNull Token token) {
        return getData().compareTo(token.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data, this.type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleToken token = (SimpleToken) o;
        return type == token.type && data.equals(token.data);
    }

    @Override
    protected SimpleToken clone() throws CloneNotSupportedException {
        return (SimpleToken) super.clone();
    }
}
