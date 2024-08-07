package ru.introguzzle.mathparser.common.math;

import java.util.Objects;

public class Number {
    private final String value;
    private final Radix radix;

    public Number(String value, Radix radix) {
        this.value = value;
        this.radix = radix;

        int length = value.length();
        for (int i = 0; i < length; i++) {
            if (value.charAt(i) > getRadix().getMaxDigit()) {
                throw new RadixNumberFormatException(radix, value.charAt(i));
            }
        }
    }

    public String getValue() {
        return value;
    }

    public Radix getRadix() {
        return radix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Number number = (Number) o;
        return Objects.equals(value, number.value) && Objects.equals(radix, number.radix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, radix);
    }

    @Override
    public String toString() {
        return "Number{" +
                "value='" + value + '\'' +
                ", radix=" + radix +
                '}';
    }

    public static void main(String[] args) {
        Number a = new Number("124343", new Radix(2));
        System.out.println(a);
    }
}
