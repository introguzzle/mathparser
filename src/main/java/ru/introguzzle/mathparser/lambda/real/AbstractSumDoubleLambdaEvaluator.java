package ru.introguzzle.mathparser.lambda.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.lambda.InfiniteLoopException;
import ru.introguzzle.mathparser.parse.AbstractParser;
import ru.introguzzle.mathparser.symbol.LambdaArgument;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.List;

public abstract class AbstractSumDoubleLambdaEvaluator extends DoubleLambdaEvaluator {
    private final AbstractParser<Double> parser;
    public static final long MAX_ITERATIONS = Integer.MAX_VALUE;

    public AbstractSumDoubleLambdaEvaluator(AbstractParser<Double> parser, int requiredArguments) {
        super(requiredArguments, 1, false, false);
        this.parser = parser;
    }

    public @NotNull Double reduce(Double left, Double right) {
        return left + right;
    }

    @Override
    public AbstractParser<Double> getParser() {
        return parser;
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

        Double result = currentValue;

        int count = 0;
        while (!getParser().compare(currentValue, to)) {
            argument.setValue(currentValue);
            Double evaluatedValue = getParser().parse(lambda, context);
            result = reduce(result, evaluatedValue);
            lambda.reset();
            count++;
            if (count >= MAX_ITERATIONS) {
                throw new InfiniteLoopException("Too many iterations");
            }

            currentValue = reduce(currentValue, delta);
        }

        context.removeSymbol(argument);
        return result;
    }
}
