package ru.introguzzle.mathparser.constant;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;

import java.lang.reflect.Modifier;
import java.util.*;

public class ConstantReflector {
    private static List<ImmutableSymbol> constants;
    @NotNull
    private static List<ImmutableSymbol> load() {
        List<ImmutableSymbol> list = new ArrayList<>();

        Reflections reflections = new Reflections("ru.introguzzle.mathparser.constant");
        Set<Class<? extends ImmutableSymbol>> classes = reflections.getSubTypesOf(ImmutableSymbol.class);

        for (Class<? extends ImmutableSymbol> symbolClass : classes) {
            try {
                if (symbolClass.isInterface() || Modifier.isAbstract(symbolClass.getModifiers())) {
                    continue;
                }

                ImmutableSymbol symbol = symbolClass.getDeclaredConstructor().newInstance();
                list.add(symbol);
            } catch (Exception e) {
                System.err.println("An error occurred while scanning constants: " + e.getMessage());
                System.err.println("Try adding constants explicitly via withConstants or addConstant, because list of predefined constants is empty.");
            }
        }

        return list;
    }

    public static List<ImmutableSymbol> get() {
        if (ConstantReflector.constants == null) {
            ConstantReflector.constants = ConstantReflector.load();
        }

        return ConstantReflector.constants;
    }
}
