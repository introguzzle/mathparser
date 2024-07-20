package ru.introguzzle.mathparser.function;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class FunctionReflector {

    private static final Map<String, Function> functionMap = new HashMap<>();
    @NotNull
    private static Map<String, Function> load() {
        Map<String, Function> functionMap = new HashMap<>();

        Reflections reflections = new Reflections("ru.introguzzle.mathparser.function");
        Set<Class<? extends Function>> classes = reflections.getSubTypesOf(Function.class);

        for (Class<? extends Function> clazz : classes) {
            try {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                    continue;
                }

                Function functionInstance = clazz.getDeclaredConstructor().newInstance();
                functionMap.put(functionInstance.getName(), functionInstance);
            } catch (Exception e) {
                System.err.println("An error occurred while scanning constants: " + e.getMessage());
                System.err.println("Try adding functions explicitly via withFunctions or addFunction, because list of predefined constants is empty.");
            }
        }

        return functionMap;
    }

    public static Map<String, Function> get() {
        if (FunctionReflector.functionMap.isEmpty()) {
            FunctionReflector.functionMap.putAll(FunctionReflector.load());
        }

        return FunctionReflector.functionMap;
    }
}
