package ru.introguzzle.mathparser.complex;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

/**
 * Immutable class representing a complex math number.
 * <p>
 * This class provides methods for common mathematical operations on complex numbers.
 * All methods that modify the state of the complex number return a new instance,
 * preserving the immutability of the class.
 * </p>
 */
public class Complex extends Number {
    @Serial
    private static final long serialVersionUID = -2667358153829885232L;

    // Constants representing common complex numbers
    public static final Character I = 'i';

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex NAN = new Complex(Double.NaN, 0);
    public static final Complex POSITIVE_INFINITY = new Complex(Double.POSITIVE_INFINITY, 0);
    public static final Complex NEGATIVE_INFINITY = new Complex(Double.NEGATIVE_INFINITY, 0);
    public static final Complex E = new Complex(Math.E, 0);
    public static final Complex PI = new Complex(Math.PI, 0);

    /**
     * Real part
     */
    private final double a;

    /**
     * Imaginary part
     */
    private final double b;

    /**
     * Static factory method to create a complex number with only a real part.
     *
     * @param a The real part.
     * @return A new Complex instance with the specified real part.
     */
    public static Complex of(double a) {
        return new Complex(a);
    }

    /**
     * Static factory method to create a complex number with real and imaginary parts.
     *
     * @param a The real part.
     * @param b The imaginary part.
     * @return A new Complex instance with the specified real and imaginary parts.
     */
    public static Complex of(double a, double b) {
        return new Complex(a, b);
    }

    /**
     * Parses a string representation of a complex number.
     * <p>
     * The string can be in the format of a real number or an imaginary number with the suffix 'i'.
     * </p>
     *
     * @param string The string to parse.
     * @return A new Complex instance parsed from the string.
     */
    public static Complex parseComplex(String string) {
        return !string.contains(I.toString())
                ? Complex.of(Double.parseDouble(string))
                : Complex.of(0, Double.parseDouble(string.substring(0, string.length() - 1)));
    }

    /**
     * Constructs a complex number with only a real part.
     *
     * @param real The real part.
     */
    public Complex(double real) {
        a = real;
        b = 0.0;
    }

    /**
     * Constructs a complex number with specified real and imaginary parts.
     *
     * @param real      The real part.
     * @param imaginary The imaginary part.
     */
    public Complex(double real, double imaginary) {
        a = real;
        b = imaginary;
    }

    /**
     * Returns the real part of this complex number.
     *
     * @return The real part.
     */
    public double getReal() {
        return a;
    }

    /**
     * Returns the imaginary part of this complex number.
     *
     * @return The imaginary part.
     */
    public double getImaginary() {
        return b;
    }

    /**
     * Adds another complex number to this complex number.
     * <p>
     * This method returns a new Complex instance with the result of the addition.
     * </p>
     *
     * @param other The other complex number to add.
     * @return A new Complex instance representing the sum.
     */
    public Complex add(Complex other) {
        return new Complex(a + other.a, b + other.b);
    }

    /**
     * Subtracts another complex number from this complex number.
     * <p>
     * This method returns a new Complex instance with the result of the subtraction.
     * </p>
     *
     * @param other The other complex number to subtract.
     * @return A new Complex instance representing the difference.
     */
    public Complex subtract(Complex other) {
        return new Complex(a - other.a, b - other.b);
    }

    /**
     * Multiplies this complex number by another complex number.
     * <p>
     * This method returns a new Complex instance with the result of the multiplication.
     * </p>
     *
     * @param other The other complex number to multiply with.
     * @return A new Complex instance representing the product.
     */
    public Complex multiply(Complex other) {
        double newReal = a * other.a - b * other.b;
        double newImaginary = a * other.b + b * other.a;
        return new Complex(newReal, newImaginary);
    }

