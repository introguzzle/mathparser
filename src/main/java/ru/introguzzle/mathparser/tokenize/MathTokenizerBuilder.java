package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.unit.Unit;

import java.util.HashMap;
import java.util.Map;

public final class MathTokenizerBuilder {
    private final Map<String, Function<?>> functions = new HashMap<>();
    private final Map<String, ImmutableSymbol<?>> constants = new HashMap<>();
    private final Map<String, Operator<?>> operators = new HashMap<>();
    private final Map<String, LambdaEvaluator<?>> lambdaEvaluators = new HashMap<>();
    private final Map<String, Unit<?, ?>> units = new HashMap<>();

    public MathTokenizerBuilder withFunctions(Map<String, ? extends Function<?>> functions) {
        this.functions.putAll(functions);
        return this;
    }

    public MathTokenizerBuilder withConstants(Map<String, ? extends ImmutableSymbol<?>> constants) {
        this.constants.putAll(constants);
        return this;
    }

    public MathTokenizerBuilder withOperators(Map<String, ? extends Operator<?>> operators) {
        this.operators.putAll(operators);
        return this;
    }

    public MathTokenizerBuilder withLambdaEvaluators(Map<String, ? extends LambdaEvaluator<?>> lambdaEvaluators) {
        this.lambdaEvaluators.putAll(lambdaEvaluators);
        return this;
    }

    public MathTokenizerBuilder withUnits(Map<String, ? extends Unit<?, ?>> units) {
        this.units.putAll(units);
        return this;
    }

    public MathTokenizer build() {
        return new MathTokenizer(functions, constants, operators, lambdaEvaluators, units);
    }
}
