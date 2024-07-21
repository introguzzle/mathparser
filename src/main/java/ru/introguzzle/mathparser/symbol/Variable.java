package ru.introguzzle.mathparser.symbol;


import ru.introguzzle.mathparser.tokenize.TokenType;
import ru.introguzzle.mathparser.tokenize.Type;

public class Variable extends MutableSymbol {

    public Variable(String name, double value) {
        super(name, value);
    }

    @Override
    public Type type() {
        return TokenType.VARIABLE;
    }
}
