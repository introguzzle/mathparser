package ru.introguzzle.mathparser.tokenize.token;

import ru.introguzzle.mathparser.tokenize.token.type.Type;

public interface Token extends Cloneable {
    Type getType();

    String getData();
    int getOffset();
    int getLength();
    Token clone();

    void setOffset(int offset);

    static String reduce(Token... tokens) {
        StringBuilder sb = new StringBuilder();

        for (Token token : tokens) {
            sb.append(token.getData());
        }

        return sb.toString();
    }
}
