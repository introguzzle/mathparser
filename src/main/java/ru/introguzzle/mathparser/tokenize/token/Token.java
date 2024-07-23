package ru.introguzzle.mathparser.tokenize.token;

public interface Token {
    Type getType();

    String getData();
    int getOffset();
    int getLength();
}
