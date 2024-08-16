package ru.introguzzle.mathparser.common.naming;


import org.jetbrains.annotations.NotNull;

public abstract class NamingException extends RuntimeException {
    public NamingException() {

    }

    public NamingException(@NotNull CharSequence message) {
        super(message.toString());
    }
}
