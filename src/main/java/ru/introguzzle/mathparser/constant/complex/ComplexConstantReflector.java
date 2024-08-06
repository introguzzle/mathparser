package ru.introguzzle.mathparser.constant.complex;

import ru.introguzzle.mathparser.common.Reflector;

import java.util.HashMap;
import java.util.Map;

public class ComplexConstantReflector {
    private static final Map<String, ComplexConstant> CONSTANTS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.constant.complex";
    private static final Reflector<ComplexConstant> REFLECTOR = new Reflector<>(ComplexConstant.class);

    private static void load() {
        CONSTANTS.putAll(REFLECTOR.getInstances(PACKAGE));
    }

    public static Map<String, ComplexConstant> get() {
        if (CONSTANTS.isEmpty()) {
            load();
        }

        return CONSTANTS;
    }
}
