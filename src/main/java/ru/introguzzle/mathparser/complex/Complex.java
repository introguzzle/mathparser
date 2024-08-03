package ru.introguzzle.mathparser.complex;

import java.util.Objects;

public class Complex extends Number {
    private final double r;
    private final double i;

    public Complex(double real) {
        r = real;
        i = 0.0;
    }

    public Complex(double real, double imaginary) {
        r = real;
        i = imaginary;
    }

    public double getReal() {
        return r;
    }

    public double getImaginary() {
        return i;
    }

    public Complex add(Complex other) {
        return new Complex(r + other.r, i + other.i);
    }

    public Complex subtract(Complex other) {
        return new Complex(r - other.r, i - other.i);
    }

    public Complex multiply(Complex other) {
        double newReal = r * other.r - i * other.i;
        double newImaginary = r * other.i + i * other.r;
        return new Complex(newReal, newImaginary);
    }

    public Complex divide(Complex other) {
        double denominator = other.r * other.r + other.i * other.i;
        double newReal = (r * other.r + i * other.i) / denominator;
        double newImaginary = (i * other.r - r * other.i) / denominator;
        return new Complex(newReal, newImaginary);
    }

    public double magnitude() {
        return Math.sqrt(r * r + i * i);
    }

    public double phase() {
        return Math.atan2(i, r);
    }

    @Override
    public String toString() {
        return r + " + " + i + "i";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Complex complex = (Complex) obj;
        return Double.compare(complex.r, r) == 0 &&
                Double.compare(complex.i, i) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, i);
    }

    @Override
    public int intValue() {
        return (int) r;
    }

    @Override
    public long longValue() {
        return (long) r;
    }

    @Override
    public float floatValue() {
        return (float) r;
    }

    @Override
    public double doubleValue() {
        return r;
    }
}
