package ru.introguzzle.mathparser.generate;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.tokenize.Tokenizer;
import ru.introguzzle.mathparser.tokenize.token.SimpleToken;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.type.FunctionType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

import java.util.function.Predicate;

public class FunctionSwapper extends Swapper {
    private final Tokenizer tokenizer;

    public FunctionSwapper(GeneratorOptions options, Tokenizer tokenizer) {
        super(options);
        this.tokenizer = tokenizer;
    }

    @Override
    public @NotNull Type getType() {
        return FunctionType.FUNCTION;
    }

    @Override
    public @NotNull Token apply(Token token, int offset) {
        Function<?> oldFunction = tokenizer.getOptions().findFunction(token.getData()).orElseThrow();
        Predicate<Function<?>> p = function ->
                function.getRequiredArguments() == oldFunction.getRequiredArguments();

        Function<?> newFunction = Random.fromMap(tokenizer.getOptions().getFunctions(), p);

        if (newFunction == null) {
            throw new EmptyFunctionListException("No function found");
        }

        return new SimpleToken(token.getType(), newFunction.getName(), token.getOffset() + offset);
    }
}
