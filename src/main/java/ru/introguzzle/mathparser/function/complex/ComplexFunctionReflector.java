package ru.introguzzle.mathparser.function.complex;

import ru.introguzzle.mathparser.common.Reflector;

import java.util.HashMap;
import java.util.Map;

public class ComplexFunctionReflector {

    private static final Map<String, ComplexFunction> FUNCTIONS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.function.complex";
    private static final Reflector<ComplexFunction> REFLECTOR = new Reflector<>(ComplexFunction.class);

    private static void load() {
        FUNCTIONS.putAll(REFLECTOR.getInstances(PACKAGE));
    }

    public static Map<String, ComplexFunction> get() {
        if (FUNCTIONS.isEmpty()) {
            load();
        }

        return FUNCTIONS;
    }
}
