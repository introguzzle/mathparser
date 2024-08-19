package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.tokenize.token.Token;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the result of a search operation, typically used to find a specific token or symbol.
 * A search result can indicate whether a match was found and, if so, can hold a reference to the found token.
 */
public class SearchResult {
    private boolean match = false; // Indicates whether the search was successful.
    private Token token; // The token found during the search, or null if no match was found.

    /**
     * Default constructor for creating an empty SearchResult.
     */
    public SearchResult() {

    }

    /**
     * Constructs a SearchResult with the given match status and token.
     *
     * @param match Indicates whether a match was found.
     * @param token The token found, or null if no match was found.
     */
    public SearchResult(boolean match, @Nullable Token token) {
        this.setMatch(match);
        this.setToken(token);
    }

    /**
     * Reduces multiple search results into a single result.
     * <p>
     * This method checks if any of the given search results contain a match. If at least one match is found,
     * it returns a SearchResult indicating a match and containing the first non-null token found.
     * If no matches are found, it returns a SearchResult with match set to false and a null token.
     * </p>
     *
     * @param results The search results to be reduced.
     * @return A new SearchResult indicating whether any matches were found and the first non-null token, if any.
     */
    @Contract("_ -> new")
    public static @NotNull SearchResult reduce(SearchResult... results) {
        boolean match = Arrays.stream(results)
                .anyMatch(SearchResult::isMatch); // Check if any result contains a match.

        if (!match) {
            return new SearchResult(false, null); // Return a non-matching result if no matches were found.
        }

        Token t = Arrays.stream(results)
                .sequential()
                .map(SearchResult::getToken)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null); // Find the first non-null token among the results.

        return new SearchResult(t != null, t); // Return a matching result with the first non-null token.
    }

    /**
     * Checks whether the search was successful.
     *
     * @return True if a match was found, false otherwise.
     */
    public boolean isMatch() {
        return match;
    }

    /**
     * Sets the match status of the search result.
     *
     * @param match True if a match was found, false otherwise.
     */
    public void setMatch(boolean match) {
        this.match = match;
    }

    /**
     * Gets the token found during the search, if any.
     *
     * @return The found token, or null if no match was found.
     */
    public Token getToken() {
        return token;
    }

    /**
     * Sets the token found during the search.
     *
     * @param token The token to set, or null if no match was found.
     */
    public void setToken(Token token) {
        this.token = token;
    }
}
