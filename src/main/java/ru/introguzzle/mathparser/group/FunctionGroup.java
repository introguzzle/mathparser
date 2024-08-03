package ru.introguzzle.mathparser.group;

import ru.introguzzle.mathparser.definition.FunctionDefinitionType;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

public class FunctionGroup extends TokenGroup {
    private final FunctionDefinitionType type;
    private final Tokens definitionTokens;

    public FunctionGroup(Tokens definitionTokens,
                         Tokens mainTokens,
                         FunctionDefinitionType type) {
        super(mainTokens);

        this.definitionTokens = definitionTokens;
        this.type = type;
    }

    public FunctionDefinitionType getType() {
        return type;
    }

    public Tokens getDefinitionTokens() {
        return definitionTokens;
    }
}
