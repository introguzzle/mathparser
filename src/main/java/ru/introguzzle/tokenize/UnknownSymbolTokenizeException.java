package ru.introguzzle.tokenize;

public class UnknownSymbolTokenizeException extends TokenizeException {

    public UnknownSymbolTokenizeException(CharSequence sequence) {
        super(UnknownSymbolTokenizeException.createMessage(sequence));
    }

    private static String createMessage(CharSequence sequence) {
        return "Unknown variable, coefficient or function: " + sequence + ". Forgot to register in Context?";
    }
}
