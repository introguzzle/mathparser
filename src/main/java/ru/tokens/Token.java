package ru.tokens;

import ru.main.TokenType;

public class Token {
    private TokenType tokenType;
    private String data;

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

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + getTokenType() + ", data= '" + getData() + "'}";
    }
}
