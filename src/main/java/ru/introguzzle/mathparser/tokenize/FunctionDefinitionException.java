package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.expression.Expression;

public class FunctionDefinitionException extends TokenizeException {

    public FunctionDefinitionException(String message, Expression expression, int offset) {
        super(message, expression, offset);
    }
}
