package ru.introguzzle.mathparser.common.math;

/**
 * Represents a numerical base (radix) for number systems.
 * <p>
 * The Radix class defines different base systems (e.g., binary, decimal, hexadecimal)
 * and allows creating custom radices with specified bases.
 * </p>
 */
public class Radix {
    /**
     * The decimal radix (base 10).
     */
    public static final Radix DECIMAL = new Radix(10);

    /**
     * The binary radix (base 2).
     */
    public static final Radix BINARY = new Radix(2);

    /**
     * The quaternary radix (base 4).
     */
    public static final Radix QUATERNARY = new Radix(4);

    /**
     * The octal radix (base 8).
     */
    public static final Radix OCTAL = new Radix(8);

    /**
     * The hexadecimal radix (base 16).
     */
    public static final Radix HEXADECIMAL = new Radix(16);

    /**
     * The ternary radix (base 3).
     */
    public static final Radix TERNARY = new Radix(3);

    /**
     * The pental radix (base 5).
     */
    public static final Radix PENTAL = new Radix(5);

    /**
     * The base (radix) of the number system.
     */
    private final double base;

    /**
     * The maximum digit value allowed in this radix.
     * <p>
     * For example, in the decimal system (base 10), the maximum digit is 9.
     * </p>
     */
    private final int maxDigit;

    /**
     * Constructs a Radix with the specified base.
     *
     * @param base The base for the radix. Must be at least 2.
     * @throws IllegalArgumentException If the base is less than 2.
     */
    public Radix(double base) throws IllegalArgumentException {
        if (base < 2) {
            throw new IllegalArgumentException("Radix must be at least 2");
        }

        this.base = base;
        this.maxDigit = ((int) base) - 1;
    }

    /**
     *
     * @param base The base for the radix. Must be at least 2.
     * @return Radix with specified {@code base}
     * @throws IllegalArgumentException If the base is less than 2.
     */
    public static Radix of(double base) throws IllegalArgumentException {
        return new Radix(base);
    }

    /**
     * Returns the base of this radix.
     *
     * @return The base of the radix.
     */
    public double getBase() {
        return base;
    }

    /**
     * Returns the maximum digit allowed in this radix.
     *
     * @return The maximum digit value.
     */
    public double getMaxDigit() {
        return maxDigit;
    }

    /**
     * Compares this Radix with another object for equality.
     * <p>
     * Two Radix objects are considered equal if their bases and maximum digits are the same.
     * </p>
     *
     * @param o The object to compare with.
     * @return true if the objects are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Radix r = (Radix) o;
        return Double.compare(base, r.base) == 0 && maxDigit == r.maxDigit;
    }

    /**
     * Returns a hash code for this Radix.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        int result = Double.hashCode(base);
        result = 31 * result + maxDigit;
        return result;
    }

    /**
     * Returns a string representation of this Radix.
     * <p>
     * The string includes the word "Radix" followed by the base value.
     * </p>
     *
     * @return A string representation of this Radix.
     */
    @Override
    public String toString() {
        return "Radix" + base;
    }
}
