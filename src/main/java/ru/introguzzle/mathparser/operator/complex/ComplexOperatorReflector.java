package ru.introguzzle.mathparser.operator.complex;

import ru.introguzzle.mathparser.common.Reflector;

import java.util.HashMap;
import java.util.Map;

public class ComplexOperatorReflector {
    private static final Map<String, ComplexOperator> OPERATORS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.operator";
    private static final Reflector<ComplexOperator> REFLECTOR = new Reflector<>(ComplexOperator.class);

    private static void load() {
        OPERATORS.putAll(REFLECTOR.getInstances(PACKAGE));
    }

    public static Map<String, ComplexOperator> get() {
        if (OPERATORS.isEmpty()) {
            load();
        }

        return OPERATORS;
    }
}

