package ru.introguzzle.mathparser.tokenize.token;

import ru.introguzzle.mathparser.definition.FunctionDefinitionType;
import ru.introguzzle.mathparser.symbol.MutableSymbol;

import java.util.HashMap;
import java.util.Map;

/**
 * Adapter class
 */
public class FunctionTokens extends SimpleTokens {
    private final Map<String, MutableSymbol> mappings = new HashMap<>();
    private final FunctionDefinitionType type;

    public Map<String, MutableSymbol> getMappings() {
        return mappings;
    }

    public FunctionTokens(Tokens tokens, FunctionDefinitionType type) {
        this.type = type;
        getTokens().clear();
        getTokens().addAll(tokens.getTokens());
    }

    public FunctionDefinitionType getType() {
        return type;
    }
}