    /**
     * Divides this complex number by another complex number.
     * <p>
     * This method returns a new Complex instance with the result of the division.
     * </p>
     *
     * @param other The other complex number to divide by.
     * @return A new Complex instance representing the quotient.
     */
    public Complex divide(Complex other) {
        double denominator = other.a * other.a + other.b * other.b;
        double newReal = (a * other.a + b * other.b) / denominator;
        double newImaginary = (b * other.a - a * other.b) / denominator;
        return new Complex(newReal, newImaginary);
    }

    /**
     * Calculates the magnitude (or modulus) of this complex number.
     *
     * @return The magnitude of this complex number.
     */
    public double magnitude() {
        return Math.hypot(a, b);
    }

    /**
     * Calculates the phase (or argument) of this complex number.
     *
     * @return The phase of this complex number, in radians.
     */
    public double phase() {
        return Math.atan2(b, a);
    }

    /**
     * Computes the logarithm of this complex number to the specified base.
     * <p>
     * This method returns a new Complex instance representing the logarithm.
     * </p>
     *
     * @param base The base for the logarithm.
     * @return A new Complex instance representing the logarithm.
     */
    public Complex log(Complex base) {
        return ln().divide(base.ln());
    }

    /**
     * Computes the natural logarithm (base e) of this complex number.
     * <p>
     * This method returns a new Complex instance representing the natural logarithm.
     * </p>
     *
     * @return A new Complex instance representing the natural logarithm.
     */
    public Complex ln() {
        return new Complex(Math.log(magnitude()), phase());
    }

    /**
     * Raises this complex number to the power of another complex number.
     * <p>
     * This method returns a new Complex instance representing the result.
     * </p>
     *
     * @param power The exponent.
     * @return A new Complex instance representing the result of the exponentiation.
     */
    public Complex pow(Complex power) {
        Complex ln = ln();
        double real = ln.a * power.a - ln.b * power.b;
        double imaginary = ln.a * power.b + ln.b * power.a;
        return new Complex(Math.exp(real) * Math.cos(imaginary), Math.exp(real) * Math.sin(imaginary));
    }

    /**
     * Calculates the sine of this complex number.
     * <p>
     * This method returns a new Complex instance representing the sine.
     * </p>
     *
     * @return A new Complex instance representing the sine of this complex number.
     */
    public Complex sin() {
        double real = Math.sin(a) * Math.cosh(b);
        double imaginary = Math.cos(a) * Math.sinh(b);

        return new Complex(real, imaginary);
    }

    /**
     * Calculates the cosine of this complex number.
     * <p>
     * This method returns a new Complex instance representing the cosine.
     * </p>
     *
     * @return A new Complex instance representing the cosine of this complex number.
     */
    public Complex cos() {
        double real = Math.cos(a) * Math.cosh(b);
        double imaginary = Math.sin(a) * Math.sinh(b);

        return new Complex(real, imaginary);
    }

    /**
     * Calculates the hyperbolic sine of this complex number.
     * <p>
     * This method returns a new Complex instance representing the hyperbolic sine.
     * </p>
     *
     * @return A new Complex instance representing the hyperbolic sine of this complex number.
     */
    public Complex sinh() {
        double real = Math.cos(b) * Math.sinh(a);
        double imaginary = Math.sin(b) * Math.cosh(a);

        return new Complex(real, imaginary);
    }

    /**
     * Calculates the hyperbolic cosine of this complex number.
     * <p>
     * This method returns a new Complex instance representing the hyperbolic cosine.
     * </p>
     *
     * @return A new Complex instance representing the hyperbolic cosine of this complex number.
     */
    public Complex cosh() {
        double real = Math.cos(b) * Math.cosh(a);
        double imaginary = Math.sin(b) * Math.sinh(a);

        return new Complex(real, imaginary);
    }

    /**
     * Calculates the tangent of this complex number.
     * <p>
     * This method returns a new Complex instance representing the tangent.
     * </p>
     *
     * @return A new Complex instance representing the tangent of this complex number.
     */
    public Complex tan() {
        return sin().divide(cos());
    }

