package ru.introguzzle.mathparser.tokenize.validation;

import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.type.DelimiterType;
import ru.introguzzle.mathparser.tokenize.token.type.ParenthesisType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;
import ru.introguzzle.mathparser.tokenize.token.type.UnitType;

public class NumberValidator implements Validator {
    @Override
    public boolean validate(Token prev, Token token, Token next) throws ValidationException {
        Type type = token.getType();
        Type nextType = next.getType();

        // After number there can be only operator, comma or right parenthesis
        if (type.getCategory() == Type.Category.NUMBER
                && nextType.getCategory() != Type.Category.OPERATOR
                && nextType != DelimiterType.ARROW
                && nextType != DelimiterType.COMMA
                && nextType != DelimiterType.SEMICOLON
                && nextType != UnitType.UNIT
                && nextType != ParenthesisType.RIGHT) {

            int start = token.getOffset();
            String number = token.getData() + next.getData();

            throw new ValidationException("Invalid number format: ", number, start);
        }

        return true;
    }
}
