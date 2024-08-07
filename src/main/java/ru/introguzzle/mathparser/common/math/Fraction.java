package ru.introguzzle.mathparser.common.math;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Fraction {
    private final BigInteger numerator;
    private final BigInteger denominator;

    public Fraction(BigInteger numerator, BigInteger denominator) {
        BigInteger gcd = numerator.gcd(denominator);
        this.numerator = numerator.divide(gcd);
        this.denominator = denominator.divide(gcd);
    }

    public Fraction(double number) {
        BigDecimal decimal = BigDecimal.valueOf(number);
        int scale = decimal.scale();
        BigInteger unscaled = decimal.unscaledValue();
        BigInteger denominator = BigInteger.TEN.pow(scale);
        BigInteger gcd = unscaled.gcd(denominator);

        this.numerator = unscaled.divide(gcd);
        this.denominator = denominator.divide(gcd);
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public BigDecimal divide() {
        return BigDecimal.valueOf(numerator.doubleValue() / denominator.doubleValue());
    }

    @Override
    public String toString() {
        return numerator + " / " + denominator;
    }
}
