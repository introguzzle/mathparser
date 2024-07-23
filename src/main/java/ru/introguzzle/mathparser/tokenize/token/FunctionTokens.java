package ru.introguzzle.mathparser.tokenize.token;

import ru.introguzzle.mathparser.definition.FunctionDefinitionType;

/**
 * Adapter class
 */
public class FunctionTokens extends SimpleTokens {
    private final FunctionDefinitionType type;

    public FunctionTokens(Tokens tokens, FunctionDefinitionType type) {
        this.type = type;
        getTokens().clear();
        getTokens().addAll(tokens.getTokens());
    }

    public FunctionDefinitionType getType() {
        return type;
    }
}
