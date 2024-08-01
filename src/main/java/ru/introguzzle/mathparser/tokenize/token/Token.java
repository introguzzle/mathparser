package ru.introguzzle.mathparser.tokenize.token;

import ru.introguzzle.mathparser.tokenize.token.type.Type;

public interface Token {
    Type getType();

    String getData();
    int getOffset();
    int getLength();
}
