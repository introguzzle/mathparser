package ru.introguzzle.mathparser.lambda;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.parse.Parser;
import ru.introguzzle.mathparser.symbol.LambdaArgument;
import ru.introguzzle.mathparser.tokenize.token.Tokens;
import ru.introguzzle.mathparser.tokenize.token.type.LambdaType;

import java.util.List;

public interface LambdaEvaluator<T extends Number> extends Delimitable, Nameable {
    Parser<T> getParser();

    @Override
    default @NotNull LambdaType type() {
        return LambdaType.LAMBDA;
    }

    int getRequiredArguments();
    int getRequiredLambdaArguments();

    boolean isVariadic();
    boolean isLambdaVariadic();

    @NotNull T evaluate(List<T> arguments,
                        List<LambdaArgument<T>> lambdaArguments,
                        Tokens lambda,
                        Context<T> context)
            throws SyntaxException;

    @NotNull default T apply(List<T> arguments,
                             List<LambdaArgument<T>> lambdaArguments,
                             Tokens lambda,
                             Context<T> context)
            throws SyntaxException {
        int size = arguments.size();
        if (size < getRequiredArguments()) {
            throw getInvocationException(arguments.size());
        }

        if (!isVariadic() && size != getRequiredArguments()) {
            throw getInvocationException(arguments.size());
        }

        int lambdaSize = lambdaArguments.size();
        if (lambdaSize < getRequiredLambdaArguments()) {
            throw getInvocationException(arguments.size());
        }

        if (!isVariadic() && size != getRequiredLambdaArguments()) {
            throw getInvocationException(arguments.size());
        }

        return evaluate(arguments, lambdaArguments, lambda, context);
    }

    private @NotNull RuntimeException getInvocationException(int given) {
        return new LambdaInvocationException(given, this);
    }
}
