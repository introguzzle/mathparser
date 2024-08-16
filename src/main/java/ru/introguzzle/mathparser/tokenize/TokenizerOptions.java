package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.common.math.Radix;
import ru.introguzzle.mathparser.common.naming.NoSuchNameException;
import ru.introguzzle.mathparser.common.options.Options;
import ru.introguzzle.mathparser.constant.real.DoubleConstant;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.function.real.DoubleFunction;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.predicates.DigitPredicate;
import ru.introguzzle.mathparser.tokenize.predicates.LetterPredicate;
import ru.introguzzle.mathparser.tokenize.predicates.OperatorPredicate;
import ru.introguzzle.mathparser.tokenize.validation.CommaValidator;
import ru.introguzzle.mathparser.tokenize.validation.NumberValidator;
import ru.introguzzle.mathparser.tokenize.validation.UnitValidator;
import ru.introguzzle.mathparser.tokenize.validation.Validator;
import ru.introguzzle.mathparser.unit.Unit;
import ru.introguzzle.mathparser.unit.UnitConverter;

import java.util.*;

public class TokenizerOptions implements Options {
    private int flags;

    /**
     * If false, tokenizer will not check strict match of mutable symbols used in context
     * and actual mutable symbols in expression
     */
    private boolean strictMode = true;

    /**
     * TODO
     */
    private Radix radix = new Radix(10);
    private List<Validator> validators = new ArrayList<>();
    private UnitConverter unitConverter = new UnitConverter();

    @NotNull
    private final transient Map<String, Nameable> names = new HashMap<>();

    private final transient Map<String, Operator<?>> operators = new HashMap<>();
    private final transient Map<String, Function<?>> functions = new HashMap<>();
    private final transient Map<String, ImmutableSymbol<?>> constants = new HashMap<>();
    private final transient Map<String, LambdaEvaluator<?>> lambdaEvaluators = new HashMap<>();
    private final transient Map<String, Unit<?, ?>> units = new HashMap<>();

    private final OperatorPredicate allowedOperatorSymbolsPredicate = new OperatorPredicate();
    private final DigitPredicate digitPredicate = new DigitPredicate();
    private final LetterPredicate letterPredicate = new LetterPredicate();

    public TokenizerOptions() {
        this(0);
    }

    public TokenizerOptions(int flags) {
        this.flags = flags;
        this.validators.add(new NumberValidator());
        this.validators.add(new CommaValidator());
        this.validators.add(new UnitValidator());
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }

    public boolean isStrictMode() {
        return strictMode;
    }

    public @NotNull Map<String, Nameable> getNames() {
        return names;
    }

    public Map<String, Operator<?>> getOperators() {
        if (!operators.isEmpty()) {
            return operators;
        }

        names.forEach((k, v) -> {
            if (v instanceof Operator<?>) {
                operators.put(k, (Operator<?>) v);
            }
        });

        return operators;
    }

    public Map<String, Function<?>> getFunctions() {
        if (!functions.isEmpty()) {
            return functions;
        }

        names.forEach((k, v) -> {
            if (v instanceof Function<?>) {
                functions.put(k, (Function<?>) v);
            }
        });

        return functions;
    }

    public Map<String, ImmutableSymbol<?>> getConstants() {
        if (!constants.isEmpty()) {
            return constants;
        }

        names.forEach((k, v) -> {
            if (v instanceof ImmutableSymbol<?>) {
                constants.put(k, (ImmutableSymbol<?>) v);
            }
        });

        return constants;
    }


    public Map<String, LambdaEvaluator<?>> getLambdaEvaluators() {
        if (!lambdaEvaluators.isEmpty()) {
            return lambdaEvaluators;
        }

        names.forEach((k, v) -> {
            if (v instanceof LambdaEvaluator<?>) {
                lambdaEvaluators.put(k, (LambdaEvaluator<?>) v);
            }
        });

        return lambdaEvaluators;
    }

    public Map<String, Unit<?, ?>> getUnits() {
        if (!units.isEmpty()) {
            return units;
        }

        names.forEach((k, v) -> {
            if (v instanceof Unit<?, ?>) {
                units.put(k, (Unit<?, ?>) v);
            }
        });

        return units;
    }

    public DigitPredicate getDigitPredicate() {
        return digitPredicate;
    }

    public LetterPredicate getLetterPredicate() {
        return letterPredicate;
    }

    public OperatorPredicate getAllowedOperatorSymbolsPredicate() {
        return allowedOperatorSymbolsPredicate;
    }

