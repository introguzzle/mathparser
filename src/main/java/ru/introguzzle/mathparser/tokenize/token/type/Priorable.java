package ru.introguzzle.mathparser.tokenize.token.type;

public interface Priorable {
    int getPriority();

    Association getAssociation();

    default boolean isLeftAssociative() {
        return getAssociation() == Association.LEFT;
    }

    default boolean isRightAssociative() {
        return getAssociation() == Association.RIGHT;
    }

    enum Association {
        LEFT, RIGHT, NONE
    }
}
