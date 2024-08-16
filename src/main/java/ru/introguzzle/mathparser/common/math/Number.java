package ru.introguzzle.mathparser.common.math;

import ru.introguzzle.mathparser.complex.Complex;
import ru.introguzzle.mathparser.unit.Unit;
import ru.introguzzle.mathparser.unit.measure.Measure;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a number in any given radix (base), including complex numbers.
 */
public class Number extends java.lang.Number {
    public static final double EPSILON = 1E-8;
    public static final int MAX_FRACTION_DIGITS = 12;

    private final String value;
    private final Radix radix;

    private boolean complex = false;

    /**
     * Returns the digit at the specified {@code index} in the number's string representation.
     *
     * @param index the index of the digit
     * @return the digit as an integer
     */
    public int getDigit(int index) {
        char c = value.charAt(index);
        int digit = Character.digit(c, (int) radix.getBase());
        if (digit == -1) {
            throw new RadixNumberFormatException(radix, c);
        }
        return digit;
    }

    /**
     * Constructs a Number object with the given {@code value} in decimal radix.
     *
     * @param value the string representation of the number
     */
    public Number(String value) {
        this(value, Radix.DECIMAL);
    }

    /**
     * Constructs a Number object with the given double {@code value} in decimal radix.
     *
     * @param value the double value
     */
    public Number(double value) {
        this(String.valueOf(value), Radix.DECIMAL);
    }

    /**
     * Constructs a Number object with the given {@code value} and {@code radix}.
     *
     * @param value the string representation of the number
     * @param radix the radix of the number
     */
    public Number(String value, Radix radix) {
        this.value = value;
        this.radix = radix;

        int length = value.length();
        if (value.contains(Complex.I.toString())) {
            complex = true;
            length--;
        }

        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c == '.' || c == '-') {
                continue;
            }

            if (getDigit(i) > radix.getMaxDigit()) {
                throw new RadixNumberFormatException(radix, c);
            }
        }
    }

    /**
     * Constructs a Number object with the given character {@code sequence} and {@code radix}.
     *
     * @param sequence the character sequence representation of the number
     * @param radix    the radix of the number
     */
    public Number(CharSequence sequence, Radix radix) {
        this(sequence.toString(), radix);
    }

    public <M extends Measure, U extends Unit<M, U>>
    Number transform(U from, U to) {
        return new Number(from.transform(doubleValue(), to));
    }

    /**
     * Transforms the number to a specified radix {@code to}
     *
     * @param to the target radix
     * @return the transformed number
     */
    public Number transform(Radix to) {
        if (to.getBase() == radix.getBase()) {
            return this;
        }

        StringBuilder result = new StringBuilder();
        String normalizedValue = normalize().value;
        double val;

        if (complex) {
            if (!normalizedValue.contentEquals(Complex.I.toString())) {
                normalizedValue = normalizedValue.substring(0, normalizedValue.length() - 1);
                val = Double.parseDouble(normalizedValue);
            } else {
                val = 1;
            }
        } else {
            val = Double.parseDouble(normalizedValue);
        }

        long integerPart = (long) val;
        double fractionalPart = val - integerPart;

        result.append(convertIntegerPartToRadix(integerPart, to));

        if (fractionalPart > 0) {
            result.append(".");
            result.append(convertFractionalPartToRadix(fractionalPart, to));
        }

        while (result.length() > 1 && result.charAt(0) == '0' && result.charAt(1) != '.') {
            result.deleteCharAt(0);
        }

        if (result.isEmpty()) {
            result.append('0');
        }

        if (complex) result.append(Complex.I);

        return new Number(result.toString(), to);
    }

    /**
     * Converts the integer part of the number to the specified radix.
     *
     * @param integerPart the integer part of the number
     * @param to          the target radix
     * @return the string representation of the converted integer part
     */
    private String convertIntegerPartToRadix(long integerPart, Radix to) {
        StringBuilder result = new StringBuilder();
        double n = (double) integerPart;
        int r = (int) to.getBase();

        while (n > EPSILON) {
            int digit = (int) (n % r);
            result.insert(0, Character.toTitleCase(Character.forDigit(digit, r)));
            n /= r;
        }

        return result.toString();
    }

    /**
     * Converts the fractional part of the number to the specified radix.
     *
     * @param fractionalPart the fractional part of the number
     * @param to             the target radix
     * @return the string representation of the converted fractional part
     */
    private String convertFractionalPartToRadix(double fractionalPart, Radix to) {
        StringBuilder result = new StringBuilder();
        int r = (int) to.getBase();

        double current = fractionalPart;
        for (int i = 0; i < MAX_FRACTION_DIGITS; i++) {
            current *= r;
            int digit = (int) current;
            result.append(Character.forDigit(digit, r));
            current -= digit;
            if (current < EPSILON) break;
        }

        return result.toString();
    }

    /**
     * Normalizes the number to decimal radix.
     *
     * @return the normalized number
     */
    private Number normalize() {
        if (radix.getBase() == 10.0) {
            return this;
        }

        String value;
        if (complex) {
            if (!this.value.contentEquals(Complex.I.toString()))
                value = this.value.substring(0, this.value.length() - 1);
            else
                value = "1";
        } else {
            value = this.value;
        }

        int decimalPoint = value.indexOf(".");
        double result = 0;

        if (decimalPoint == -1) {
            int length = value.length();
            for (int i = 0; i < length; i++) {
                int raise = length - i - 1;
                int digit = getDigit(i);
                result += digit * Math.pow(radix.getBase(), raise);
            }

        } else {
            int length = value.substring(0, decimalPoint).length();

            for (int i = 0; i < decimalPoint; i++) {
                int raise = length - i - 1;
                int digit = getDigit(i);
                result += digit * Math.pow(radix.getBase(), raise);
            }

            int n = 0;
            for (int i = decimalPoint + 1; i < value.length(); i++) {
                n++;
                int raise = -n;
                int digit = getDigit(i);
                result += digit * Math.pow(radix.getBase(), raise);
            }
        }

        String plainString = new BigDecimal(result).toPlainString();
        if (complex) plainString += Complex.I;

        return new Number(plainString, Radix.DECIMAL);
    }

    @Override
    public String toString() {
        return "Number{" +
                "value='" + value + '\'' +
                ", radix=" + radix +
                '}';
    }

    public String getValue() {
        return value;
    }

    /**
     *
     * @return Plain presentation of this number in decimal radix
     */
    public String getPlain() {
        String value = transform(Radix.DECIMAL).value;
        if (complex) {
            if (!value.contentEquals(Complex.I.toString()))
                value = value.substring(0, value.length() - 1);
            else
                value = "1";
        }

        String plainString = new BigDecimal(value).toPlainString();
        return complex ? plainString + Complex.I : plainString;
    }

    public Radix getRadix() {
        return radix;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        Number that = (Number) obj;
        return Objects.equals(value, that.value) &&
                Objects.equals(radix, that.radix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, radix);
    }

    public boolean isComplex() {
        return complex;
    }

    @Override
    public int intValue() {
        return complexValue().intValue();
    }

    @Override
    public long longValue() {
        return complexValue().longValue();
    }

    @Override
    public float floatValue() {
        return complexValue().floatValue();
    }

    @Override
    public double doubleValue() {
        return complexValue().doubleValue();
    }

    public Complex complexValue() {
        return Complex.parseComplex(getPlain());
    }
}
