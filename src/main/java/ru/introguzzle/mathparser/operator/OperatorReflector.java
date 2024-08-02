package ru.introguzzle.mathparser.operator;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Reflector;

import java.util.HashMap;
import java.util.Map;

public class OperatorReflector {
    private static final Map<String, Operator> OPERATORS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.operator";
    private static final Reflector<Operator> REFLECTOR = new Reflector<>();

    @NotNull
    private static Map<String, Operator> load() {
        return REFLECTOR.getInstances(Operator.class, OPERATORS, PACKAGE);
    }

    public static Map<String, Operator> get() {
        if (OPERATORS.isEmpty()) {
            OPERATORS.putAll(load());
        }

        return OPERATORS;
    }
}
