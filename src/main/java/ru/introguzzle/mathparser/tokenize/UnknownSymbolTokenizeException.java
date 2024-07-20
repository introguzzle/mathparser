package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.expression.Expression;

public class UnknownSymbolTokenizeException extends TokenizeException {

    public UnknownSymbolTokenizeException(@NotNull CharSequence symbols,
                                          @NotNull Expression expression,
                                          int offset) {
        super(UnknownSymbolTokenizeException.createMessage(symbols), expression, offset);
    }

    @NotNull
    private static String createMessage(CharSequence symbols) {
        return "Unknown variable, coefficient or function: " + symbols + ". Forgot to register in Context?";
    }
}
