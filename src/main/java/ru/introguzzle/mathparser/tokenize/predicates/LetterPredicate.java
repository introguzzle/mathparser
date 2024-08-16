package ru.introguzzle.mathparser.tokenize.predicates;

public class LetterPredicate implements CharacterPredicate {
    @Override
    public boolean test(Character character) {
        return character != null && (character == '_' || Character.isLetter(character));
    }
}
