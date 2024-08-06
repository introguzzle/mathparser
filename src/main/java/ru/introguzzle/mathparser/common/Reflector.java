package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Reflector<N extends Nameable> {

    private final Class<N> cls;

    public Reflector(Class<N> cls) {
        this.cls = cls;
    }

    @NotNull
    public Map<String, ? extends N> getInstances(@NotNull String packageName) {
        Map<String, N> instances = new HashMap<>();
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends N>> classes = reflections.getSubTypesOf(cls);

        for (Class<? extends N> clazz : classes) {
            try {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                    continue;
                }

                N instance = clazz.getDeclaredConstructor().newInstance();
                instances.put(instance.getName(), instance);
            } catch (Exception e) {
                System.err.println("An error occurred while scanning classes: " + e.getMessage());
                System.err.println("Try adding names explicitly, because list of predefined names is empty.");
            }
        }

        return instances;
    }
}
