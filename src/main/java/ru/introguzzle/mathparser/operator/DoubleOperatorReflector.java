package ru.introguzzle.mathparser.operator;

import ru.introguzzle.mathparser.common.Reflector;

import java.util.HashMap;
import java.util.Map;

public class DoubleOperatorReflector {
    private static final Map<String, DoubleOperator> OPERATORS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.operator";
    private static final Reflector<DoubleOperator> REFLECTOR = new Reflector<>(DoubleOperator.class);

    private static void load() {
        OPERATORS.putAll(REFLECTOR.getInstances(PACKAGE));
    }

    public static Map<String, DoubleOperator> get() {
        if (OPERATORS.isEmpty()) {
            load();
        }

        return OPERATORS;
    }
}
