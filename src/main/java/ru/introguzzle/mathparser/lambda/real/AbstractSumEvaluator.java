package ru.introguzzle.mathparser.lambda.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.lambda.InfiniteLoopException;
import ru.introguzzle.mathparser.parse.Parser;
import ru.introguzzle.mathparser.symbol.LambdaArgument;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.List;

public abstract class AbstractSumEvaluator extends DoubleLambdaEvaluator {
    public static final long MAX_ITERATIONS = Integer.MAX_VALUE;

    public AbstractSumEvaluator(Parser<Double> parser, int requiredArguments) {
        super(parser, requiredArguments, 1);
    }

    public abstract Double getDelta(List<Double> arguments);

    @Override
    public @NotNull Double evaluate(List<Double> arguments,
                                    List<LambdaArgument<Double>> lambdaArguments,
                                    Tokens lambda,
                                    Context<Double> context)
            throws SyntaxException {
        LambdaArgument<Double> argument = lambdaArguments.getFirst();

        Double currentValue = arguments.get(0);
        Double to = arguments.get(1);
        Double delta = getDelta(arguments);

        if (Double.compare(0.0, delta) == 0) {
            throw new InfiniteLoopException("Delta cannot be 0");
        }

        Double result = 0.0;

        int count = 0;
        while (currentValue <= to) {
            argument.setValue(currentValue);
            Double evaluatedValue = getParser().parse(lambda, context);
            result += evaluatedValue;
            lambda.reset();
            count++;
            if (count >= MAX_ITERATIONS) {
                throw new InfiniteLoopException("Too many iterations");
            }

            currentValue += delta;
        }

        return result;
    }

    @Override
    public boolean isVariadic() {
        return false;
    }

    @Override
    public boolean isLambdaVariadic() {
        return false;
    }
}
