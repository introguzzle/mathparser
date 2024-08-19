package ru.introguzzle.mathparser.tokenize.token;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Mutates;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.tokenize.token.type.TerminalType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface Tokens extends Iterable<Token> {
    default void add(Token token) {
        getTokenList().add(token);
    }

    void add(Type type, CharSequence name, int offset);
    void add(Type type, char name, int offset);

    void reset();

    default Token remove(int index) {
        return getTokenList().remove(index);
    }

    Token next();
    void back();

    default List<Tokens> split(Type type) {
        List<Tokens> splitTokensList = new ArrayList<>();
        Tokens currentTokens = new SimpleTokens();

        for (Token token : getTokenList()) {
            if (token.getType().equals(type)) {
                if (!currentTokens.getTokenList().isEmpty()) {
                    currentTokens.add(TerminalType.TERMINAL, "", currentTokens.getLast().getOffset() + currentTokens.getLast().getLength());
                    splitTokensList.add(currentTokens);
                    currentTokens = new SimpleTokens();
                }
            } else {
                currentTokens.add(token);
            }
        }

        if (!currentTokens.getTokenList().isEmpty()) {
            splitTokensList.add(currentTokens);
        }

        return splitTokensList;
    }

    default String toStringRecursive() {
        return toStringRecursive(0);
    }

    private String toStringRecursive(int level) {
        StringBuilder result = new StringBuilder();
        String indent = "    ".repeat(level);

        for (Token token : getTokenList()) {
            result.append(indent);

            if (token instanceof CompositeToken compositeToken) {
                result.append(token).append("\n");
                result.append(compositeToken.getTokens().toStringRecursive(level + 1));
            } else {
                result.append(token.toString()).append("\n");
            }
        }

        return result.toString();
    }

    int getPosition();
    List<Token> getTokenList();
    int getVariableCount();
    int getConstantCount();
    int getCoefficientCount();
    void skipDeclaration();

    default int size() {
        return getTokenList().size();
    }

    default Optional<Token> findFirst(Type type) {
        return stream().filter(p -> p.getType() == type).findFirst();
    }

    default Token get(int index) {
        return getTokenList().get(index);
    }

    default Token getFirst() {
        return getTokenList().getFirst();
    }

    default Token getLast() {
        return getTokenList().getLast();
    }

    String reduce();

    Expression toExpression();

    @Mutates default void merge(Token token) {
        SimpleTokens other = new SimpleTokens();
        other.add(token);
        merge(other);
    }

    @Mutates default void merge(Tokens other) {
        int index;
        Token last = get(index = size() - 1);

        int length = last.getOffset() + last.getLength();
        getTokenList().addAll(other.getTokenList());

        for (int i = index + 1; i < getTokenList().size(); i++) {
            Token token = getTokenList().get(i);
            token.setOffset(token.getOffset() + length);
        }
    }

    default @NotNull Iterator<Token> iterator() {
        return getTokenList().iterator();
    }

    Tokens clone();

    default Stream<Token> stream() {
        return getTokenList().stream();
    }

    default Stream<Token> parallelStream() {
        return getTokenList().parallelStream();
    }

    void setPosition(int offset);

    default Tokens slice(int from, int to) {
        List<Token> slice = getTokenList().subList(from, to);
        return new SimpleTokens(slice);
    }
}
