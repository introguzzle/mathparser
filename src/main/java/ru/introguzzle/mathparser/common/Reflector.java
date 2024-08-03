package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

public class Reflector<N extends Nameable> {

    @NotNull
    public Map<String, N> getInstances(Class<N> cls,
                                       @Mutates Map<String, N> map,
                                       String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends N>> classes = reflections.getSubTypesOf(cls);

        for (Class<? extends N> clazz : classes) {
            try {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                    continue;
                }

                N instance = clazz.getDeclaredConstructor().newInstance();
                map.put(instance.getName(), instance);
            } catch (Exception e) {
                System.err.println("An error occurred while scanning classes: " + e.getMessage());
                System.err.println("Try adding names explicitly, because list of predefined names is empty.");
            }
        }

        return map;
    }
}