    /**
     * Calculates the hyperbolic tangent of this complex number.
     * <p>
     * This method returns a new Complex instance representing the hyperbolic tangent.
     * </p>
     *
     * @return A new Complex instance representing the hyperbolic tangent of this complex number.
     */
    public Complex tanh() {
        return sinh().divide(cosh());
    }

    /**
     * Returns the complex conjugate of this complex number.
     * <p>
     * This method returns a new Complex instance representing the conjugate.
     * </p>
     *
     * @return A new Complex instance representing the conjugate of this complex number.
     */
    public Complex conjugate() {
        return new Complex(a, -b);
    }

    /**
     * Returns the reciprocal of this complex number.
     * <p>
     * This method returns a new Complex instance representing the reciprocal.
     * </p>
     *
     * @return A new Complex instance representing the reciprocal of this complex number.
     */
    public Complex reciprocal() {
        double scale = a * a + b * b;
        return new Complex(a / scale, b / scale);
    }

    /**
     * Formats a double value as a plain string.
     * <p>
     * This method is used internally for representing double values in string format
     * without scientific notation.
     * </p>
     *
     * @param value The value to format.
     * @return The formatted string.
     */
    public static String format(double value) {
        return BigDecimal.valueOf(value).toPlainString();
    }

    /**
     * Returns a string representation of this complex number in the form "a + bi".
     *
     * @return The string representation of this complex number.
     */
    @Override
    public String toString() {
        return b >= 0
                ? format(a) + " + " + format(b) + I
                : format(a) + " - " + format(Math.abs(b)) + I;
    }

    /**
     * Returns a string representation of this complex number in exponential form.
     *
     * @return The exponential form of this complex number.
     */
    public String toExponential() {
        return format(magnitude()) + " * (e ** " + format(phase()) + "i)";
    }

    /**
     * Returns a string representation of this complex number in trigonometric form.
     *
     * @return The trigonometric form of this complex number.
     */
    public String toTrigonometric() {
        double phase = phase();
        return String.format(
                Locale.US, "%s * (sin(%s) * i * cos(%s))",
                format(magnitude()),
                format(phase),
                format(phase)
        );
    }

    /**
     * Compares this complex number to another for equality.
     * <p>
     * This method compares both the real and imaginary parts.
     * </p>
     *
     * @param complex The other complex number to compare with.
     * @return {@code true} if the complex numbers are equal, {@code false} otherwise.
     */
    public boolean equals(Complex complex) {
        return Double.compare(complex.a, a) == 0 &&
                Double.compare(complex.b, b) == 0;
    }

    /**
     * Compares this object to another for equality.
     * <p>
     * This method overrides the {@link Object#equals(Object)} method and ensures that
     * two Complex instances are equal if their real and imaginary parts are equal.
     * </p>
     *
     * @param obj The object to compare with.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Complex complex = (Complex) obj;
        return Double.compare(complex.a, a) == 0 &&
                Double.compare(complex.b, b) == 0;
    }

    /**
     * Returns a hash code for this complex number.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    /**
     * Returns the integer value of the real part of this complex number.
     *
     * @return The integer value of the real part.
     */
    @Override
    public int intValue() {
        return (int) a;
    }

    /**
     * Returns the long value of the real part of this complex number.
     *
     * @return The long value of the real part.
     */
    @Override
    public long longValue() {
        return (long) a;
    }

    /**
     * Returns the float value of the real part of this complex number.
     *
     * @return The float value of the real part.
     */
    @Override
    public float floatValue() {
        return (float) a;
    }

    /**
     * Returns the double value of the real part of this complex number.
     *
     * @return The double value of the real part.
     */
    @Override
    public double doubleValue() {
        return a;
    }

    /**
     * Returns the negated value of this complex number.
     * <p>
     * This method returns a new Complex instance with both the real and imaginary parts negated.
     * </p>
     *
     * @return A new Complex instance representing the negated value.
     */
    public Complex negate() {
        return new Complex(-a, -b);
    }
}
