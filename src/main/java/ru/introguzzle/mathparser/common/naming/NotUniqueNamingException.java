package ru.introguzzle.mathparser.common.naming;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Collection;

public class NotUniqueNamingException extends NamingException {
    @Serial
    private static final long serialVersionUID = 1955776752114617147L;

    public NotUniqueNamingException(@NotNull CharSequence name,
                                    @NotNull Collection<? extends CharSequence> names) {
        super(String.format("Name: %s is already in use. ", name) + "Current names: " + names);
    }
}
