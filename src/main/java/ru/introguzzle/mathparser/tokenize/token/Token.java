package ru.introguzzle.mathparser.tokenize.token;

import ru.introguzzle.mathparser.tokenize.token.type.Type;
import java.util.Arrays;

/**
 * Interface representing a token in a mathematical expression or formula.
 * <p>
 * A token is a basic unit used in parsing, representing a piece of data such as an operator, number, variable, etc.
 * </p>
 */
public interface Token extends Cloneable {

    /**
     * Gets the type of this token.
     * <p>
     * The type indicates what kind of token this is, such as a number, operator, function, etc.
     * </p>
     *
     * @return The type of this token.
     */
    Type getType();

    /**
     * Gets the data contained in this token.
     * <p>
     * The data is the string representation of the token, such as the actual number, variable name, or operator symbol.
     * </p>
     *
     * @return The data contained in this token.
     */
    String getData();

    /**
     * Gets the offset of this token in the original expression.
     * <p>
     * The offset indicates the position of the token's first character in the original expression.
     * </p>
     *
     * @return The offset of this token.
     */
    int getOffset();

    /**
     * Gets the length of this token.
     * <p>
     * The length is the number of characters this token occupies in the original expression.
     * </p>
     *
     * @return The length of this token.
     */
    int getLength();

    /**
     * Creates a copy of this token.
     *
     * @return A new token that is a clone of this token.
     */
    Token clone();

    /**
     * Sets the offset of this token in the original expression.
     *
     * @param offset The new offset to set.
     */
    void setOffset(int offset);

    /**
     * Merges multiple tokens into a single token.
     * <p>
     * The resulting token will combine the data from all the provided tokens, and its type will be the specified type.
     * The offset of the resulting token will be the offset of the first token, and the length will be the sum of all token lengths.
     * </p>
     *
     * @param type   The type of the resulting merged token.
     * @param tokens The tokens to merge.
     * @return A new token representing the merged tokens.
     * @throws IllegalArgumentException If no tokens are provided.
     */
    static Token merge(Type type, Token... tokens) throws IllegalArgumentException {
        if (tokens.length == 0) {
            throw new IllegalArgumentException("Empty tokens");
        }

        int offset = tokens[0].getOffset();
        int length = Arrays.stream(tokens).mapToInt(Token::getLength).sum();

        return new SimpleToken(type, reduce(tokens), offset, length);
    }

    /**
     * Reduces multiple tokens into a single string.
     * <p>
     * This method concatenates the data from each token into a single string.
     * </p>
     *
     * @param tokens The tokens to reduce.
     * @return A string representing the concatenated data from all the tokens.
     */
    static String reduce(Token... tokens) {
        StringBuilder sb = new StringBuilder();

        for (Token token : tokens) {
            sb.append(token.getData());
        }

        return sb.toString();
    }
}
