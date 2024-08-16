package ru.introguzzle.mathparser.common.naming;

import org.jetbrains.annotations.NotNull;

public class AmbiguousNameException extends NamingException {
    public AmbiguousNameException(@NotNull CharSequence name) {
        super("Ambiguous argument with name: " + name + " encountered in the expression.");
    }
}
