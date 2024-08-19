package ru.introguzzle.mathparser.lambda.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.parse.AbstractParser;

public abstract class DoubleLambdaEvaluator implements LambdaEvaluator<Double> {
    private final AbstractParser<Double> parser;

    private final int requiredArguments;
    private final int requiredLambdaArguments;

    public DoubleLambdaEvaluator(AbstractParser<Double> parser,
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
    public @NotNull AbstractParser<Double> getParser() {
        return parser;
    }
}
