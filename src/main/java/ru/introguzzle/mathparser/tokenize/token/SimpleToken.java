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

    public SimpleToken(Type type, CharSequence data, int offset) {
        this.type = type;
        this.data = data.toString();
        this.offset = offset;
        this.length = data.length();
    }

    public SimpleToken(Type type, Character data, int offset) {
        this.type = type;
        this.data = data.toString();
        this.offset = offset;
        this.length = 1;
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
        return "SimpleToken{" +
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
