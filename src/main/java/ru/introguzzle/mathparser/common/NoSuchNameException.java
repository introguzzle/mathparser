package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;

public class NoSuchNameException extends NamingException {
    public NoSuchNameException(@NotNull CharSequence name,
                               @NotNull Collection<? extends CharSequence> names) {
        super(String.format("No such element with name: %s. Current names: %s", name, names));
    }
}
