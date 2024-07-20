package ru.introguzzle.mathparser.symbol;


import ru.introguzzle.mathparser.tokenize.TokenType;

public class Variable extends MutableSymbol {

    public Variable(String name, double value) {
        super(name, value);
    }

    @Override
    public TokenType type() {
        return TokenType.VARIABLE;
    }
}
