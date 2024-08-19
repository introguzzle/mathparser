package ru.introguzzle.mathparser.tokenize.token;

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
import java.util.List;
import java.util.stream.IntStream;

public class SimpleTokens implements Tokens, Serializable {

    @Serial
    private static final long serialVersionUID = -2446858740059770855L;
    private int position = 0;

    private final List<Token> tokens;
    private transient Integer constantCount;
    private transient Integer variableCount;
    private transient Integer coefficientCount;

    public SimpleTokens() {
        this(new ArrayList<>());
    }

    public SimpleTokens(Token... tokens) {
        this(Arrays.asList(tokens));
    }

    public SimpleTokens(List<Token> tokens) {
        this.tokens = tokens;
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
    public Token next() {
        return get(position++);
    }

    @Override
    public void back() {
        position--;
    }

    @Override
    public int getPosition() {
        return position;
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

        IntStream.range(0, size())
                .filter(i -> get(i).getType() == type)
                .findFirst()
                .ifPresent(i -> ref.setValue(i + 1));

        return ref.getValue();
    }

    private int computeVariableCount() {
        variableCount = (int) stream()
                .filter(token -> token.getType() == SymbolType.VARIABLE)
                .count();

        return variableCount;
    }

    private int computeConstantCount() {
        constantCount = (int) stream()
                .filter(token -> token.getType() == SymbolType.CONSTANT)
                .count();

        return constantCount;
    }

    private int computeCoefficientCount() {
        coefficientCount = (int) stream()
                .filter(token -> token.getType() == SymbolType.COEFFICIENT)
                .count();

        return coefficientCount;
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
    public Tokens clone() {
        Tokens clone = new SimpleTokens();

        for (Token token: tokens) {
            clone.add(token.clone());
        }

        return clone;
    }

    @Override
    public void setPosition(int offset) {
        position = offset;
    }

    @Override
    public String toString() {
        return tokens.toString();
    }
}
