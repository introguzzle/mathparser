package ru.introguzzle.mathparser.lambda.complex;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.complex.Complex;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.parse.AbstractParser;

public abstract class ComplexLambdaEvaluator implements LambdaEvaluator<Complex> {
    private final AbstractParser<Complex> parser;

    private final int requiredArguments;
    private final int requiredLambdaArguments;

    public ComplexLambdaEvaluator(AbstractParser<Complex> parser,
                                  int requiredArguments,
                                  int requiredLambdaArguments) {
        this.parser = parser;
        this.requiredArguments = requiredArguments;
        this.requiredLambdaArguments = requiredLambdaArguments;
    }

    @Override
    public int getRequiredArguments() {
        return requiredArguments;
    }

    @Override
    public int getRequiredLambdaArguments() {
        return requiredLambdaArguments;
    }

    @Override
    public @NotNull AbstractParser<Complex> getParser() {
        return parser;
    }
}
