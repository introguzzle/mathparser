package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.common.math.Number;
import ru.introguzzle.mathparser.common.math.Radix;
import ru.introguzzle.mathparser.common.math.RadixNumberFormatException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.constant.real.DoubleConstantReflector;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.ExpressionIterator;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.function.real.DoubleFunctionReflector;
import ru.introguzzle.mathparser.group.Group;
import ru.introguzzle.mathparser.group.TokenGroup;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.operator.DoubleOperatorReflector;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.predicates.DigitPredicate;
import ru.introguzzle.mathparser.tokenize.token.*;
import ru.introguzzle.mathparser.tokenize.token.type.*;
import ru.introguzzle.mathparser.tokenize.validation.ValidationException;
import ru.introguzzle.mathparser.tokenize.validation.Validator;
import ru.introguzzle.mathparser.unit.Unit;
import ru.introguzzle.mathparser.unit.UnitReflector;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Stack;

public class MathTokenizer implements Tokenizer, Serializable {
    private TokenizerOptions options = new TokenizerOptions();

    @Override
    public void setOptions(TokenizerOptions options) {
        this.options = options;
    }

    @Override
    public TokenizerOptions getOptions() {
        return options;
    }

    @Serial
    private static final long serialVersionUID = -5494362190912839L;

    public MathTokenizer() {
        this(DoubleFunctionReflector.get(),
                DoubleConstantReflector.get(),
                DoubleOperatorReflector.get(),
                Map.of(),
                UnitReflector.get()
        );
    }

    public MathTokenizer(Map<String, ? extends Function<?>> functions,
                         Map<String, ? extends ImmutableSymbol<?>> constants,
                         Map<String, ? extends Operator<?>> operators,
                         Map<String, ? extends LambdaEvaluator<?>> lambdaEvaluators,
                         Map<String, ? extends Unit<?, ?>> units) {
        options.getNames().putAll(functions);
        options.getNames().putAll(constants);
        options.getNames().putAll(operators);
        options.getNames().putAll(lambdaEvaluators);
        options.getNames().putAll(units);
    }

    @Override
    public synchronized
    @NotNull Group tokenize(@NotNull Expression expression,
                            @NotNull Context<?> context)
            throws TokenizeException {
        return new TokenGroup(start(expression.iterator(), context, false));
    }

    protected synchronized
    @NotNull Group tokenize(@NotNull Expression expression,
                            @NotNull Context<?> context,
                            boolean suppressUnknownSymbols)
            throws TokenizeException {

        return new TokenGroup(start(expression.iterator(), context, suppressUnknownSymbols));
    }

    protected @NotNull Tokens start(@NotNull ExpressionIterator iterator,
                                    Context<?> context,
                                    boolean suppressUnknownSymbols)
            throws TokenizeException {

        Stack<Character> parenthesisStack = new Stack<>();
        Tokens tokens = new SimpleTokens();
        Expression expression = iterator.getExpression();

        if (expression.getString().isBlank() || expression.getString().isEmpty()) {
            return new SimpleTokens(new SimpleToken(TerminalType.TERMINAL, "", 0));
        }

        while (iterator.hasNext()) {
            char current = iterator.current();
            switch (current) {
                case '\n':
                case ' ':
                    iterator.next();
                    continue;

                case '(':
                    tokens.add(ParenthesisType.LEFT, current, iterator.getCursor());
                    parenthesisStack.push(current);
                    iterator.next();
                    continue;

                case ')':
                    tokens.add(ParenthesisType.RIGHT, current, iterator.getCursor());

                    if (parenthesisStack.isEmpty()) {
                        throw new IllegalBracketStartException(expression, iterator.getCursor());
                    } else {
                        parenthesisStack.pop();
                    }

                    iterator.next();
                    continue;

                case ',':
                    tokens.add(DelimiterType.COMMA, current, iterator.getCursor());
                    iterator.next();
                    continue;

                case ';':
                    tokens.add(DelimiterType.SEMICOLON, current, iterator.getCursor());
                    iterator.next();
                    continue;

                default:
                    if (options.getAllowedOperatorSymbolsPredicate().test(iterator.current())) {
                        tokens.add(handleOperator(iterator));

                        if (!iterator.hasNext()) {
                            break;
                        }

                        continue;
                    }

                    if (options.getLetterPredicate().test(iterator.current())) {
                        tokens.add(handleSymbols(iterator, context, suppressUnknownSymbols));

                        if (!iterator.hasNext()) {
                            break;
                        }

                        continue;
                    }

                    if (options.getDigitPredicate().test(iterator.current())) {
                        tokens.add(handleNumber(iterator));

                        if (!iterator.hasNext()) {
                            break;
                        }

                        continue;
                    }

                    throw new UnknownCharacterException(current, expression, iterator.getCursor());
            }
        }

        if (!parenthesisStack.isEmpty()) {
            throw new BracketMismatchException(expression, iterator.getCursor() - 1);
        }

        if (getOptions().isStrictMode()) {
            try {
                validate(tokens);
            } catch (ValidationException e) {
                TokenizeException te = new TokenizeException(e.getMessage(), expression, e.getOffset()) {};
                te.initCause(e.getCause());
                throw te;
            }
        }

        tokens.add(TerminalType.TERMINAL, "", iterator.getCursor() - 1);
        return tokens;
    }

