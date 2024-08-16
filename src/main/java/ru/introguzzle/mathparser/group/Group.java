package ru.introguzzle.mathparser.group;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

public interface Group {
    @NotNull Tokens getTokens();
}
