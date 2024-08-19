package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.expression.Expression;

import java.io.Serial;

public class UnknownSymbolTokenizeException extends TokenizeException {
    @Serial
    private static final long serialVersionUID = 9142971083383361303L;

    public UnknownSymbolTokenizeException(@NotNull CharSequence symbols,
                                          @NotNull Expression expression,
                                          int offset) {
        super(UnknownSymbolTokenizeException.createMessage(symbols), expression, offset);
    }

    @NotNull
    private static String createMessage(CharSequence symbols) {
        return "Unknown argument, coefficient or function: " + symbols + ". Forgot to register in Context?";
    }
}
