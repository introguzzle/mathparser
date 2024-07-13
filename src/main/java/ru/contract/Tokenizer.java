package ru.contract;

import ru.constant.Constants;
import ru.exceptions.TokenizeException;
import ru.function.Function;
import ru.tokens.Tokens;

import java.util.List;
import java.util.Map;

public interface Tokenizer {
    Tokens tokenize(Expression expression) throws TokenizeException;

    List<Symbol> getConstants();
    Map<String, Function> getFunctions();

}
