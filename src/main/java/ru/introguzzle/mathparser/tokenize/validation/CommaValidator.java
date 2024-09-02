package ru.introguzzle.mathparser.tokenize.validation;

import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.type.DelimiterType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public class CommaValidator implements Validator {
    @Override
    public boolean validate(Token prev, Token token, Token next) {
        Type type = token.getType();
        Type nextType = next.getType();

        return type != DelimiterType.COMMA || nextType != DelimiterType.COMMA;
    }
}
