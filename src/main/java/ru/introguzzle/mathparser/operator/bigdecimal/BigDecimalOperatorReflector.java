package ru.introguzzle.mathparser.operator.bigdecimal;

import ru.introguzzle.mathparser.common.reflect.Reflector;

import java.util.HashMap;
import java.util.Map;

public class BigDecimalOperatorReflector {
    private static final Map<String, BigDecimalOperator> OPERATORS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.operator.bigdecimal";
    private static final Reflector<BigDecimalOperator> REFLECTOR = new Reflector<>(BigDecimalOperator.class);

    private static void load() {
        OPERATORS.putAll(REFLECTOR.getInstances(PACKAGE));
    }

    public static Map<String, BigDecimalOperator> get() {
        if (OPERATORS.isEmpty()) {
            load();
        }

        return OPERATORS;
    }
}
