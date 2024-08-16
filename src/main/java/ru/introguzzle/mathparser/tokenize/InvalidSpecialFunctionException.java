package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.expression.Expression;

public class InvalidSpecialFunctionException extends TokenizeException {
    public InvalidSpecialFunctionException(CharSequence name,
                                           CharSequence sequence,
                                           Expression expression,
                                           int offset) {
        super("Invalid body syntax of function " + name + " : " + sequence.toString(), expression, offset);
    }
}
