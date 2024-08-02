package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

public class Reflector<X extends Nameable> {

    @NotNull
    public Map<String, X> getInstances(Class<X> cls,
                                       @Mutates Map<String, X> map,
                                       String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends X>> classes = reflections.getSubTypesOf(cls);

        for (Class<? extends X> clazz : classes) {
            try {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                    continue;
                }

                X instance = clazz.getDeclaredConstructor().newInstance();
                map.put(instance.getName(), instance);
            } catch (Exception e) {
                System.err.println("An error occurred while scanning classes: " + e.getMessage());
                System.err.println("Try adding names explicitly, because list of predefined names is empty.");
            }
        }

        return map;
    }
}
