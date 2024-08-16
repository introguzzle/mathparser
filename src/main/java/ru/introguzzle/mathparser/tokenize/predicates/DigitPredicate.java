package ru.introguzzle.mathparser.tokenize.predicates;

import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.complex.Complex;

public class DigitPredicate implements CharacterPredicate {
    private char decimalPoint = '.';
    private char imaginaryUnit = Complex.I;
    private char radixSpliterator = '_';

    @Override
    public boolean test(@Nullable Character character) {
        return character != null && (character == decimalPoint
                || character == imaginaryUnit
                || character == radixSpliterator
                || Character.isDigit(character)
        );
    }

    public char getDecimalPoint() {
        return decimalPoint;
    }

    public void setDecimalPoint(char decimalPoint) {
        this.decimalPoint = decimalPoint;
    }

    public char getImaginaryUnit() {
        return imaginaryUnit;
    }

    public void setImaginaryUnit(char imaginaryUnit) {
        this.imaginaryUnit = imaginaryUnit;
    }

    public char getRadixSpliterator() {
        return radixSpliterator;
    }

    public void setRadixSpliterator(char radixSpliterator) {
        this.radixSpliterator = radixSpliterator;
    }
}
