package ru.constant;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import ru.main.Symbol;

import java.lang.reflect.Modifier;
import java.util.*;

public class Constants {
    private static List<? extends Symbol> constants;
    @NotNull
    private static List<? extends Symbol> load() {
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

    public static Optional<? extends Symbol> find(String string) {
        return Constants
                .get()
                .stream()
                .filter(symbol -> symbol.getRepresentation().equals(string))
                .findFirst();
    }

    public static List<? extends Symbol> get() {
        if (Constants.constants == null) {
            Constants.constants = Constants.load();
        }

        return Constants.constants;
    }
}
