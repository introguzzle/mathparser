package ru.function;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class FunctionReflector {
    private static Map<String, Function> functionMap;
    @NotNull
    private static Map<String, Function> load() {
        Map<String, Function> functionMap = new HashMap<>();

        Reflections reflections = new Reflections("ru.function");
        Set<Class<? extends Function>> classes = reflections.getSubTypesOf(Function.class);

        for (Class<? extends Function> clazz : classes) {
            try {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                    continue;
                }

                Function functionInstance = clazz.getDeclaredConstructor().newInstance();
                functionMap.put(functionInstance.getName(), functionInstance);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        return functionMap;
    }

    public static Map<String, Function> get() {
        if (FunctionReflector.functionMap == null) {
            FunctionReflector.functionMap = FunctionReflector.load();
        }

        return FunctionReflector.functionMap;
    }
}