    public TokenizerOptions addFunction(@NotNull Function<?> function) {
        addName(function);
        return this;
    }

    public TokenizerOptions addConstant(@NotNull ImmutableSymbol<?> constant) {
        addName(constant);
        return this;
    }

    public TokenizerOptions addName(@NotNull Nameable nameable) {
        names.put(nameable.getName(), nameable);
        return this;
    }

    public TokenizerOptions addOperator(@NotNull Operator<?> operator) {
        addName(operator);
        return this;
    }

    public TokenizerOptions addLambdaEvaluator(@NotNull LambdaEvaluator<?> lambdaEvaluator) {
        addName(lambdaEvaluator);
        return this;
    }

    public TokenizerOptions addUnit(Unit<?, ?> unit) {
        addName(unit);
        return this;
    }

    private <N extends Nameable> TokenizerOptions clearNameables(Class<N> cls) {
        names.entrySet()
                .removeIf(cls::isInstance);
        return this;
    }

    public TokenizerOptions clearFunctions() {
        return clearNameables(Function.class);
    }

    public TokenizerOptions clearConstants() {
        return clearNameables(DoubleConstant.class);
    }

    public TokenizerOptions clearOperators() {
        return clearNameables(Operator.class);
    }

    public TokenizerOptions clearLambdaEvaluators() {
        return clearNameables(LambdaEvaluator.class);
    }

    public TokenizerOptions clearUnits() {
        return clearNameables(Unit.class);
    }

    public @NotNull Optional<ImmutableSymbol<?>> findConstant(String name) {
        return Optional.ofNullable(getConstants().get(name));
    }

    public @NotNull Optional<Function<?>> findFunction(String name) {
        return Optional.ofNullable(getFunctions().get(name));
    }

    public @NotNull Optional<Operator<?>> findOperator(String name) {
        return Optional.ofNullable(getOperators().get(name));
    }

    public @NotNull Optional<LambdaEvaluator<?>> findLambdaEvaluator(String name) {
        return Optional.ofNullable(getLambdaEvaluators().get(name));
    }

    public @NotNull Optional<Unit<?, ?>> findUnit(String name) {
        return Optional.ofNullable(getUnits().get(name));
    }

    public TokenizerOptions withFunctions(@NotNull Collection<? extends Function<?>> functions) {
        names.putAll(Nameable.toMap(functions));
        return this;
    }

    public TokenizerOptions withConstants(@NotNull Collection<? extends ImmutableSymbol<?>> constants) {
        names.putAll(Nameable.toMap(constants));
        return this;
    }

    public TokenizerOptions withOperators(@NotNull Collection<? extends Operator<?>> operators) {
        names.putAll(Nameable.toMap(operators));
        return this;
    }

    public TokenizerOptions overrideOperator(@NotNull String name,
                                             Operator<?> operator) {
        if (!names.containsKey(name)) {
            throw new NoSuchNameException(name, names.keySet());
        }

        names.replace(name, operator);
        return this;
    }

    public TokenizerOptions overrideConstant(@NotNull String name, double value) {
        names.remove(name);
        names.put(name, new DoubleConstant(name, value) {});
        return this;
    }

    public TokenizerOptions overrideFunction(@NotNull String name,
                                   int requiredArguments,
                                   boolean variadic,
                                   @NotNull java.util.function.Function<List<Double>, Double> replace) {
        if (!names.containsKey(name)) {
            throw new NoSuchNameException(name, names.keySet());
        }

        Function<Double> newFunction = new DoubleFunction(name, requiredArguments) {
            @Override
            public @NotNull Double evaluate(List<Double> arguments) {
                return replace.apply(arguments);
            }

            @Override
            public boolean isVariadic() {
                return variadic;
            }
        };

        names.replace(name, newFunction);
        return this;
    }

    public Radix getRadix() {
        return radix;
    }

    public void setRadix(Radix radix) {
        this.radix = radix;
    }

    @Override
    public final int getFlags() {
        return flags;
    }

    public final List<Validator> getValidators() {
        return validators;
    }

    public final void setValidators(Validator... validators) {
        this.validators = new ArrayList<>(Arrays.asList(validators));
    }

    public final TokenizerOptions addValidator(Validator validator) {
        validators.add(validator);
        return this;
    }

    public final TokenizerOptions removeValidators() {
        validators.clear();
        return this;
    }

    public UnitConverter getUnitConverter() {
        return unitConverter;
    }

    public void setUnitConverter(UnitConverter unitConverter) {
        this.unitConverter = unitConverter;
    }
}
