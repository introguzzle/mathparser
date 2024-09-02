package ru.introguzzle.mathparser.tokenize.validation;

import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.type.OperatorType;
import ru.introguzzle.mathparser.tokenize.token.type.UnitType;

public class UnitValidator implements Validator {
    @Override
    public boolean validate(Token prev, Token token, Token next) {
        return token.getType() != OperatorType.CONVERTER
                || prev.getType() == UnitType.UNIT
                || next.getType() == UnitType.UNIT;
    }
}
