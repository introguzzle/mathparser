package ru.introguzzle.mathparser.common.math;

public class RadixNumberFormatException extends NumberFormatException {
    public RadixNumberFormatException(Radix radix, char digit) {
        super(String.format("Digit: %s is not allowed in radix %f", digit, radix.getRadix()));
    }
}
