package ru.introguzzle.mathparser.common.math;

import java.io.Serial;
import java.util.Locale;

public class RadixNumberFormatException extends NumberFormatException {
    @Serial
    private static final long serialVersionUID = -738070192152796245L;

    public RadixNumberFormatException(Radix radix, char digit) {
        super(String.format(Locale.US, "Digit: %s is not allowed in radix %f", digit, radix.getBase()));
    }
}
