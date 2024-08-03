package ru.introguzzle.mathparser.tokenize.token;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.primitive.IntegerReference;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.tokenize.token.type.DeclarationType;
import ru.introguzzle.mathparser.tokenize.token.type.SymbolType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

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
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public List<Token> getTokenList() {
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
        int result = findType(DeclarationType.DECLARATION);
        if (result == -1) {
            int then = findType(DeclarationType.DECLARATION_TERMINAL);
            if (then != -1) {
                position = result;
            }
        } else {
            position = result;
        }
    }

    private int findType(Type type) {
        IntegerReference ref = new IntegerReference(-1);

        IntStream.range(0, tokens.size())
                .filter(i -> tokens.get(i).getType() == type)
                .findFirst()
                .ifPresent(i -> ref.setValue(i + 1));

        return ref.getValue();
    }

    private int computeVariableCount() {
        variableCount = (int) tokens.stream()
                .filter(token -> token.getType() == SymbolType.VARIABLE)
                .count();

        return variableCount;
    }

    private int computeConstantCount() {
        constantCount = (int) tokens.stream()
                .filter(token -> token.getType() == SymbolType.CONSTANT)
                .count();

        return constantCount;
    }

    private int computeCoefficientCount() {
        coefficientCount = (int) tokens.stream()
                .filter(token -> token.getType() == SymbolType.COEFFICIENT)
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
    public String reduce() {
        StringBuilder builder = new StringBuilder();
        int currentOffset = 0;

        for (Token token : tokens) {
            int tokenOffset = token.getOffset();

            while (currentOffset < tokenOffset) {
                builder.append(' ');
                currentOffset++;
            }

            builder.append(token.getData());
            currentOffset += token.getData().length();
        }

        return builder.toString();
    }

    @Override
    public Expression toExpression() {
        return new MathExpression(reduce());
    }

    @Override
    public void merge(Tokens other) {
        for (Token token: other) {
            this.add(token);
        }
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
