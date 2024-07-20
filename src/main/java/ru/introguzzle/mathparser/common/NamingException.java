package ru.introguzzle.mathparser.common;


import org.jetbrains.annotations.NotNull;

public abstract class NamingException extends RuntimeException {
    public NamingException() {

    }

    public NamingException(@NotNull CharSequence message) {
        super(message.toString());
    }
}
