package ru.introguzzle.mathparser.tokenize.token;

import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

import java.util.List;

public interface Tokens extends Iterable<Token> {
    void add(Token token);
    void add(Type type, CharSequence name, int offset);
    void add(Type type, char name, int offset);
    void reset();
    Token getNextToken();
    void returnBack();
    int getPosition();
    List<Token> getTokenList();
    int getVariableCount();
    int getConstantCount();
    int getCoefficientCount();
    void skipDeclaration();
    int size();
    Token get(int index);
    String reduce();
    Expression toExpression();
}
