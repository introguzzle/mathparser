package ru.introguzzle.mathparser.lambda;

import java.io.Serial;

public class FunctionAdapterEvaluatorException extends LambdaInvocationException {
    @Serial
    private static final long serialVersionUID = 5094675644912537239L;

    public FunctionAdapterEvaluatorException(String message) {
        super(message);
    }
}
