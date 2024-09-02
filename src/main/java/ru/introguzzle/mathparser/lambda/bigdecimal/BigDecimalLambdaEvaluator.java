package ru.introguzzle.mathparser.lambda.bigdecimal;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.parse.Parser;

import java.math.BigDecimal;

public abstract class BigDecimalLambdaEvaluator implements LambdaEvaluator<BigDecimal> {
    private final Parser<BigDecimal> parser;

    private final int requiredArguments;
    private final int requiredLambdaArguments;

    public BigDecimalLambdaEvaluator(Parser<BigDecimal> parser,
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
    public @NotNull Parser<BigDecimal> getParser() {
        return parser;
    }
}
