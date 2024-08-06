package ru.introguzzle.mathparser.constant.real;

import ru.introguzzle.mathparser.common.Reflector;

import java.util.*;

public class DoubleConstantReflector {
    private static final Map<String, DoubleConstant> CONSTANTS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.constant.real";
    private static final Reflector<DoubleConstant> REFLECTOR = new Reflector<>(DoubleConstant.class);

    private static void load() {
        CONSTANTS.putAll(REFLECTOR.getInstances(PACKAGE));
    }

    public static Map<String, DoubleConstant> get() {
        if (CONSTANTS.isEmpty()) {
            load();
        }

        return CONSTANTS;
    }
}
