package ru.introguzzle.mathparser.tokenize.token;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

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
    private final int offset;
    private final int length;

    public static Token of(Type type, CharSequence data, int offset) {
        return new SimpleToken(type, data, offset);
    }

    public static Token of(Type type, CharSequence data, int offset, int length) {
        return new SimpleToken(type, data, offset, length);
    }

    public SimpleToken(Type type, CharSequence data, int offset) {
        this(type, data, offset, data.length());
    }

    public SimpleToken(Type type, Character data, int offset) {
        this(type, data, offset, 1);
    }

    public SimpleToken(Type type, CharSequence data, int offset, int length) {
        this.type = type;
        this.data = data.toString();
        this.offset = offset;
        this.length = length;
    }

    public SimpleToken(Type type, Character data, int offset, int length) {
        this.type = type;
        this.data = data.toString();
        this.offset = offset;
        this.length = length;
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
    public int getOffset() {
        return offset;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "type=" + type +
                ", data='" + data + '\'' +
                ", offset=" + offset +
                ", length=" + length +
                '}';
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
