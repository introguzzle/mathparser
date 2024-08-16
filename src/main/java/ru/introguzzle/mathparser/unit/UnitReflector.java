package ru.introguzzle.mathparser.unit;

import org.reflections.Reflections;
import ru.introguzzle.mathparser.common.reflect.ReflectionUtilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UnitReflector {
    private static final Map<String, Unit<?, ?>> UNITS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.unit";

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void load() {
        Reflections reflections = new Reflections(PACKAGE);
        Set<Class<? extends Unit>> rawClasses = reflections.getSubTypesOf(Unit.class);
        Set<Class<? extends Unit<?, ?>>> classes = new HashSet<>();
        for (Class<? extends Unit> raw : rawClasses) {
            @SuppressWarnings("unchecked")
            Class<? extends Unit<?, ?>> cast = (Class<? extends Unit<?, ?>>) raw;
            classes.add(cast);
        }

        for (Class<? extends Unit<?, ?>> c : classes) {
            try {
                if (!ReflectionUtilities.isInstantiable(c)) {
                    continue;
                }

                Unit<?, ?> instance = c.getDeclaredConstructor().newInstance();
                UNITS.put(instance.getName(), instance);
            } catch (Exception e) {
                System.err.println("An error occurred while scanning classes: " + e.getMessage());
                System.err.println("Try adding names explicitly, because list of predefined names is empty.");
            }
        }
    }

    public static Map<String, Unit<?, ?>> get() {
        if (UNITS.isEmpty()) {
            load();
        }

        return UNITS;
    }
}
