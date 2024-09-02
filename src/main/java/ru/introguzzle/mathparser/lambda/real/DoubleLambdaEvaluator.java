package ru.introguzzle.mathparser.lambda.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.parse.Parser;

public abstract class DoubleLambdaEvaluator implements LambdaEvaluator<Double> {
    private final Parser<Double> parser;

    private final int requiredArguments;
    private final int requiredLambdaArguments;

    public DoubleLambdaEvaluator(Parser<Double> parser,
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
    public @NotNull Parser<Double> getParser() {
        return parser;
    }
}
