package ru.introguzzle.mathparser.tokenize.token;

import ru.introguzzle.mathparser.common.math.Number;
import ru.introguzzle.mathparser.tokenize.token.type.NumberType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public class NumberToken extends SimpleToken {
    private final Number number;

    public NumberToken(Type type, Number number, int offset) {
        super(type, number.getValue(), offset);
        this.number = number;
    }

    public NumberToken(NumberType numberType, Number number, int offset, int length) {
        super(numberType, number.getValue(), offset, length);
        this.number = number;
    }

    public Number getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "type=" + getType() +
                ", number='" + getNumber().getValue() + '\'' +
                ", radix=" + getNumber().getRadix().getBase() +
                ", offset=" + getOffset() +
                ", length=" + getLength() +
                '}';
    }
}
