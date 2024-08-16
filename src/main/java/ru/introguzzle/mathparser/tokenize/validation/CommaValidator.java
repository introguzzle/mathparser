package ru.introguzzle.mathparser.tokenize.validation;

import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.type.DelimiterType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public class CommaValidator implements Validator {
    @Override
    public boolean validate(Token prev, Token token, Token next) throws ValidationException {
        Type type = token.getType();
        Type nextType = next.getType();

        if (type == DelimiterType.COMMA && nextType == DelimiterType.COMMA) {
            throw new InvalidCommaSyntaxException(token.getData() + next.getData(), next.getOffset());
        }

        return true;
    }

    public final static class InvalidCommaSyntaxException extends ValidationException {
        public InvalidCommaSyntaxException(String data, int offset) {
            super("Invalid comma syntax: ", data, offset);
        }
    }
}
