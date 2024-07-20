package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.common.Context;

public class ContextlessMathTokenizer extends MathTokenizer {

    @Override
    protected Result findFromContext(Context context, CharSequence symbols) {
        return new Result(true, new Token(TokenType.VARIABLE, symbols));
    }
}
