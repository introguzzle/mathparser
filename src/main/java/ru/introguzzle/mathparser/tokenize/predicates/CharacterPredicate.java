package ru.introguzzle.mathparser.tokenize.predicates;

import java.util.function.Predicate;

public interface CharacterPredicate extends Predicate<Character> {
    default boolean test(String string) {
        boolean match = true;

        for (int i = 0; i < string.length(); i++) {
            match &= test(string.charAt(i));
        }

        return match;
    }
}
