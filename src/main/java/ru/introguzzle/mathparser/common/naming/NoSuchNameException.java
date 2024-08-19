package ru.introguzzle.mathparser.common.naming;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Collection;

public class NoSuchNameException extends NamingException {
    @Serial
    private static final long serialVersionUID = 337677581496777781L;

    public NoSuchNameException(@NotNull CharSequence name,
                               @NotNull Collection<? extends CharSequence> names) {
        super(String.format("No such element with name: %s. Current names: %s", name, names));
    }
}
