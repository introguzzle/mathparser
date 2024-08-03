package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.tokenize.token.type.Type;

public class EquationTokenizer extends MathTokenizer {
    public enum EquationType implements Type {
        SPLITERATOR;

        @Override
        public Category getCategory() {
            return Category.SPECIAL;
        }
    }
}
