package ru.introguzzle.mathparser.lambda.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.parse.Parser;
import ru.introguzzle.mathparser.symbol.LambdaArgument;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.List;

public class NewtonEvaluator extends DoubleLambdaEvaluator {
    private static final int MAX_ITERATIONS = 1000;
    private static final double TOLERANCE = 1e-7;

    public NewtonEvaluator(Parser<Double> parser) {
        super(parser, 2, 1);
    }

    @Override
    public boolean isVariadic() {
        return false;
    }

    @Override
    public boolean isLambdaVariadic() {
        return false;
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments, List<LambdaArgument<Double>> lambdaArguments, Tokens lambda, Context<Double> context) throws SyntaxException {
        double x0 = arguments.getFirst();

        LambdaArgument<Double> argument = lambdaArguments.getFirst();

        double x = x0;
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            argument.setValue(x);

            double fValue = getParser().parse(lambda, context);  // f(x)
            lambda.reset();

            double dfValue;
            argument.setValue(x + TOLERANCE);
            dfValue = (getParser().parse(lambda, context) - fValue) / TOLERANCE;  // f'(x) ≈ (f(x + h) - f(x)) / h
            lambda.reset();

            if (Math.abs(dfValue) < TOLERANCE) {
                throw new ArithmeticException("Derivative too small; Newton's method may not converge.");
            }

            double x1 = x - fValue / dfValue;

            if (Math.abs(x1 - x) < TOLERANCE) {
                return x1;
            }

            x = x1;
        }

        throw new ArithmeticException("Newton's method did not converge after maximum number of iterations.");
    }

    @Override
    public @NotNull String getName() {
        return "solve";
    }
}
