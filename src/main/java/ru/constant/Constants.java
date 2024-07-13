package ru.constant;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import ru.contract.Symbol;

import java.lang.reflect.Modifier;
import java.util.*;

public class Constants {
    private static List<Symbol> constants;
    @NotNull
    private static List<Symbol> load() {
        List<Symbol> list = new ArrayList<>();

        Reflections reflections = new Reflections("ru.constant");
        Set<Class<? extends Symbol>> classes = reflections.getSubTypesOf(Symbol.class);

        for (Class<? extends Symbol> symbolClass : classes) {
            try {
                if (symbolClass.isInterface() || Modifier.isAbstract(symbolClass.getModifiers())) {
                    continue;
                }

                Symbol symbol = symbolClass.getDeclaredConstructor().newInstance();
                list.add(symbol);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        return list;
    }

    public static List<Symbol> get() {
        if (Constants.constants == null) {
            Constants.constants = Constants.load();
        }

        return Constants.constants;
    }
}
