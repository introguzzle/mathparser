package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.tokenize.*;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MathParser implements Parser<Double>, Serializable {

    private final Tokenizer tokenizer;

    public MathParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Serial
    private static final long serialVersionUID = -2443784738437783L;

    private Tokens tokenize(Expression expression, Context context) throws TokenizeException {
        return this.tokenizer.tokenize(expression, context);
    }

    @Override
    public Double parse(@NotNull Expression expression) throws SyntaxException {
        return this.parse(expression, new NamingContext());
    }

    @Override
    public Double parse(@NotNull Expression expression,
                        @NotNull Context context) throws SyntaxException {
        Tokens tokens = this.tokenize(expression, context);
        return this.parse(tokens, context);
    }

    @Override
    public Optional<Double> tryParse(@Nullable Expression expression) {
        if (expression == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(this.parse(expression));
        } catch (SyntaxException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Double> tryParse(@Nullable Expression expression,
                                     @Nullable Context context) {
        if (expression == null || context == null) {
            return Optional.empty();
        }

        try {
            Double value = this.parse(expression, context);
            return Optional.of(value);
        } catch (SyntaxException e) {
            return Optional.empty();
        }
    }

    private Double parse(Tokens tokens, Context context) throws SyntaxException {
        tokens.skipDeclaration();
        Token token = tokens.getNextToken();

        if (token.getTokenType() == TokenType.EOF) {
            return 0.0;
        }

        tokens.returnBack();
        return this.parseComparison(tokens, context);
    }

    private double parseComparison(Tokens tokens, Context context) throws SyntaxException {
        double value = this.parseBitwiseOr(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_LESS:
                    value = value < this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                    break;
                case OPERATOR_LESS_OR_EQUALS:
                    value = value <= this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                    break;
                case OPERATOR_GREATER:
                    value = value > this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                    break;
                case OPERATOR_GREATER_OR_EQUALS:
                    value = value >= this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                    break;
                case OPERATOR_EQUALS:
                    value = value == this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                    break;
                case OPERATOR_NOT_EQUALS:
                    value = value != this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseBitwiseOr(Tokens tokens, Context context) throws SyntaxException {
        double value = this.parseBitwiseExclusiveOr(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_OR:
                    value = (long) value | (long) this.parseBitwiseExclusiveOr(tokens, context);
                    break;
                case OPERATOR_LESS:
                case OPERATOR_LESS_OR_EQUALS:
                case OPERATOR_GREATER:
                case OPERATOR_GREATER_OR_EQUALS:
                case OPERATOR_EQUALS:
                case OPERATOR_NOT_EQUALS:
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseBitwiseExclusiveOr(Tokens tokens, Context context) throws SyntaxException {
        double value = this.parseBitwiseAnd(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_XOR:
                    value = (long) value ^ (long) this.parseBitwiseAnd(tokens, context);
                    break;
                case OPERATOR_OR:
                case OPERATOR_LESS:
                case OPERATOR_LESS_OR_EQUALS:
                case OPERATOR_GREATER:
                case OPERATOR_GREATER_OR_EQUALS:
                case OPERATOR_EQUALS:
                case OPERATOR_NOT_EQUALS:
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseBitwiseAnd(Tokens tokens, Context context) throws SyntaxException {
        double value = this.parseBitwiseShift(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_AND:
                    value = (long) value & (long) this.parseBitwiseShift(tokens, context);
                    break;
                case OPERATOR_LESS:
                case OPERATOR_LESS_OR_EQUALS:
                case OPERATOR_GREATER:
                case OPERATOR_GREATER_OR_EQUALS:
                case OPERATOR_EQUALS:
                case OPERATOR_NOT_EQUALS:
                case OPERATOR_OR:
                case OPERATOR_XOR:
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseBitwiseShift(Tokens tokens, Context context) throws SyntaxException {
        double value = this.parseAdditionSubtraction(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_LEFT_SHIFT:
                    value = (long) value << (long) this.parseAdditionSubtraction(tokens, context);
                    break;

                case OPERATOR_RIGHT_SHIFT:
                    value = (long) value >> (long) this.parseAdditionSubtraction(tokens, context);
                    break;

                case OPERATOR_LESS:
                case OPERATOR_LESS_OR_EQUALS:
                case OPERATOR_GREATER:
                case OPERATOR_GREATER_OR_EQUALS:
                case OPERATOR_EQUALS:
                case OPERATOR_NOT_EQUALS:
                case OPERATOR_OR:
                case OPERATOR_AND:
                case OPERATOR_XOR:
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseAdditionSubtraction(Tokens tokens, Context context) throws SyntaxException {
        double value = this.parseMultiplicationDivision(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_ADD:
                    value += this.parseMultiplicationDivision(tokens, context);
                    break;
                case OPERATOR_SUB:
                    value -= this.parseMultiplicationDivision(tokens, context);
                    break;
                case OPERATOR_LESS:
                case OPERATOR_LESS_OR_EQUALS:
                case OPERATOR_GREATER:
                case OPERATOR_GREATER_OR_EQUALS:
                case OPERATOR_EQUALS:
                case OPERATOR_NOT_EQUALS:
                case OPERATOR_LEFT_SHIFT:
                case OPERATOR_RIGHT_SHIFT:
                case OPERATOR_AND:
                case OPERATOR_OR:
                case OPERATOR_XOR:
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseMultiplicationDivision(Tokens tokens, Context context) throws SyntaxException {
        double value = this.parseExponent(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_MUL:
                    value *= this.parseExponent(tokens, context);
                    break;
                case OPERATOR_DIV:
                    value /= this.parseExponent(tokens, context);
                    break;
                case OPERATOR_LESS:
                case OPERATOR_LESS_OR_EQUALS:
                case OPERATOR_GREATER:
                case OPERATOR_GREATER_OR_EQUALS:
                case OPERATOR_EQUALS:
                case OPERATOR_NOT_EQUALS:
                case OPERATOR_OR:
                case OPERATOR_ADD:
                case OPERATOR_SUB:
                case OPERATOR_AND:
                case OPERATOR_XOR:
                case OPERATOR_LEFT_SHIFT:
                case OPERATOR_RIGHT_SHIFT:
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseExponent(Tokens tokens, Context context) throws SyntaxException {
        double value = this.parseFactor(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_EXP:
                    value = Math.pow(value, parseExponent(tokens, context));
                    break;

                case OPERATOR_LESS:
                case OPERATOR_LESS_OR_EQUALS:
                case OPERATOR_GREATER:
                case OPERATOR_GREATER_OR_EQUALS:
                case OPERATOR_EQUALS:
                case OPERATOR_NOT_EQUALS:
                case OPERATOR_OR:
                case OPERATOR_ADD:
                case OPERATOR_SUB:
                case OPERATOR_MUL:
                case OPERATOR_DIV:
                case OPERATOR_XOR:
                case OPERATOR_LEFT_SHIFT:
                case OPERATOR_RIGHT_SHIFT:
                case OPERATOR_AND:
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                    tokens.returnBack();

                    return value;

                default:
                    throw new UnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseFactor(Tokens tokens, Context context) throws SyntaxException {
        Token token = tokens.getNextToken();

        switch (token.getTokenType()) {
            case FUNCTION_NAME:
                tokens.returnBack();
                return this.parseFunction(tokens, context);

            case OPERATOR_SUB:
                return -parseFactor(tokens, context);

            case OPERATOR_ADD:
                return parseFactor(tokens, context);

            case OPERATOR_BITWISE_NOT:
                return ~((long) parseFactor(tokens, context));

            case NUMBER:
                return Double.parseDouble(token.getData());

            case CONSTANT:
                final Token t = token;
                Optional<ImmutableSymbol> symbol = this.tokenizer.getConstants()
                        .stream()
                        .filter(s -> s.getName().equals(t.getData()))
                        .findFirst();

                return symbol.orElseThrow().getValue();

            case VARIABLE:
                return context.getSymbol(token.getData()).orElseThrow().getValue();

            case LEFT_BRACKET:
                double value = this.parse(tokens, context);
                token = tokens.getNextToken();
                if (token.getTokenType() != TokenType.RIGHT_BRACKET) {
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

        if (token.getTokenType() != TokenType.RIGHT_BRACKET) {
            tokens.returnBack();
            do {
                arguments.add(parse(tokens, context));
                token = tokens.getNextToken();

                if ((token.getTokenType() != TokenType.COMMA) && (token.getTokenType() != TokenType.RIGHT_BRACKET)) {
                    throw new RuntimeException();
                }

            } while (token.getTokenType() == TokenType.COMMA);
        }

        Function function = this.tokenizer.getFunctions()
                .stream()
                .filter(Nameable.match(name))
                .findFirst()
                .orElseThrow();


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
