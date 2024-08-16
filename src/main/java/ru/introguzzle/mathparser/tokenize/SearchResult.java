package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.tokenize.token.Token;

import java.util.Arrays;
import java.util.Objects;

public class SearchResult {
    private boolean match = false;
    private Token token;

    public SearchResult() {

    }

    public SearchResult(boolean match, @Nullable Token token) {
        this.setMatch(match);
        this.setToken(token);
    }

    @Contract("_ -> new")
    public static @NotNull SearchResult reduce(SearchResult... results) {
        boolean match = Arrays.stream(results)
                .anyMatch(SearchResult::isMatch);

        if (!match) {
            return new SearchResult(false, null);
        }

        Token nonNullToken = Arrays.stream(results)
                .sequential()
                .map(SearchResult::getToken)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        return new SearchResult(true, nonNullToken);
    }

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
