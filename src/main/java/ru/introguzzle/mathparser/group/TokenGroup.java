package ru.introguzzle.mathparser.group;

import ru.introguzzle.mathparser.tokenize.token.Tokens;

public class TokenGroup implements Group {
    private final Tokens tokens;

    public TokenGroup(Tokens tokens) {
        this.tokens = tokens;
    }

    @Override
    public Tokens getTokens() {
        return tokens;
    }
}
