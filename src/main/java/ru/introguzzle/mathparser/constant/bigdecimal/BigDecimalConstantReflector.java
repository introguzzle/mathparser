package ru.introguzzle.mathparser.constant.bigdecimal;

import ru.introguzzle.mathparser.common.reflect.Reflector;

import java.util.HashMap;
import java.util.Map;

public class BigDecimalConstantReflector {
    private static final Map<String, BigDecimalConstant> CONSTANTS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.constant.bigdecimal";
    private static final Reflector<BigDecimalConstant> REFLECTOR = new Reflector<>(BigDecimalConstant.class);

    private static void load() {
        CONSTANTS.putAll(REFLECTOR.getInstances(PACKAGE));
    }

    public static Map<String, BigDecimalConstant> get() {
        if (CONSTANTS.isEmpty()) {
            load();
        }

        return CONSTANTS;
    }
}
