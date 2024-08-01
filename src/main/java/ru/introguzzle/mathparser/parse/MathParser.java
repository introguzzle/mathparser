package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.*;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.Tokens;
import ru.introguzzle.mathparser.tokenize.token.type.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MathParser implements Parser<Double>, Serializable {
    protected final Tokenizer tokenizer;

    public MathParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Serial
    private static final long serialVersionUID = -2443784738437783L;

    protected Tokens tokenize(Expression expression, Context context) throws TokenizeException {
        return tokenizer.tokenize(expression, context);
    }

    @Override
    public Double parse(@NotNull Expression expression) throws SyntaxException {
        return parse(expression, new NamingContext());
    }

    @Override
    public Double parse(@NotNull Expression expression,
                        @NotNull Context context) throws SyntaxException {
        Tokens tokens = this.tokenize(expression, context);
        return parse(tokens, context);
    }

    @Override
    public Double parse(@NotNull Tokens tokens, Context context) throws SyntaxException {
        tokens.skipDeclaration();
        Token token = tokens.getNextToken();

        if (token.getType().isTerminal()) {
            return Double.NaN;
        }

        tokens.returnBack();
        return parseComparison(tokens, context);
    }

    private double parseComparison(Tokens tokens, Context context) throws SyntaxException {
        double value = parseBitwiseOr(tokens, context);
        Token token = tokens.getNextToken();

        if (token.getType() instanceof OperatorType operator) {
            if (operator.getPriority() == Priorities.COMPARISON_PRIORITY) {
                return operator.apply(List.of(value, parseBitwiseOr(tokens, context)));
            }
        }

        return switch (token.getType()) {
            case TerminalType.TERMINAL, ParenthesisType.RIGHT, SpecialType.COMMA -> {
                tokens.returnBack();
                yield value;
            }

            default -> throw new UnexpectedTokenException(tokens, token);
        };
    }

    private double parseBitwiseOr(Tokens tokens, Context context) throws SyntaxException {
        double value = parseBitwiseExclusiveOr(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getType()) {
                case OperatorType.OR:
                    value = (long) value | (long) parseBitwiseExclusiveOr(tokens, context);
                    break;
                case OperatorType.LESS:
                case OperatorType.LESS_OR_EQUALS:
                case OperatorType.GREATER:
                case OperatorType.GREATER_OR_EQUALS:
                case OperatorType.EQUALS:
                case OperatorType.NOT_EQUALS:
                case TerminalType.TERMINAL:
                case ParenthesisType.RIGHT:
                case SpecialType.COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseBitwiseExclusiveOr(Tokens tokens, Context context) throws SyntaxException {
        double value = parseBitwiseAnd(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getType()) {
                case OperatorType.XOR:
                    value = (long) value ^ (long) parseBitwiseAnd(tokens, context);
                    break;
                case OperatorType.OR:
                case OperatorType.LESS:
                case OperatorType.LESS_OR_EQUALS:
                case OperatorType.GREATER:
                case OperatorType.GREATER_OR_EQUALS:
                case OperatorType.EQUALS:
                case OperatorType.NOT_EQUALS:
                case TerminalType.TERMINAL:
                case ParenthesisType.RIGHT:
                case SpecialType.COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseBitwiseAnd(Tokens tokens, Context context) throws SyntaxException {
        double value = parseBitwiseShift(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getType()) {
                case OperatorType.AND:
                    value = (long) value & (long) parseBitwiseShift(tokens, context);
                    break;
                case OperatorType.LESS:
                case OperatorType.LESS_OR_EQUALS:
                case OperatorType.GREATER:
                case OperatorType.GREATER_OR_EQUALS:
                case OperatorType.EQUALS:
                case OperatorType.NOT_EQUALS:
                case OperatorType.OR:
                case OperatorType.XOR:
                case TerminalType.TERMINAL:
                case ParenthesisType.RIGHT:
                case SpecialType.COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseBitwiseShift(Tokens tokens, Context context) throws SyntaxException {
        double value = parseAdditionSubtraction(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getType()) {
                case OperatorType.LEFT_SHIFT:
                    value = (long) value << (long) parseAdditionSubtraction(tokens, context);
                    break;

                case OperatorType.RIGHT_SHIFT:
                    value = (long) value >> (long) parseAdditionSubtraction(tokens, context);
                    break;

                case OperatorType.LESS:
                case OperatorType.LESS_OR_EQUALS:
                case OperatorType.GREATER:
                case OperatorType.GREATER_OR_EQUALS:
                case OperatorType.EQUALS:
                case OperatorType.NOT_EQUALS:
                case OperatorType.OR:
                case OperatorType.AND:
                case OperatorType.XOR:
                case TerminalType.TERMINAL:
                case ParenthesisType.RIGHT:
                case SpecialType.COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseAdditionSubtraction(Tokens tokens, Context context) throws SyntaxException {
        double value = parseMultiplicationDivision(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getType()) {
                case OperatorType.ADDITION:
                    value += parseMultiplicationDivision(tokens, context);
                    break;
                case OperatorType.SUBTRACTION:
                    value -= parseMultiplicationDivision(tokens, context);
                    break;
                case OperatorType.LESS:
                case OperatorType.LESS_OR_EQUALS:
                case OperatorType.GREATER:
                case OperatorType.GREATER_OR_EQUALS:
                case OperatorType.EQUALS:
                case OperatorType.NOT_EQUALS:
                case OperatorType.LEFT_SHIFT:
                case OperatorType.RIGHT_SHIFT:
                case OperatorType.AND:
                case OperatorType.OR:
                case OperatorType.XOR:
                case TerminalType.TERMINAL:
                case ParenthesisType.RIGHT:
                case SpecialType.COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseMultiplicationDivision(Tokens tokens, Context context) throws SyntaxException {
        double value = parseExponent(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getType()) {
                case OperatorType.MULTIPLICATION:
                    value *= parseExponent(tokens, context);
                    break;
                case OperatorType.DIVISION:
                    value /= parseExponent(tokens, context);
                    break;
                case OperatorType.LESS:
                case OperatorType.LESS_OR_EQUALS:
                case OperatorType.GREATER:
                case OperatorType.GREATER_OR_EQUALS:
                case OperatorType.EQUALS:
                case OperatorType.NOT_EQUALS:
                case OperatorType.OR:
                case OperatorType.ADDITION:
                case OperatorType.SUBTRACTION:
                case OperatorType.AND:
                case OperatorType.XOR:
                case OperatorType.LEFT_SHIFT:
                case OperatorType.RIGHT_SHIFT:
                case TerminalType.TERMINAL:
                case ParenthesisType.RIGHT:
                case SpecialType.COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseExponent(Tokens tokens, Context context) throws SyntaxException {
        double value = parseFactor(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getType()) {
                case OperatorType.EXPONENT:
                    value = Math.pow(value, parseExponent(tokens, context));
                    break;

                case OperatorType.LESS:
                case OperatorType.LESS_OR_EQUALS:
                case OperatorType.GREATER:
                case OperatorType.GREATER_OR_EQUALS:
                case OperatorType.EQUALS:
                case OperatorType.NOT_EQUALS:
                case OperatorType.OR:
                case OperatorType.ADDITION:
                case OperatorType.SUBTRACTION:
                case OperatorType.MULTIPLICATION:
                case OperatorType.DIVISION:
                case OperatorType.XOR:
                case OperatorType.LEFT_SHIFT:
                case OperatorType.RIGHT_SHIFT:
                case OperatorType.AND:
                case TerminalType.TERMINAL:
                case ParenthesisType.RIGHT:
                case SpecialType.COMMA:
                    tokens.returnBack();

                    return value;

                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseFactor(Tokens tokens, Context context) throws SyntaxException {
        Token token = tokens.getNextToken();

        switch (token.getType()) {
            case FunctionType.FUNCTION:
                tokens.returnBack();
                return parseFunction(tokens, context);

            case OperatorType.SUBTRACTION:
                return -parseFactor(tokens, context);

            case OperatorType.ADDITION:
                return parseFactor(tokens, context);

            case OperatorType.BITWISE_NOT:
                return ~((long) parseFactor(tokens, context));

            case NumberType.NUMBER:
                return Double.parseDouble(token.getData());

            case SymbolType.CONSTANT:
                Optional<ImmutableSymbol> symbol = tokenizer.findConstant(token);
                return symbol.orElseThrow().getValue();

            case SymbolType.VARIABLE:
            case SymbolType.COEFFICIENT:
                return context.getSymbol(token.getData()).orElseThrow().getValue();

            case ParenthesisType.LEFT:
                double value = parse(tokens, context);
                token = tokens.getNextToken();
                if (token.getType() != ParenthesisType.RIGHT) {
                    throw new UnexpectedTokenException(tokens, token);
                }

                return value;

            default:
                throw new UnexpectedTokenException(tokens, token);
        }
    }

    private double parseFunction(Tokens tokens, Context context) throws SyntaxException {
        String name = tokens.getNextToken().getData();
        tokens.getNextToken();

        List<Double> arguments = new ArrayList<>();
        Token token = tokens.getNextToken();

        if (token.getType() != ParenthesisType.RIGHT) {
            tokens.returnBack();
            do {
                arguments.add(parse(tokens, context));
                token = tokens.getNextToken();

                if ((token.getType() != SpecialType.COMMA) && (token.getType() != ParenthesisType.RIGHT)) {
                    // Should never happen, because tokenizer won't let this pass to here
                    throw new RuntimeException();
                }

            } while (token.getType() == SpecialType.COMMA);
        }

        Function function = tokenizer.findFunction(name).orElseThrow();
        int given = arguments.size();

        if (function.isVariadic()) {
            if (function.getRequiredArguments() > given) {
                throw function.createException(given);
            } else {
                return function.apply(arguments);
            }
        }

        if (function.getRequiredArguments() == given) {
            return function.apply(arguments);
        }

        throw function.createException(given);
    }
}
