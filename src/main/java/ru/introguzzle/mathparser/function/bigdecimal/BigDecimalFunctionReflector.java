package ru.introguzzle.mathparser.function.bigdecimal;

import ru.introguzzle.mathparser.common.reflect.Reflector;

import java.util.HashMap;
import java.util.Map;

public class BigDecimalFunctionReflector {
    private static final Map<String, BigDecimalFunction> FUNCTIONS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.function.bigdecimal";
    private static final Reflector<BigDecimalFunction> REFLECTOR = new Reflector<>(BigDecimalFunction.class);

    private static void load() {
        FUNCTIONS.putAll(REFLECTOR.getInstances(PACKAGE));
    }

    public static Map<String, BigDecimalFunction> get() {
        if (FUNCTIONS.isEmpty()) {
            load();
        }

        return FUNCTIONS;
    }
}
