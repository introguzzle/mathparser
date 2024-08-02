package ru.introguzzle.mathparser.function;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Reflector;

public class FunctionReflector {

    private static final Map<String, Function> FUNCTIONS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.function";
    private static final Reflector<Function> REFLECTOR = new Reflector<>();

    @NotNull
    private static Map<String, Function> load() {
        return REFLECTOR.getInstances(Function.class, FUNCTIONS, PACKAGE);
    }

    public static Map<String, Function> get() {
        if (FunctionReflector.FUNCTIONS.isEmpty()) {
            FunctionReflector.FUNCTIONS.putAll(FunctionReflector.load());
        }

        return FunctionReflector.FUNCTIONS;
    }
}
