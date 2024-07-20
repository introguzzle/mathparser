package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;

public class NotUniqueNamingException extends NamingException {
    public NotUniqueNamingException(@NotNull CharSequence name,
                                    @NotNull Collection<? extends CharSequence> names) {
        super(String.format("Name: %s is already in use. ", name) + "Current names: " + names);
    }
}
