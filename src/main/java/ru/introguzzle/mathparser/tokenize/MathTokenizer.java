package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.*;
import ru.introguzzle.mathparser.common.math.Number;
import ru.introguzzle.mathparser.common.math.Radix;
import ru.introguzzle.mathparser.expression.ExpressionIterator;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.function.real.DoubleFunctionReflector;
import ru.introguzzle.mathparser.group.Group;
import ru.introguzzle.mathparser.group.TokenGroup;
import ru.introguzzle.mathparser.operator.DoubleOperatorReflector;
import ru.introguzzle.mathparser.constant.real.DoubleConstantReflector;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.token.*;
import ru.introguzzle.mathparser.tokenize.token.type.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.*;

public class MathTokenizer implements Tokenizer, Serializable {
    private TokenizerOptions options = new TokenizerOptions() {};

    @Override
    public void setOptions(TokenizerOptions options) {
        this.options = options;
    }

    @Override
    public TokenizerOptions getOptions() {
        return options;
    }

    public record Buffer(@NotNull ExpressionIterator iterator,
                         @NotNull Expression expression,
                         @NotNull Context<?> context) {
    }

    @Serial
    private static final long serialVersionUID = -54943912839L;

    public MathTokenizer() {
        this(DoubleFunctionReflector.get(), DoubleConstantReflector.get(), DoubleOperatorReflector.get());
    }

    public MathTokenizer(@NotNull Map<String, ? extends Function<?>> functions) {
        this();
        options.getNames().putAll(functions);
    }

    public MathTokenizer(Map<String, ? extends Function<?>> functions,
                         Map<String, ? extends ImmutableSymbol<?>> constants,
                         Map<String, ? extends Operator<?>> operators) {
        options.getNames().putAll(functions);
        options.getNames().putAll(constants);
        options.getNames().putAll(operators);
    }

    @Override
    public synchronized
    @NotNull Group tokenize(@NotNull Expression expression,
                            @NotNull Context<?> context) throws TokenizeException {
        return new TokenGroup(start(new Buffer(expression.iterator(), expression, context)));
    }

    protected @NotNull Tokens start(@NotNull Buffer buffer) throws TokenizeException {
        Stack<Character> parenthesisStack = new Stack<>();
        Tokens tokens = new SimpleTokens();
        ExpressionIterator iterator = buffer.iterator;
        Expression expression = buffer.expression;

        if (expression.getString().isBlank() || expression.getString().isEmpty()) {
            return new SimpleTokens(new SimpleToken(TerminalType.TERMINAL, " ", 0));
        }

        while (iterator.hasNext()) {
            char current = iterator.current();
            switch (current) {
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
                    tokens.add(SpecialType.COMMA, current, iterator.getCursor());
                    iterator.next();
                    continue;

                default:
                    if (options.getAllowedOperatorSymbolsPredicate().test(iterator.current())) {
                        tokens.add(handleOperator(buffer));
                        if (!iterator.hasNext()) {
                            break;
                        }

                        continue;
                    }

                    if (options.getDigitPredicate().test(iterator.current())) {
                        tokens.add(handleNumber(buffer));
                        if (!iterator.hasNext()) {
                            break;
                        }

                        continue;
                    }

                    if (options.getLetterPredicate().test(iterator.current())) {
                        tokens.add(handleSymbols(buffer));
                        if (!iterator.hasNext()) {
                            break;
                        }

                        continue;
                    }

                    throw new UnknownCharacterException(current, expression, iterator.getCursor());
            }
        }

        if (!parenthesisStack.isEmpty()) {
            throw new BracketMismatchException(expression, iterator.getCursor());
        }

        tokens.add(TerminalType.TERMINAL, "", iterator.getCursor());
        return tokens;
    }

    protected Token handleNumber(Buffer buffer) throws InvalidNumberFormatException {
        StringBuilder acc = new StringBuilder();

        ExpressionIterator iterator = buffer.iterator;
        char current = iterator.current();
        final int start = iterator.getCursor();

        int imaginaryUnitCount = 0;
        int decimalPointCount = 0;
        int underscoreCount = 0;

        while (iterator.hasNext() && options.getDigitPredicate().test(current)) {
            if (current == TokenizerOptions.IMAGINARY_UNIT) {
                imaginaryUnitCount++;

            } else if (current == TokenizerOptions.UNDERSCORE) {
                underscoreCount++;

            } else if (current == TokenizerOptions.DECIMAL) {
                decimalPointCount++;
            }

            acc.append(current);
            iterator.next();
            if (!iterator.hasNext()) {
                break;
            }

            current = iterator.current();
        }

        if (imaginaryUnitCount > 1 || decimalPointCount > 1 || underscoreCount > 1) {
            throw new InvalidNumberFormatException(acc, buffer.expression, iterator.getCursor() - 1);
        }

        String[] splitted = acc.toString().split("_");

        Radix radix = Radix.DECIMAL;
        if (splitted.length == 2) {
            radix = new Radix(Double.parseDouble(splitted[1]));
        }

        Number number = new Number(splitted[0], radix);
        if (imaginaryUnitCount > 0) {
            return new NumberToken(NumberType.COMPLEX_NUMBER, number, start);
        }

        return new NumberToken(NumberType.NUMBER, number, start);
    }

    protected Token handleOperator(Buffer buffer)
            throws UnknownOperatorException {
        ExpressionIterator iterator = buffer.iterator;
        StringBuilder operator = new StringBuilder();
        final int start = iterator.getCursor();

        while (iterator.hasNext() && options.getAllowedOperatorSymbolsPredicate().test(iterator.current())) {
            operator.append(iterator.next());
            if (!iterator.hasNext()) {
                break;
            }
        }

        SearchResult operatorResult = find(operator, start);

        if (operatorResult.token == null) {
            throw new UnknownOperatorException(operator, buffer.expression, start);
        }

        return operatorResult.token;
    }

    protected Token handleSymbols(Buffer buffer) throws TokenizeException {
        ExpressionIterator iterator = buffer.iterator;
        StringBuilder symbols = new StringBuilder();
        final int start = iterator.getCursor();

        while (iterator.hasNext() && options.getLetterPredicate().test(iterator.current())) {
            symbols.append(iterator.next());
            if (!iterator.hasNext()) {
                break;
            }
        }

        SearchResult result = SearchResult.reduce(
                find(symbols, start),
                findFromContext(buffer.context, symbols, start)
        );

        if (!result.match) {
            iterator.setCursor(start);
            throw new UnknownSymbolTokenizeException(symbols, buffer.expression, start);
        }

        return result.token;
    }

    /**
     *
     * @param symbols Sequence of letters (candidate for nameable)
     * @return Search result
     */

    protected SearchResult find(CharSequence symbols, int start) {
        SearchResult result = new SearchResult();

        options.getNames().values().stream()
                .filter(Nameable.match(symbols))
                .findFirst()
                .ifPresent(s -> {
                    result.token = s.getToken(start);
                    result.match = true;
                });

        return result;
    }

    protected SearchResult findFromContext(Context<?> context, CharSequence symbols, int start) {
        SearchResult result = new SearchResult();

        context.getSymbol(symbols.toString())
                .ifPresent(s -> {
                    result.token = s.getToken(start);
                    result.match = true;
                });

        return result;
    }
}
