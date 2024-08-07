package ru.introguzzle.mathparser.common.math;

import java.util.Locale;

public class RadixNumberFormatException extends NumberFormatException {
    public RadixNumberFormatException(Radix radix, char digit) {
        super(String.format(Locale.US, "Digit: %s is not allowed in radix %f", digit, radix.getRadix()));
    }
}
