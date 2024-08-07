package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.tokenize.token.Token;

import java.util.Arrays;
import java.util.Objects;

public class SearchResult {
    public boolean match = false;
    public Token token;

    public SearchResult() {

    }

    public SearchResult(boolean match, @Nullable Token token) {
        this.match = match;
        this.token = token;
    }

    @Contract("_ -> new")
    public static @NotNull SearchResult reduce(SearchResult... results) {
        boolean match = Arrays.stream(results)
                .anyMatch(r -> r.match);

        if (!match) {
            return new SearchResult(false, null);
        }

        Token nonNullToken = Arrays.stream(results)
                .sequential()
                .map(result -> result.token)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        return new SearchResult(true, nonNullToken);
    }
}
