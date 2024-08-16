package ru.introguzzle.mathparser.lambda.real;

import ru.introguzzle.mathparser.lambda.LambdaEvaluator;

public abstract class DoubleLambdaEvaluator implements LambdaEvaluator<Double> {
    private final int requiredArguments;
    private final int requiredLambdaArguments;
    private final boolean lambdaVariadic;
    private final boolean variadic;

    public DoubleLambdaEvaluator(int requiredArguments,
                                 int requiredLambdaArguments,
                                 boolean variadic,
                                 boolean lambdaVariadic) {
        this.requiredArguments = requiredArguments;
        this.requiredLambdaArguments = requiredLambdaArguments;
        this.variadic = variadic;
        this.lambdaVariadic = lambdaVariadic;
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
    public boolean isVariadic() {
        return variadic;
    }

    @Override
    public boolean isLambdaVariadic() {
        return lambdaVariadic;
    }
}
