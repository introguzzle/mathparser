package ru.introguzzle.mathparser.common.math;

import java.math.BigDecimal;
import java.util.Objects;

public class Number {
    public static final double EPSILON = 1E-8;
    public static final int MAX_FRACTION_DIGITS = 12;
    private final String value;
    private final Radix radix;
    private boolean complex = false;

    public int getDigit(int index) {
        char c = value.charAt(index);
        int digit = Character.digit(c, (int) radix.getRadix());

        if (c == 'i') {
            complex = true;
        }

        if (digit == -1 && c != 'i') {
            throw new RadixNumberFormatException(radix, c);
        }

        return digit;
    }

    public Number(String value) {
        this(value, Radix.DECIMAL);
    }

    public Number(double value) {
        this(String.valueOf(value), Radix.DECIMAL);
    }

    public Number(String value, Radix radix) {
        this.value = value;
        this.radix = radix;

        int length = value.length();
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c == '.') {
                continue;
            }

            if (getDigit(i) > radix.getMaxDigit()) {
                throw new RadixNumberFormatException(radix, c);
            }
        }
    }

    public Number(CharSequence sequence, Radix radix) {
        this(sequence.toString(), radix);
    }

    public Number transform(Radix to) {
        if (to.getRadix() == radix.getRadix()) {
            return this;
        }

        StringBuilder result = new StringBuilder();
        double value = Double.parseDouble(normalize().value);

        long integerPart = (long) value;
        double fractionalPart = value - integerPart;

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

        return new Number(new BigDecimal(result.toString()).toPlainString(), to);
    }

    private String convertIntegerPartToRadix(long integerPart, Radix to) {
        StringBuilder result = new StringBuilder();
        double n = (double) integerPart;
        int r = (int) to.getRadix();

        while (n > EPSILON) {
            int digit = (int) (n % r);
            result.insert(0, Character.toTitleCase(Character.forDigit(digit, r)));
            n /= r;
        }

        return result.toString();
    }

    private String convertFractionalPartToRadix(double fractionalPart, Radix to) {
        StringBuilder result = new StringBuilder();
        int r = (int) to.getRadix();

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

    private Number normalize() {
        if (radix.getRadix() == 10.0) {
            return this;
        }

        int decimalPoint = value.indexOf(".");
        double result = 0;

        if (decimalPoint == -1) {
            int length = value.length();
            for (int i = 0; i < length; i++) {
                int base = length - i - 1;
                int digit = getDigit(i);
                result += digit * Math.pow(radix.getRadix(), base);
            }

        } else {
            int length = value.substring(0, decimalPoint).length();

            for (int i = 0; i < decimalPoint; i++) {
                int base = length - i - 1;
                int digit = getDigit(i);
                result += digit * Math.pow(radix.getRadix(), base);
            }

            int n = 0;
            for (int i = decimalPoint + 1; i < value.length(); i++) {
                n++;
                int base = -n;
                int digit = getDigit(i);
                result += digit * Math.pow(radix.getRadix(), base);
            }
        }

        return new Number(new BigDecimal(result).toPlainString(), new Radix(10));
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

    public String getPlain() {
        return new BigDecimal(transform(Radix.DECIMAL).value).toPlainString();
    }

    public Radix getRadix() {
        return radix;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        var that = (Number) obj;
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
}
