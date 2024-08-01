package ru.introguzzle.mathparser.tokenize.token.type;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.tokenize.token.SimpleToken;
import ru.introguzzle.mathparser.tokenize.token.Token;

public interface ScalarType extends Type {
    @NotNull String getRepresentation();

    default @NotNull Token getToken(int offset) {
        return new SimpleToken(this, getRepresentation(), offset);
    }
}
