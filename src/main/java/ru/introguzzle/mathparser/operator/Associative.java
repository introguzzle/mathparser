package ru.introguzzle.mathparser.operator;

import org.jetbrains.annotations.NotNull;

public interface Associative {
    @NotNull
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
