package ru.introguzzle.mathparser.common.math;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberTest {

    @Test
    public void testDecimalToBinary() {
        Number decimalNumber = new Number("10");
        Number binaryNumber = decimalNumber.transform(Radix.BINARY);
        assertEquals("1010", binaryNumber.getValue());
    }

    @Test
    public void testBinaryToDecimal() {
        Number binaryNumber = new Number("1010", Radix.BINARY);
        Number decimalNumber = binaryNumber.transform(Radix.DECIMAL);
        assertEquals("10", decimalNumber.getPlain());
    }

    @Test
    public void testDecimalToHexadecimal() {
        Number decimalNumber = new Number("255");
        Number hexNumber = decimalNumber.transform(Radix.HEXADECIMAL);
        assertEquals("FF", hexNumber.getValue());
    }

    @Test
    public void testHexadecimalToDecimal() {
        Number hexNumber = new Number("FF", Radix.HEXADECIMAL);
        Number decimalNumber = hexNumber.transform(Radix.DECIMAL);
        assertEquals("255", decimalNumber.getPlain());
    }

    @Test
    public void testComplexNumberDecimalToBinary() {
        Number complexNumber = new Number("10i");
        Number binaryNumber = complexNumber.transform(Radix.BINARY);
        assertEquals("1010i", binaryNumber.getValue());
    }

    @Test
    public void testComplexNumberBinaryToDecimal() {
        Number complexBinaryNumber = new Number("1010i", Radix.BINARY);
        Number decimalNumber = complexBinaryNumber.transform(Radix.DECIMAL);
        assertEquals("10i", decimalNumber.getPlain());
    }

    @Test
    public void testComplexNumberOperations() {
        Number complexNumber = new Number("5.5i");
        assertTrue(complexNumber.isComplex());
        assertEquals("5.5i", complexNumber.getValue());

        Number transformedComplex = complexNumber.transform(Radix.BINARY);
        assertEquals("101.1i", transformedComplex.getValue());
    }

    @Test
    public void testInvalidDigitInRadix() {
        Radix radix = new Radix(8);
        assertThrows(RadixNumberFormatException.class, () -> {
            new Number("19", radix);
        });
    }

    @Test
    public void testNegativeNumbers() {
        Number negativeNumber = new Number("-255");
        assertEquals("-255", negativeNumber.getValue());
    }

    @Test
    public void testFractionalNumber() {
        Number fractionalNumber = new Number("10.75");
        Number binaryFractional = fractionalNumber.transform(Radix.BINARY);
        assertEquals("1010.11", binaryFractional.getValue());

        Number decimalFractional = binaryFractional.transform(Radix.DECIMAL);
        assertEquals("10.75", decimalFractional.getPlain());
    }

    @Test
    public void testEqualsAndHashCode() {
        Number number1 = new Number("123", Radix.DECIMAL);
        Number number2 = new Number("123", Radix.DECIMAL);
        assertEquals(number1, number2);
        assertEquals(number1.hashCode(), number2.hashCode());
    }

    @Test
    public void testGetPlain() {
        Number number = new Number("123", Radix.DECIMAL);
        assertEquals("123", number.getPlain());

        Number complexNumber = new Number("10i", Radix.DECIMAL);
        assertEquals("10i", complexNumber.getPlain());
    }
}
