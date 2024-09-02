package ru.introguzzle.mathparser.tokenize.token.type;

/**
 * Represents a type in the tokenization process.
 * Each type has an ordinal value, a category, and a name.
 */
public interface Type {
    /**
     * Returns the ordinal value of the type, typically representing its position.
     *
     * @return the ordinal value of the type.
     */
    int ordinal();

    /**
     * Returns the category to which this type belongs.
     *
     * @return the category of the type.
     */
    Category getCategory();

    /**
     * Returns the name of the type.
     *
     * @return the name of the type.
     */
    String name();

    /**
     * Determines if this type represents a terminal token.
     *
     * @return true if the type is terminal, false otherwise.
     */
    default boolean isTerminal() {
        return getCategory().isTerminal();
    }

    /**
     * Represents the category of a type in the tokenization process.
     * Each category can be associated with a specific token type.
     */
    enum Category implements Type {
        OPERATOR,
        PARENTHESIS,
        NUMBER,
        LAMBDA,
        FUNCTION,
        DECLARATION,
        SYMBOL,
        TERMINAL,
        DELIMITER,
        UNIT,
        SPECIAL;

        /**
         * Returns the category of this type, which in this case is the category itself.
         *
         * @return the category of the type.
         */
        @Override
        public Category getCategory() {
            return this;
        }

        /**
         * Determines if this category represents a terminal token.
         *
         * @return true if the category is TERMINAL, false otherwise.
         */
        public boolean isTerminal() {
            return this == TERMINAL;
        }
    }
}
