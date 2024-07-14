package ru.tokens;

import java.util.ArrayList;
import java.util.List;

public class Tokens {
    private int position;
    private final List<Token> tokens;
    private Integer constantCount;
    private Integer variableCount;

    public Tokens() {
        this.tokens = new ArrayList<>();
    }

    public Tokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void add(Token token) {
        this.tokens.add(token);
    }

    public void add(TokenType tokenType, String representation) {
        this.add(new Token(tokenType, representation));
    }

    public void add(TokenType tokenType, StringBuilder representation) {
        this.add(new Token(tokenType, representation));
    }

    public void add(TokenType tokenType, char representation) {
        this.add(new Token(tokenType, representation));
    }

    public void reset() {
        this.position = 0;
    }

    public void clear() {
        this.tokens.clear();
    }

    public Token getNextToken() {
        return this.tokens.get(this.position++);
    }

    public void returnBack() {
        position--;
    }

    public int getPosition() {
        return position;
    }

    public List<Token> getTokens() {
        return this.tokens;
    }

    public int getVariableCount() {
        return this.variableCount == null ? this.computeVariableCount() : this.variableCount;
    }

    public int getConstantCount() {
        return this.constantCount == null ? this.computeConstantCount() : this.constantCount;
    }

    private int computeVariableCount() {
        return (int) tokens.stream()
                .filter(token -> token.getTokenType() == TokenType.VARIABLE)
                .count();
    }

    private int computeConstantCount() {
        return (int) tokens.stream()
                .filter(token -> token.getTokenType() == TokenType.CONSTANT)
                .count();
    }

    public int size() {
        return this.tokens.size();
    }

    public Token get(int index) {
        return this.tokens.get(index);
    }

    @Override
    public String toString() {
        return this.tokens.toString();
    }
}