package ru.introguzzle.mathparser.parse;

import ru.introguzzle.mathparser.complex.Complex;

public interface NumberConverter<T extends Number> {
    T convert(String string);
    default T convert(CharSequence sequence) {
        return convert(sequence.toString());
    }

    static NumberConverter<Double> getDoubleConverter() {
        return Double::parseDouble;
    }

    static NumberConverter<Integer> getIntegerConverter() {
        return Integer::parseInt;
    }

    static NumberConverter<Complex> getComplexConverter() {
        return Complex::parseComplex;
    }
}
