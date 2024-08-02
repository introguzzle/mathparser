package ru.introguzzle.mathparser.operator;

public interface Associative {
    Association getAssociation();

    enum Association {
        LEFT, RIGHT, NONE
    }

    default boolean isLeftAssociative() {
        return getAssociation() == Association.LEFT;
    }

    default boolean isRightAssociative() {
        return getAssociation() == Association.RIGHT;
    }
}
