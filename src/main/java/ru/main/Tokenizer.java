package ru.main;

import ru.exceptions.TokenizeException;
import ru.tokens.Token;

import java.util.List;

public interface Tokenizer {
    Tokens tokenize(Expression expression) throws TokenizeException;
}
