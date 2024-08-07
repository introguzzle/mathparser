package ru.introguzzle.mathparser.common.math;

import java.util.Locale;
import java.util.Map;

public class Radix {
    private static final Map<Integer, Character> ADDITIONAL = Map.of(
        10, 'A',
        11, 'B',
        12, 'C',
        13, 'D',
        14, 'E',
        15, 'F'
    );

    private static final double EPSILON = 1E-6;
    private final double radix;
    private final int maxDigit;

    public Radix(double radix) {
        if (radix <= 0) {
            throw new IllegalArgumentException("Radix must be greater than 0");
        }

        this.radix = radix;
        this.maxDigit = ((int) radix) - 1;
    }

    public double getRadix() {
        return radix;
    }

    public double getMaxDigit() {
        return maxDigit;
    }

    public String convert(double number) {
        if (getRadix() == 10.0) {
            return Double.toString(number);
        }

        StringBuilder result = new StringBuilder();

        long integerPart = (long) number;
        double actualFractionalPart = number - integerPart;
        long converted = Long.parseLong(String.valueOf(number - integerPart).substring(2));

        result.append(convertIntegerPartToRadix(integerPart));

        if (actualFractionalPart > 0) {
            result.append(".");
            result.append(convertFractionalPartToRadix(converted));
        }

        while (result.length() > 1 && result.charAt(0) == '0' && result.charAt(1) != '.') {
            result.deleteCharAt(0);
        }

        return result.toString();
    }

    private double convertToTen(String number) {
        if (getRadix() == 10.0) {
            return Double.parseDouble(number);
        }

        int decimalPoint = number.indexOf(".");
        double value = 0;

        if (decimalPoint == -1) {
            int length = number.length();
            for (int i = 0; i < length; i++) {
                int base = length - i - 1;
                int digit = number.charAt(i) - '0';
                if (digit > maxDigit) {
                    throw newException(digit);
                }

                value += digit * Math.pow(getRadix(), base);
            }

        } else {
            int length = number.substring(0, decimalPoint).length();

            for (int i = 0; i < decimalPoint; i++) {
                int base = length - i - 1;
                int digit = number.charAt(i) - '0';
                if (digit > maxDigit) {
                    throw newException(digit);
                }

                value += digit * Math.pow(getRadix(), base);
            }

            int n = 0;
            for (int i = decimalPoint + 1; i < number.length(); i++) {
                n++;
                int base = -n;
                int digit = number.charAt(i) - '0';
                if (digit > maxDigit) {
                    throw newException(digit);
                }

                value += digit * Math.pow(getRadix(), base);
            }
        }

        return value;
    }

    private String convertIntegerPartToRadix(long integerPart) {
        StringBuilder result = new StringBuilder();
        double n = (double) integerPart;

        while (n > EPSILON) {
            int digit = (int) (n % radix);
            result.insert(0, Character.toTitleCase(Character.forDigit(digit, (int) radix)));
            n /= radix;
        }

        return result.toString();
    }

    private String convertFractionalPartToRadix(long fractionalPart) {
        return "";
    }

    private NumberFormatException newException(int digit) {
        String format = "Digit: %d is not allowed in radix %f";
        return new NumberFormatException(String.format(Locale.US, format, digit, getRadix()));
    }

    public static void main(String[] args) {
        Radix r = new Radix(2);
        r.convert(120210);
    }
}