    protected boolean validate(Tokens tokens) throws ValidationException {
        boolean result = true;

        for (int i = 1; i < tokens.size() - 1; i++) {
            Token current = tokens.get(i);
            Token previous = tokens.get(i - 1);
            Token next = tokens.get(i + 1);

            for (Validator validator : getOptions().getValidators()) {
                result &= validator.validate(previous, current, next);
            }

            if (current instanceof CompositeToken compositeToken) {
                result &= validate(compositeToken.getTokens());
            }
        }

        return result;
    }

    protected @NotNull Token handleNumber(ExpressionIterator iterator) throws InvalidNumberFormatException {
        StringBuilder acc = new StringBuilder();

        char current = iterator.current();
        final int start = iterator.getCursor();

        int imaginaryUnitCount = 0;
        int decimalPointCount = 0;
        int radixSpliteratorCount = 0;

        DigitPredicate predicate = options.getDigitPredicate();

        while (iterator.hasNext() && predicate.test(current)) {
            if (current == predicate.getImaginaryUnit()) {
                imaginaryUnitCount++;

            } else if (current == predicate.getRadixSpliterator()) {
                radixSpliteratorCount++;

            } else if (current == predicate.getDecimalPoint()) {
                decimalPointCount++;
            }

            acc.append(current);
            iterator.next();
            if (!iterator.hasNext()) {
                break;
            }

            current = iterator.current();
        }

        if (imaginaryUnitCount > 1 || decimalPointCount > 1 || radixSpliteratorCount > 1) {
            throw new InvalidNumberFormatException(acc, iterator.getExpression(), iterator.getCursor() - 1);
        }

        Number number = getNumber(iterator, acc.toString());

        if (imaginaryUnitCount > 0) {
            return new NumberToken(NumberType.COMPLEX_NUMBER, number, start);
        }

        return new NumberToken(NumberType.NUMBER, number, start);
    }

    protected @NotNull Number getNumber(ExpressionIterator iterator,
                                        String string) throws InvalidNumberFormatException {
        String[] split = string.split(String.valueOf(options.getDigitPredicate().getRadixSpliterator()));

        Radix radix = Radix.DECIMAL;
        if (split.length == 2) {
            try {
                radix = new Radix(Double.parseDouble(split[1]));
            } catch (RadixNumberFormatException ignored) {
                throw new InvalidNumberFormatException(string, iterator.getExpression(), iterator.getCursor() - 1);
            }
        }

        Number number;
        try {
            number = new Number(split[0], radix);
        } catch (RadixNumberFormatException ignored) {
            throw new InvalidNumberFormatException(string, iterator.getExpression(), iterator.getCursor() - 1);
        }

        return number;
    }

    protected @NotNull Token handleCompositeNameable(ExpressionIterator iterator,
                                                     Nameable specialNameable,
                                                     Context<?> context)
            throws TokenizeException {

        String name = specialNameable.getName();
        StringBuilder builder = new StringBuilder();

        int start = iterator.getCursor() - name.length();
        int parenthesisDepth = 0;

        while (iterator.hasNext()) {
            char current = iterator.current();

            builder.append(current);

            if (current == ParenthesisType.LEFT.getName().charAt(0)) {
                parenthesisDepth++;
            } else if (current == ParenthesisType.RIGHT.getName().charAt(0)) {
                parenthesisDepth--;

                if (parenthesisDepth == 0) {
                    iterator.next();
                    break;
                }
            }

            iterator.next();
        }

        @NotNull Tokens tokens = tokenize(Expression.of(builder.toString()), context, true).getTokens();

        Tokens prev = new SimpleTokens();
        prev.add(new SimpleToken(FunctionType.FUNCTION, name, 0));
        prev.merge(tokens);

        return new SpecialToken(specialNameable.type(), prev, start);
    }

