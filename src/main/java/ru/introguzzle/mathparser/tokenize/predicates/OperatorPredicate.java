package ru.introguzzle.mathparser.tokenize.predicates;

public class OperatorPredicate implements CharacterPredicate {
    private String allowedOperatorSymbols = "+-/*~!@#$%^&*()\"{}_[]|\\?/<>,.=";

    @Override
    public boolean test(Character character) {
        return character != null && allowedOperatorSymbols.indexOf(character) != -1;
    }

    public String getAllowedOperatorSymbols() {
        return allowedOperatorSymbols;
    }

    public void setAllowedOperatorSymbols(String allowedOperatorSymbols) {
        this.allowedOperatorSymbols = allowedOperatorSymbols;
    }
}
