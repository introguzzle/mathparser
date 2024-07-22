package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.*;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.TokenType;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

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

    public Double parse(Tokens tokens, Context context) throws SyntaxException {
        tokens.skipDeclaration();
        Token token = tokens.getNextToken();

        if (token.getType() == TokenType.EOF) {
            return Double.NaN;
        }

        tokens.returnBack();
        return this.parseComparison(tokens, context);
    }

    private double parseComparison(Tokens tokens, Context context) throws SyntaxException {
        double value = this.parseBitwiseOr(tokens, context);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getType()) {
                case TokenType.OPERATOR_LESS:
                    value = value < this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                    break;
                case TokenType.OPERATOR_LESS_OR_EQUALS:
                    value = value <= this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                    break;
                case TokenType.OPERATOR_GREATER:
                    value = value > this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                    break;
                case TokenType.OPERATOR_GREATER_OR_EQUALS:
                    value = value >= this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                    break;
                case TokenType.OPERATOR_EQUALS:
                    value = value == this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                    break;
                case TokenType.OPERATOR_NOT_EQUALS:
                    value = value != this.parseBitwiseOr(tokens, context)
                            ? 1.0
                            : 0.0;
                case TokenType.EOF:
                case TokenType.RIGHT_PARENTHESIS:
                case TokenType.COMMA:
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
            switch (token.getType()) {
                case TokenType.OPERATOR_OR:
                    value = (long) value | (long) this.parseBitwiseExclusiveOr(tokens, context);
                    break;
                case TokenType.OPERATOR_LESS:
                case TokenType.OPERATOR_LESS_OR_EQUALS:
                case TokenType.OPERATOR_GREATER:
                case TokenType.OPERATOR_GREATER_OR_EQUALS:
                case TokenType.OPERATOR_EQUALS:
                case TokenType.OPERATOR_NOT_EQUALS:
                case TokenType.EOF:
                case TokenType.RIGHT_PARENTHESIS:
                case TokenType.COMMA:
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
            switch (token.getType()) {
                case TokenType.OPERATOR_XOR:
                    value = (long) value ^ (long) this.parseBitwiseAnd(tokens, context);
                    break;
                case TokenType.OPERATOR_OR:
                case TokenType.OPERATOR_LESS:
                case TokenType.OPERATOR_LESS_OR_EQUALS:
                case TokenType.OPERATOR_GREATER:
                case TokenType.OPERATOR_GREATER_OR_EQUALS:
                case TokenType.OPERATOR_EQUALS:
                case TokenType.OPERATOR_NOT_EQUALS:
                case TokenType.EOF:
                case TokenType.RIGHT_PARENTHESIS:
                case TokenType.COMMA:
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
            switch (token.getType()) {
                case TokenType.OPERATOR_AND:
                    value = (long) value & (long) this.parseBitwiseShift(tokens, context);
                    break;
                case TokenType.OPERATOR_LESS:
                case TokenType.OPERATOR_LESS_OR_EQUALS:
                case TokenType.OPERATOR_GREATER:
                case TokenType.OPERATOR_GREATER_OR_EQUALS:
                case TokenType.OPERATOR_EQUALS:
                case TokenType.OPERATOR_NOT_EQUALS:
                case TokenType.OPERATOR_OR:
                case TokenType.OPERATOR_XOR:
                case TokenType.EOF:
                case TokenType.RIGHT_PARENTHESIS:
                case TokenType.COMMA:
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
            switch (token.getType()) {
                case TokenType.OPERATOR_LEFT_SHIFT:
                    value = (long) value << (long) this.parseAdditionSubtraction(tokens, context);
                    break;

                case TokenType.OPERATOR_RIGHT_SHIFT:
                    value = (long) value >> (long) this.parseAdditionSubtraction(tokens, context);
                    break;

                case TokenType.OPERATOR_LESS:
                case TokenType.OPERATOR_LESS_OR_EQUALS:
                case TokenType.OPERATOR_GREATER:
                case TokenType.OPERATOR_GREATER_OR_EQUALS:
                case TokenType.OPERATOR_EQUALS:
                case TokenType.OPERATOR_NOT_EQUALS:
                case TokenType.OPERATOR_OR:
                case TokenType.OPERATOR_AND:
                case TokenType.OPERATOR_XOR:
                case TokenType.EOF:
                case TokenType.RIGHT_PARENTHESIS:
                case TokenType.COMMA:
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
            switch (token.getType()) {
                case TokenType.OPERATOR_ADD:
                    value += this.parseMultiplicationDivision(tokens, context);
                    break;
                case TokenType.OPERATOR_SUB:
                    value -= this.parseMultiplicationDivision(tokens, context);
                    break;
                case TokenType.OPERATOR_LESS:
                case TokenType.OPERATOR_LESS_OR_EQUALS:
                case TokenType.OPERATOR_GREATER:
                case TokenType.OPERATOR_GREATER_OR_EQUALS:
                case TokenType.OPERATOR_EQUALS:
                case TokenType.OPERATOR_NOT_EQUALS:
                case TokenType.OPERATOR_LEFT_SHIFT:
                case TokenType.OPERATOR_RIGHT_SHIFT:
                case TokenType.OPERATOR_AND:
                case TokenType.OPERATOR_OR:
                case TokenType.OPERATOR_XOR:
                case TokenType.EOF:
                case TokenType.RIGHT_PARENTHESIS:
                case TokenType.COMMA:
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
            switch (token.getType()) {
                case TokenType.OPERATOR_MUL:
                    value *= this.parseExponent(tokens, context);
                    break;
                case TokenType.OPERATOR_DIV:
                    value /= this.parseExponent(tokens, context);
                    break;
                case TokenType.OPERATOR_LESS:
                case TokenType.OPERATOR_LESS_OR_EQUALS:
                case TokenType.OPERATOR_GREATER:
                case TokenType.OPERATOR_GREATER_OR_EQUALS:
                case TokenType.OPERATOR_EQUALS:
                case TokenType.OPERATOR_NOT_EQUALS:
                case TokenType.OPERATOR_OR:
                case TokenType.OPERATOR_ADD:
                case TokenType.OPERATOR_SUB:
                case TokenType.OPERATOR_AND:
                case TokenType.OPERATOR_XOR:
                case TokenType.OPERATOR_LEFT_SHIFT:
                case TokenType.OPERATOR_RIGHT_SHIFT:
                case TokenType.EOF:
                case TokenType.RIGHT_PARENTHESIS:
                case TokenType.COMMA:
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
            switch (token.getType()) {
                case TokenType.OPERATOR_EXP:
                    value = Math.pow(value, parseExponent(tokens, context));
                    break;

                case TokenType.OPERATOR_LESS:
                case TokenType.OPERATOR_LESS_OR_EQUALS:
                case TokenType.OPERATOR_GREATER:
                case TokenType.OPERATOR_GREATER_OR_EQUALS:
                case TokenType.OPERATOR_EQUALS:
                case TokenType.OPERATOR_NOT_EQUALS:
                case TokenType.OPERATOR_OR:
                case TokenType.OPERATOR_ADD:
                case TokenType.OPERATOR_SUB:
                case TokenType.OPERATOR_MUL:
                case TokenType.OPERATOR_DIV:
                case TokenType.OPERATOR_XOR:
                case TokenType.OPERATOR_LEFT_SHIFT:
                case TokenType.OPERATOR_RIGHT_SHIFT:
                case TokenType.OPERATOR_AND:
                case TokenType.EOF:
                case TokenType.RIGHT_PARENTHESIS:
                case TokenType.COMMA:
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
            case TokenType.FUNCTION_NAME:
                tokens.returnBack();
                return this.parseFunction(tokens, context);

            case TokenType.OPERATOR_SUB:
                return -parseFactor(tokens, context);

            case TokenType.OPERATOR_ADD:
                return parseFactor(tokens, context);

            case TokenType.OPERATOR_BITWISE_NOT:
                return ~((long) parseFactor(tokens, context));

            case TokenType.NUMBER:
                return Double.parseDouble(token.getData());

            case TokenType.CONSTANT:
                final Token t = token;
                Optional<ImmutableSymbol> symbol = tokenizer.getConstants()
                        .stream()
                        .filter(s -> s.getName().equals(t.getData()))
                        .findFirst();

                return symbol.orElseThrow().getValue();

            case TokenType.VARIABLE:
            case TokenType.COEFFICIENT:
                return context.getSymbol(token.getData()).orElseThrow().getValue();

            case TokenType.LEFT_PARENTHESIS:
                double value = this.parse(tokens, context);
                token = tokens.getNextToken();
                if (token.getType() != TokenType.RIGHT_PARENTHESIS) {
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

        if (token.getType() != TokenType.RIGHT_PARENTHESIS) {
            tokens.returnBack();
            do {
                arguments.add(parse(tokens, context));
                token = tokens.getNextToken();

                if ((token.getType() != TokenType.COMMA) && (token.getType() != TokenType.RIGHT_PARENTHESIS)) {
                    throw new RuntimeException();
                }

            } while (token.getType() == TokenType.COMMA);
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
