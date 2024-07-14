package ru.tokens;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Token implements Serializable, Comparable<Token> {

    @Serial
    private static final long serialVersionUID = -54892981192L;
    private final TokenType tokenType;
    private final String data;

    public Token(TokenType tokenType, String data) {
        this.tokenType = tokenType;
        this.data = data;
    }

    public Token(TokenType tokenType, Character data) {
        this.tokenType = tokenType;
        this.data = data.toString();
    }

    public Token(TokenType tokenType, StringBuilder data) {
        this.tokenType = tokenType;
        this.data = data.toString();
    }

    public TokenType getTokenType() {
        return this.tokenType;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + getTokenType() + ", data= '" + getData() + "'}";
    }

    @Override
    public int compareTo(@NotNull Token o) {
        return this.data.compareTo(o.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data, this.tokenType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return tokenType == token.tokenType && data.equals(token.data);
    }

    public boolean equalsType(Token anotherToken) {
        return this.tokenType == anotherToken.tokenType;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
