package ru.introguzzle.mathparser.common.reflect;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;

public final class ReflectionUtilities {
    public static boolean isInstantiable(@NotNull Class<?> cls) {
        return !cls.isInterface() && !cls.isEnum() && !cls.isAnnotation() && !Modifier.isAbstract(cls.getModifiers());
    }
}
