package ru.introguzzle.mathparser.function.real;

import java.util.HashMap;
import java.util.Map;

import ru.introguzzle.mathparser.common.Reflector;

public final class DoubleFunctionReflector {

    private static final Map<String, DoubleFunction> FUNCTIONS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.function.real";
    private static final Reflector<DoubleFunction> REFLECTOR = new Reflector<>(DoubleFunction.class);

    private static void load() {
        FUNCTIONS.putAll(REFLECTOR.getInstances(PACKAGE));
    }

    public static Map<String, DoubleFunction> get() {
        if (FUNCTIONS.isEmpty()) {
            load();
        }

        return FUNCTIONS;
    }
}
