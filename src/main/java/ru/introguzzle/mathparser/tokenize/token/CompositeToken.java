package ru.introguzzle.mathparser.tokenize.token;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.group.Group;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public class CompositeToken extends SimpleToken implements Group {
    private final Tokens tokens;

    public CompositeToken(Type specialType,
                          Tokens tokens,
                          int offset) {
        super(specialType, tokens.toExpression().getString(), offset);
        this.tokens = tokens;
    }

    @Override
    public String getData() {
        return tokens.toExpression().getString();
    }

    @Override
    public int getLength() {
        return tokens.stream().mapToInt(Token::getLength).sum();
    }

    @Override
    public Type getType() {
        return super.getType();
    }

    public @NotNull Tokens getTokens() {
        return tokens;
    }
}
