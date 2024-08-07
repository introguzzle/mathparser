package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.common.NoSuchNameException;
import ru.introguzzle.mathparser.common.Options;
import ru.introguzzle.mathparser.common.math.Radix;
import ru.introguzzle.mathparser.constant.real.DoubleConstant;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.function.real.DoubleFunction;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.token.Token;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TokenizerOptions implements Options {
    public static final char DECIMAL = '.';
    public static final char IMAGINARY_UNIT = 'i';
    public static final char UNDERSCORE = '_';

    private int flags;

    /**
     * If false, tokenizer will not check strict match of mutable symbols used in context
     * and actual mutable symbols in expression
     */
    public boolean strictMode = false;
    public Radix radix = new Radix(10);


    @NotNull
    private final transient Map<String, Nameable> names = new HashMap<>();

    private final transient Map<String, Operator<?>> operators = new HashMap<>();
    private final transient Map<String, Function<?>> functions = new HashMap<>();
    private final transient Map<String, ImmutableSymbol<?>> constants = new HashMap<>();

    private String allowedOperatorSymbols = "+-/*~!@#$%^&*()\"{}_[]|\\?/<>,.=";

    private final Predicate<Character> allowedOperatorSymbolsPredicate = c ->
            c != null && allowedOperatorSymbols.indexOf(c) != -1;

    private Predicate<Character> digitPredicate = c ->
            c != null && (c == DECIMAL || c == IMAGINARY_UNIT || Character.isDigit(c));

    private Predicate<Character> letterPredicate = c ->
            c != null && (c == UNDERSCORE || Character.isLetter(c));

    public TokenizerOptions() {

    }

    public TokenizerOptions(int flags) {
        this.flags = flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }

    public TokenizerOptions setAllowedOperatorSymbols(String allowedOperatorSymbols) {
        this.allowedOperatorSymbols = allowedOperatorSymbols;
        return this;
    }

    public TokenizerOptions setLetterPredicate(Predicate<Character> letterPredicate) {
        this.letterPredicate = letterPredicate;
        return this;
    }

    public TokenizerOptions setDigitPredicate(Predicate<Character> digitPredicate) {
        this.digitPredicate = digitPredicate;
        return this;
    }

    public boolean isStrictMode() {
        return strictMode;
    }

    public Map<String, Nameable> getNames() {
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

    public String getAllowedOperatorSymbols() {
        return allowedOperatorSymbols;
    }

    public Predicate<Character> getDigitPredicate() {
        return digitPredicate;
    }

    public Predicate<Character> getLetterPredicate() {
        return letterPredicate;
    }

    public Predicate<Character> getAllowedOperatorSymbolsPredicate() {
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

    public @NotNull Optional<ImmutableSymbol<?>> findConstant(Token token) {
        return findConstant(token.getData());
    }

    public @NotNull Optional<Function<?>> findFunction(Token token) {
        return findFunction(token.getData());
    }

    public @NotNull Optional<Operator<?>> findOperator(Token token) {
        return findOperator(token.getData());
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

    private static Map<String, Nameable> toMap(@NotNull Collection<? extends Nameable> collection) {
        return collection.stream().collect(Collectors.toMap(Nameable::getName, n -> n));
    }

    public TokenizerOptions withFunctions(@NotNull Collection<? extends Function<?>> functions) {
        names.putAll(toMap(functions));
        return this;
    }

    public TokenizerOptions withConstants(@NotNull Collection<? extends ImmutableSymbol<?>> constants) {
        names.putAll(toMap(constants));
        return this;
    }

    public TokenizerOptions withOperators(@NotNull Collection<? extends Operator<?>> operators) {
        names.putAll(toMap(operators));
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
            public @NotNull Double apply(List<Double> arguments) {
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
    public int getFlags() {
        return flags;
    }
}