    protected @NotNull Token handleOperator(ExpressionIterator iterator)
            throws UnknownOperatorException {
        StringBuilder operator = new StringBuilder();
        final int start = iterator.getCursor();

        while (iterator.hasNext() && options.getAllowedOperatorSymbolsPredicate().test(iterator.current())) {
            operator.append(iterator.next());
            if (!iterator.hasNext()) {
                break;
            }
        }

        if (DelimiterType.ARROW.nameEquals(operator)) {
            return DelimiterType.ARROW.toToken(start);
        }

        if (SpecialType.ASSIGNMENT.nameEquals(operator)) {
            return SpecialType.ASSIGNMENT.toToken(start);
        }

        SearchResult operatorResult = find(operator, start);

        if (operatorResult.getToken() == null) {
            throw new UnknownOperatorException(operator, iterator.getExpression(), start);
        }

        return operatorResult.getToken();
    }

    /**
     * Processes a sequence of symbols, recognizing it as a known object (e.g., function, constant, operator, etc.)
     * or a variable in the context.
     *
     * @param iterator              The expression iterator that points to the current position in the string.
     * @param context               The context containing known symbols (e.g., variables).
     * @param suppressUnknownSymbols If true, unknown symbols will not raise an exception;
     *                               if false, an exception will be thrown.
     * @return A token representing the recognized symbol.
     * @throws TokenizeException If an error occurs during tokenization (e.g., an unknown symbol is encountered).
     */
    protected @NotNull Token handleSymbols(ExpressionIterator iterator,
                                           Context<?> context,
                                           boolean suppressUnknownSymbols)
            throws TokenizeException {
        StringBuilder symbols = new StringBuilder();
        final int start = iterator.getCursor();

        // Reads a sequence of letter characters (the symbol could be, for example, a function or variable name).
        while (iterator.hasNext() && options.getLetterPredicate().test(iterator.current())) {
            symbols.append(iterator.next());
            if (!iterator.hasNext()) {
                break;
            }
        }

        // Checks if the found symbol is the name of a unit converter.
        if (getOptions().getUnitConverter().nameEquals(symbols)) {
            return getOptions().getUnitConverter().toToken(start);
        }

        // Checks if the found symbol is the name of a lambda expression.
        if (getOptions().getLambdaEvaluators().containsKey(symbols.toString())) {
            LambdaEvaluator<?> lambdaEvaluator = getOptions().getLambdaEvaluators().get(symbols.toString());
            return handleCompositeNameable(iterator, lambdaEvaluator, context);
        }

        // Search among registered objects (functions, operators, constants, etc.) and in the context.
        SearchResult result = SearchResult.reduce(
                find(symbols, start),                    // Search among known symbols.
                findFromContext(context, symbols, start) // Search among variables in the context.
        );

        // If the symbol was not found and suppressing unknown symbols is not allowed, an exception is thrown.
        if (!result.isMatch() && !suppressUnknownSymbols) {
            throw new UnknownSymbolTokenizeException(symbols, iterator.getExpression(), start);
        }

        // If nothing is found, return a token interpreted as a lambda expression argument.
        if (result.getToken() == null) {
            return new SimpleToken(SymbolType.LAMBDA_ARGUMENT, symbols, start);
        }

        return result.getToken();  // Return the found token.
    }

    /**
     *
     * @param symbols Sequence of letters (candidate for nameable)
     * @return Search result
     */

    protected @NotNull SearchResult find(CharSequence symbols, int start) {
        // We try to find by main name first
        // If failed, try to check alternative names
        // If previous step failed, certainly nothing was found

        Nameable nameable = getOptions().getNames().get(symbols.toString());
        if (nameable != null) {
            return new SearchResult(true, nameable.toToken(start));
        }

        // Slow O(n) lookup if nameable has multiple names
        for (Nameable n : getOptions().getNames().values()) {
            if (n instanceof MultiNameable multiNameable && multiNameable.nameEquals(symbols)) {
                return new SearchResult(true, n.toToken(start));
            }
        }

        return new SearchResult(false, null);
    }

    protected @NotNull SearchResult findFromContext(Context<?> context,
                                                    CharSequence symbols,
                                                    int start) {
        SearchResult result = new SearchResult();

        context.getSymbol(symbols.toString())
                .ifPresent(s -> {
                    result.setToken(s.toToken(start));
                    result.setMatch(true);
                });

        return result;
    }
}
