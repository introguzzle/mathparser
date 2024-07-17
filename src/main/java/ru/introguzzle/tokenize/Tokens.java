package ru.introguzzle.tokenize;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class Tokens implements Serializable, Iterable<Token> {

    @Serial
    private static final long serialVersionUID = 48984394839L;
    private int position;
    private final List<Token> tokens;
    private transient Integer constantCount;
    private transient Integer variableCount;
    private transient Integer coefficientCount;

    public Tokens() {
        tokens = new ArrayList<>();
    }

    public Tokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void add(Token token) {
        tokens.add(token);
    }

    public void add(TokenType tokenType, CharSequence name) {
        add(new Token(tokenType, name));
    }

    public void add(TokenType tokenType, char name) {
        add(new Token(tokenType, name));
    }

    public void reset() {
        position = 0;
    }

    public void clear() {
        tokens.clear();
    }

    public Token getNextToken() {
        return tokens.get(this.position++);
    }

    public void returnBack() {
        position--;
    }

    public int getPosition() {
        return position;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public int getVariableCount() {
        return variableCount == null ? computeVariableCount() : variableCount;
    }

    public int getConstantCount() {
        return constantCount == null ? computeConstantCount() : constantCount;
    }

    public int getCoefficientCount() {
        return coefficientCount == null ? computeCoefficientCount() : coefficientCount;
    }

    public void skip() {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getTokenType() == TokenType.DECLARATION_END) {
                this.position = i + 1;
                break;
            }
        }
    }

    public boolean isFunctionDefinition() {
        Predicate<Token> predicate = token -> token.getTokenType() == TokenType.DECLARATION;

        return tokens.stream()
                .filter(predicate.or(token -> token.getTokenType() == TokenType.DECLARATION_END))
                .count() == 2;

    }

    private int computeVariableCount() {
        variableCount = (int) tokens.stream()
                .filter(token -> token.getTokenType() == TokenType.VARIABLE)
                .count();

        return variableCount;
    }

    private int computeConstantCount() {
        constantCount = (int) tokens.stream()
                .filter(token -> token.getTokenType() == TokenType.CONSTANT)
                .count();

        return constantCount;
    }

    private int computeCoefficientCount() {
        coefficientCount = (int) tokens.stream()
                .filter(token -> token.getTokenType() == TokenType.COEFFICIENT)
                .count();

        return coefficientCount;
    }

    public int size() {
        return tokens.size();
    }

    public Token get(int index) {
        return tokens.get(index);
    }

    @Override
    public String toString() {
        return tokens.toString();
    }

    @NotNull
    @Override
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }
}
