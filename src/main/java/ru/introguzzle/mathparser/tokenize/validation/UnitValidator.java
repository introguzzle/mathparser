package ru.introguzzle.mathparser.tokenize.validation;

import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.type.OperatorType;
import ru.introguzzle.mathparser.tokenize.token.type.UnitType;

public class UnitValidator implements Validator {
    @Override
    public boolean validate(Token prev, Token token, Token next) throws ValidationException {
        if (token.getType() == OperatorType.CONVERTER
                && prev.getType() != UnitType.UNIT
                && next.getType() != UnitType.UNIT) {
            throw new ValidationException(
                    "Invalid unit conversion syntax: ",
                    Token.reduce(prev, token, next),
                    prev.getOffset()
            );
        }

        return true;
    }
}
