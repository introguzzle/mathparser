package ru.introguzzle.mathparser.lambda.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.lambda.FunctionAdapterEvaluatorException;
import ru.introguzzle.mathparser.parse.AbstractParser;
import ru.introguzzle.mathparser.symbol.LambdaArgument;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.List;

public class FunctionAdapterEvaluator extends DoubleLambdaEvaluator {
    public FunctionAdapterEvaluator(AbstractParser<Double> parser) {
        super(parser, 0, 0);
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
    public @NotNull Double evaluate(List<Double> arguments,
                                    List<LambdaArgument<Double>> lambdaArguments,
                                    Tokens lambda,
                                    Context<Double> context)
            throws SyntaxException {
        if (arguments.size() != lambdaArguments.size()) {
            throw new FunctionAdapterEvaluatorException("Wrong number of arguments");
        }

        for (int i = 0; i < arguments.size(); i++) {
            lambdaArguments.get(i).setValue(arguments.get(i));
        }

        return getParser().parse(lambda, context);
    }

    @Override
    public @NotNull String getName() {
        return "eval";
    }
}
