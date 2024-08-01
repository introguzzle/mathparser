package ru.introguzzle.mathparser.constant;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;

import java.lang.reflect.Modifier;
import java.util.*;

public class ConstantReflector {
    private static final Map<String, ImmutableSymbol> CONSTANTS = new HashMap<>();
    @NotNull
    private static Map<String, ImmutableSymbol> load() {
        Map<String, ImmutableSymbol> constants = new HashMap<>();

        Reflections reflections = new Reflections("ru.introguzzle.mathparser.constant");
        Set<Class<? extends ImmutableSymbol>> classes = reflections.getSubTypesOf(ImmutableSymbol.class);

        for (Class<? extends ImmutableSymbol> symbolClass : classes) {
            try {
                if (symbolClass.isInterface() || Modifier.isAbstract(symbolClass.getModifiers())) {
                    continue;
                }

                ImmutableSymbol symbol = symbolClass.getDeclaredConstructor().newInstance();
                constants.put(symbol.getName(), symbol);
            } catch (Exception e) {
                System.err.println("An error occurred while scanning constants: " + e.getMessage());
                System.err.println("Try adding constants explicitly via withConstants or addConstant, because list of predefined constants is empty.");
            }
        }

        return constants;
    }

    public static Map<String, ImmutableSymbol> get() {
        if (ConstantReflector.CONSTANTS.isEmpty()) {
            ConstantReflector.CONSTANTS.putAll(ConstantReflector.load());
        }

        return ConstantReflector.CONSTANTS;
    }
}
