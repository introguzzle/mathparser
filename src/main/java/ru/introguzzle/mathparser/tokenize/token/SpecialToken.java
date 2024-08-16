package ru.introguzzle.mathparser.tokenize.token;

import ru.introguzzle.mathparser.tokenize.token.type.Type;

public class SpecialToken extends CompositeToken {
    public SpecialToken(Type type, Tokens tokens, int offset) {
        super(type, tokens, offset);
    }
}
