package ru.introguzzle.mathparser.common.naming;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class AmbiguousNameException extends NamingException {
    @Serial
    private static final long serialVersionUID = 5952043197609944628L;

    public AmbiguousNameException(@NotNull CharSequence name) {
        super("Ambiguous argument with name: " + name + " encountered in the expression.");
    }
}
