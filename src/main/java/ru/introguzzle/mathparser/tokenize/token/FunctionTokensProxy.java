package ru.introguzzle.mathparser.tokenize.token;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.definition.FunctionDefinitionType;
import ru.introguzzle.mathparser.expression.Expression;

import java.util.Iterator;
import java.util.List;

/**
 * Adapter class
 */
public class FunctionTokensProxy implements Tokens {
    private final FunctionDefinitionType type;
    private final Tokens tokens;

    public FunctionTokensProxy(Tokens tokens, FunctionDefinitionType type) {
        this.tokens = tokens;
        this.type = type;
    }

    public FunctionDefinitionType getType() {
        return type;
    }

    @Override
    public void add(Token token) {
        tokens.add(token);
    }

    @Override
    public void add(Type type, CharSequence name, int offset) {
        tokens.add(type, name, offset);
    }

    @Override
    public void add(Type type, char name, int offset) {
        tokens.add(type, name, offset);
    }

    @Override
    public void reset() {
        tokens.reset();
    }

    @Override
    public Token getNextToken() {
        return tokens.getNextToken();
    }

    @Override
    public void returnBack() {
        tokens.returnBack();
    }

    @Override
    public int getPosition() {
        return tokens.getPosition();
    }

    @Override
    public List<Token> getTokens() {
        return tokens.getTokens();
    }

    @Override
    public int getVariableCount() {
        return tokens.getVariableCount();
    }

    @Override
    public int getConstantCount() {
        return tokens.getConstantCount();
    }

    @Override
    public int getCoefficientCount() {
        return tokens.getCoefficientCount();
    }

    @Override
    public void skipDeclaration() {
        tokens.skipDeclaration();
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
    public String reduce() {
        return tokens.reduce();
    }

    @Override
    public Expression toExpression() {
        return tokens.toExpression();
    }

    @NotNull
    @Override
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }
}
