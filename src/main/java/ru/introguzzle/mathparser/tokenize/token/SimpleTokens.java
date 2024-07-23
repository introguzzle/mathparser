package ru.introguzzle.mathparser.tokenize.token;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SimpleTokens implements Tokens, Serializable {

    @Serial
    private static final long serialVersionUID = 48984394839L;
    private int position;
    private final List<Token> tokens;
    private transient Integer constantCount;
    private transient Integer variableCount;
    private transient Integer coefficientCount;

    public SimpleTokens() {
        tokens = new ArrayList<>();
    }

    public SimpleTokens(Token token) {
        tokens = new ArrayList<>();
        tokens.add(token);
    }

    public SimpleTokens(Token... tokens) {
        this.tokens = Arrays.asList(tokens);
    }

    public SimpleTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public void add(Token token) {
        tokens.add(token);
    }

    @Override
    public void add(Type type, CharSequence name, int offset) {
        add(new SimpleToken(type, name, offset));
    }

    @Override
    public void add(Type type, char name, int offset) {
        add(new SimpleToken(type, name, offset));
    }

    @Override
    public void reset() {
        position = 0;
    }

    @Override
    public Token getNextToken() {
        return tokens.get(this.position++);
    }

    @Override
    public void returnBack() {
        position--;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public List<Token> getTokens() {
        return tokens;
    }

    @Override
    public int getVariableCount() {
        return variableCount == null ? computeVariableCount() : variableCount;
    }

    @Override
    public int getConstantCount() {
        return constantCount == null ? computeConstantCount() : constantCount;
    }

    @Override
    public int getCoefficientCount() {
        return coefficientCount == null ? computeCoefficientCount() : coefficientCount;
    }

    @Override
    public void skipDeclaration() {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenType.DECLARATION_END) {
                this.position = i + 1;
                break;
            }
        }
    }

    private int computeVariableCount() {
        variableCount = (int) tokens.stream()
                .filter(token -> token.getType() == TokenType.VARIABLE)
                .count();

        return variableCount;
    }

    private int computeConstantCount() {
        constantCount = (int) tokens.stream()
                .filter(token -> token.getType() == TokenType.CONSTANT)
                .count();

        return constantCount;
    }

    private int computeCoefficientCount() {
        coefficientCount = (int) tokens.stream()
                .filter(token -> token.getType() == TokenType.COEFFICIENT)
                .count();

        return coefficientCount;
    }

    @Override
    public int size() {
        return tokens.size();
    }

    @Override
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
