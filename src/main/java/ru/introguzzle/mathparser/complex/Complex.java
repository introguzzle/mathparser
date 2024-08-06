package ru.introguzzle.mathparser.complex;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

public final class Complex extends Number {
    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex NAN = new Complex(Double.NaN, 0);
    public static final Complex POSITIVE_INFINITY = new Complex(Double.POSITIVE_INFINITY, 0);
    public static final Complex NEGATIVE_INFINITY = new Complex(Double.NEGATIVE_INFINITY, 0);
    public static final Complex E = new Complex(Math.E, 0);
    public static final Complex PI = new Complex(Math.PI, 0);

    private final double a;
    private final double b;

    public Complex(double real) {
        a = real;
        b = 0.0;
    }

    public Complex(double real, double imaginary) {
        a = real;
        b = imaginary;
    }

    public double getReal() {
        return a;
    }

    public double getImaginary() {
        return b;
    }

    public Complex add(Complex other) {
        return new Complex(a + other.a, b + other.b);
    }

    public Complex subtract(Complex other) {
        return new Complex(a - other.a, b - other.b);
    }

    public Complex multiply(Complex other) {
        double newReal = a * other.a - b * other.b;
        double newImaginary = a * other.b + b * other.a;
        return new Complex(newReal, newImaginary);
    }

    public Complex divide(Complex other) {
        double denominator = other.a * other.a + other.b * other.b;
        double newReal = (a * other.a + b * other.b) / denominator;
        double newImaginary = (b * other.a - a * other.b) / denominator;
        return new Complex(newReal, newImaginary);
    }

    public double magnitude() {
        return Math.hypot(a, b);
    }

    public double phase() {
        return Math.atan2(b, a);
    }

    public Complex log() {
        return new Complex(Math.log(magnitude()), phase());
    }

    public Complex pow(Complex power) {
        Complex l = log();
        double real = l.a * power.a - l.b * power.b;
        double imaginary = l.a * power.b + l.b * power.a;
        return new Complex(Math.exp(real) * Math.cos(imaginary), Math.exp(real) * Math.sin(imaginary));
    }

    public Complex sin() {
        double real = Math.sin(a) * Math.cosh(b);
        double imaginary = Math.cos(a) * Math.sinh(b);

        return new Complex(real, imaginary);
    }

    public Complex cos() {
        double real = Math.cos(a) * Math.cosh(b);
        double imaginary = Math.sin(a) * Math.sinh(b);

        return new Complex(real, imaginary);
    }

    public Complex sinh() {
        double real = Math.cos(b) * Math.sinh(a);
        double imaginary = Math.sin(b) * Math.cosh(a);

        return new Complex(real, imaginary);
    }

    public Complex cosh() {
        double real = Math.cos(b) * Math.cosh(a);
        double imaginary = Math.sin(b) * Math.sinh(a);

        return new Complex(real, imaginary);
    }

    public Complex tan() {
        return sin().divide(cos());
    }

    public Complex tanh() {
        return sinh().divide(cosh());
    }

    public Complex conjugate() {
        return new Complex(a, -b);
    }

    public Complex reciprocal() {
        double scale = a * a + b * b;
        return new Complex(a / scale, b / scale);
    }

    private static String format(double value) {
        return BigDecimal.valueOf(value).toPlainString();
    }

    @Override
    public String toString() {
        return b > 0
                ? format(a) + " + " + format(b) + "i"
                : format(a) + " - " + format(Math.abs(b)) + "i";
    }

    public String toExponential() {
        return format(magnitude()) + " * (e ** " + format(phase()) + "i)";
    }

    public String toTrigonometric() {
        double phase = phase();
        return String.format(
                Locale.US, "%s * (sin(%s) * i * cos(%s))",
                format(magnitude()),
                format(phase),
                format(phase)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Complex complex = (Complex) obj;
        return Double.compare(complex.a, a) == 0 &&
                Double.compare(complex.b, b) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public int intValue() {
        return (int) a;
    }

    @Override
    public long longValue() {
        return (long) a;
    }

    @Override
    public float floatValue() {
        return (float) a;
    }

    @Override
    public double doubleValue() {
        return a;
    }
}
