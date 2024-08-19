package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.tokenize.Tokenizer;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.Optional;

/**
 * The `Parser` interface defines the contract for parsing mathematical expressions into a numerical type `T`.
 * <p>
 * It supports parsing expressions with or without a context, and provides methods for safely attempting to parse
 * expressions with optional results.
 * </p>
 *
 * @param <T> The numeric type that the parser processes, typically a subclass of {@link Number}.
 */
public interface Parser<T extends Number> {

    /**
     * Parses the given expression and returns the result as an instance of {@code T}.
     *
     * @param expression The expression to be parsed.
     * @return The parsed result as an instance of {@code T}.
     * @throws SyntaxException If the expression is invalid or cannot be parsed.
     */
    T parse(@NotNull Expression expression) throws SyntaxException;

    /**
     * Parses the given expression within the specified context and returns the result as an instance of {@code T}.
     * <p>
     * The context provides additional symbols or variables that may be referenced within the expression.
     * </p>
     *
     * @param expression The expression to be parsed.
     * @param context    The context containing symbols or variables used in the expression.
     * @return The parsed result as an instance of {@code T}.
     * @throws SyntaxException If the expression is invalid or cannot be parsed.
     */
    T parse(@NotNull Expression expression, @NotNull Context<T> context) throws SyntaxException;

    /**
     * Parses the given tokens within the specified context and returns the result as an instance of {@code T}.
     * <p>
     * This method is used for parsing tokenized expressions directly.
     * </p>
     *
     * @param tokens  The tokens representing the expression to be parsed.
     * @param context The context containing symbols or variables used in the expression.
     * @return The parsed result as an instance of {@code T}.
     * @throws SyntaxException If the tokens are invalid or cannot be parsed.
     */
    T parse(@NotNull Tokens tokens, Context<T> context) throws SyntaxException;

    /**
     * Returns the converter associated with this parser for converting between different numerical types.
     *
     * @return The {@link NumberConverter} associated with this parser.
     */
    NumberConverter<T> getConverter();

    /**
     * Returns the tokenizer used by this parser for converting expressions into tokens.
     *
     * @return The {@link Tokenizer} used by this parser.
     */
    Tokenizer getTokenizer();

    /**
     * Attempts to parse the given expression and returns an {@link Optional} containing the result.
     * <p>
     * If the expression is {@code null} or cannot be parsed, an empty {@link Optional} is returned.
     * </p>
     *
     * @param expression The expression to be parsed, or {@code null}.
     * @return An {@link Optional} containing the parsed result, or an empty {@link Optional} if parsing fails.
     */
    default Optional<T> tryParse(@Nullable Expression expression) {
        if (expression == null) return Optional.empty();

        try {
            return Optional.of(parse(expression));
        } catch (SyntaxException e) {
            return Optional.empty();
        }
    }

    /**
     * Attempts to parse the given expression within the specified context and returns an {@link Optional} containing the result.
     * <p>
     * If the expression or context is {@code null} or parsing fails, an empty {@link Optional} is returned.
     * </p>
     *
     * @param expression The expression to be parsed, or {@code null}.
     * @param context    The context containing symbols or variables, or {@code null}.
     * @return An {@link Optional} containing the parsed result, or an empty {@link Optional} if parsing fails.
     */
    default Optional<T> tryParse(@Nullable Expression expression, @Nullable Context<T> context) {
        if (expression == null || context == null) return Optional.empty();

        try {
            return Optional.of(parse(expression, context));
        } catch (SyntaxException e) {
            return Optional.empty();
        }
    }
}
