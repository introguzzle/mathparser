package ru.introguzzle.mathparser.common.naming;


import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public abstract class NamingException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 413478586100507812L;

    public NamingException() {

    }

    public NamingException(@NotNull CharSequence message) {
        super(message.toString());
    }
}
