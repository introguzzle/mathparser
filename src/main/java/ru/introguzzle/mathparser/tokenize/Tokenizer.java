package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.common.Optionable;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.group.Group;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.token.Token;

import java.util.Map;
import java.util.Optional;

public interface Tokenizer extends Optionable<TokenizerOptions> {
    @NotNull Group tokenize(@NotNull Expression expression, @NotNull Context<?> context) throws TokenizeException;

    @NotNull Map<String, Nameable> getNames();

    @NotNull Map<String, ImmutableSymbol<?>> getConstants();
    @NotNull Map<String, Function<?>> getFunctions();
    @NotNull Map<String, Operator<?>> getOperators();

    default @NotNull Optional<ImmutableSymbol<?>> findConstant(Token token) {
        return findConstant(token.getData());
    }

    default @NotNull Optional<Function<?>> findFunction(Token token) {
        return findFunction(token.getData());
    }

    default @NotNull Optional<Operator<?>> findOperator(Token token) {
        return findOperator(token.getData());
    }

    default @NotNull Optional<ImmutableSymbol<?>> findConstant(String name) {
        return Optional.ofNullable(getConstants().get(name));
    }

    default @NotNull Optional<Function<?>> findFunction(String name) {
        return Optional.ofNullable(getFunctions().get(name));
    }

    default @NotNull Optional<Operator<?>> findOperator(String name) {
        return Optional.ofNullable(getOperators().get(name));
    }
}
