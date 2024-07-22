package ru.introguzzle.mathparser.tokenize.token;

import java.util.List;

public interface Tokens extends Iterable<Token> {
    void add(Token token);

    void add(Type type, CharSequence name);

    void add(Type type, char name);

    void reset();

    default void clear() {
        getTokens().clear();
    }

    Token getNextToken();

    void returnBack();

    int getPosition();

    List<Token> getTokens();

    int getVariableCount();

    int getConstantCount();

    int getCoefficientCount();

    void skipDeclaration();

    int size();

    Token get(int index);
}
