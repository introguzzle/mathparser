package ru.introguzzle.mathparser.common.math;

public class Radix {
    public static final Radix DECIMAL = new Radix(10);

    public static final Radix BINARY = new Radix(2);
    public static final Radix QUATERNARY = new Radix(4);
    public static final Radix OCTAL = new Radix(8);
    public static final Radix HEXADECIMAL = new Radix(16);

    public static final Radix TERNARY = new Radix(3);
    public static final Radix PENTAL = new Radix(5);

    private final double radix;
    private final int maxDigit;

    public Radix(double radix) {
        if (radix < 2) {
            throw new IllegalArgumentException("Radix must be at least 2");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Radix r = (Radix) o;
        return Double.compare(radix, r.radix) == 0 && maxDigit == r.maxDigit;
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(radix);
        result = 31 * result + maxDigit;
        return result;
    }

    @Override
    public String toString() {
        return "Radix" + radix;
    }
}
