package ru.impl;

import ru.exceptions.MathSyntaxException;
import ru.exceptions.ParseException;
import ru.exceptions.VariableMismatchException;
import ru.function.Function;
import ru.contract.*;
import ru.tokens.Token;
import ru.tokens.TokenType;
import ru.tokens.Tokens;
import ru.variable.Variables;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record MathParser(Tokenizer tokenizer) implements Parser<Double> {

    private ParseException createUnexpectedTokenException(Tokens tokens, Token token) {
        return new ParseException("Unexpected token: '" + token.getTokenType() + "' at pos " + tokens.getPosition() + " in expression");
    }

    @Override
    public Double parse(Expression expression) throws MathSyntaxException {
        return this.parse(expression, new Variables());
    }

    @Override
    public Double parse(Expression expression, Variables variables) throws MathSyntaxException {
        Tokens tokens = tokenizer.tokenize(expression);

        if (variables.size() != tokens.getVariableCount()) {
            throw new VariableMismatchException(variables.size(), tokens.getVariableCount());
        }

        return this.parseExpression(tokens, variables);
    }

    @Override
    public
    BigDecimal parseBigDecimal(Expression expression)
            throws MathSyntaxException {
        return BigDecimal.valueOf(this.parse(expression));
    }

    @Override
    public
    BigDecimal parseBigDecimal(Expression expression, Variables variables)
            throws MathSyntaxException {
        return BigDecimal.valueOf(this.parse(expression, variables));
    }

    @Override
    public
    BigInteger parseBigInteger(Expression expression)
            throws MathSyntaxException {
        return BigInteger.valueOf(this.parse(expression).longValue());
    }

    @Override
    public BigInteger parseBigInteger(Expression expression, Variables variables)
            throws MathSyntaxException {
        return BigInteger.valueOf(this.parse(expression, variables).longValue());
    }

    @Override
    public Optional<Double> parseOptional(Expression expression) {
        try {
            return Optional.of(this.parse(expression));
        } catch (MathSyntaxException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Double> parseOptional(Expression expression, Variables variables) {
        try {
            return Optional.of(this.parse(expression, variables));
        } catch (MathSyntaxException e) {
            return Optional.empty();
        }
    }

    private Double parseExpression(Tokens tokens, Variables variables) throws MathSyntaxException {
        Token token = tokens.getNextToken();
        if (token.getTokenType() == TokenType.EOF) {
            return 0.0;
        }

        tokens.returnBack();
        return this.parseComparison(tokens, variables);
    }

    private double parseComparison(Tokens tokens, Variables variables) throws MathSyntaxException {
        double value = this.parseBitwiseOr(tokens, variables);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_LESS:
                    value = value < this.parseBitwiseOr(tokens, variables)
                            ? 1.0
                            : 0.0;
                    break;
                case OPERATOR_LESS_OR_EQUALS:
                    value = value <= this.parseBitwiseOr(tokens, variables)
                            ? 1.0
                            : 0.0;
                    break;
                case OPERATOR_GREATER:
                    value = value > this.parseBitwiseOr(tokens, variables)
                            ? 1.0
                            : 0.0;
                    break;
                case OPERATOR_GREATER_OR_EQUALS:
                    value = value >= this.parseBitwiseOr(tokens, variables)
                            ? 1.0
                            : 0.0;
                    break;
                case OPERATOR_EQUALS:
                    value = value == this.parseBitwiseOr(tokens, variables)
                            ? 1.0
                            : 0.0;
                    break;
                case OPERATOR_NOT_EQUALS:
                    value = value != this.parseBitwiseOr(tokens, variables)
                            ? 1.0
                            : 0.0;
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                    tokens.returnBack();

                    return value;
                default:
                    throw this.createUnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseBitwiseOr(Tokens tokens, Variables variables) throws MathSyntaxException {
        double value = this.parseBitwiseExclusiveOr(tokens, variables);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_OR:
                    value = (long) value | (long) this.parseBitwiseExclusiveOr(tokens, variables);
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
                    throw this.createUnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseBitwiseExclusiveOr(Tokens tokens, Variables variables) throws MathSyntaxException {
        double value = this.parseBitwiseAnd(tokens, variables);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_XOR:
                    value = (long) value ^ (long) this.parseBitwiseAnd(tokens, variables);
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
                    throw this.createUnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseBitwiseAnd(Tokens tokens, Variables variables) throws MathSyntaxException {
        double value = this.parseBitwiseShift(tokens, variables);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_AND:
                    value = (long) value & (long) this.parseBitwiseShift(tokens, variables);
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
                    throw this.createUnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseBitwiseShift(Tokens tokens, Variables variables) throws MathSyntaxException {
        double value = this.parseAdditionSubtraction(tokens, variables);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_LEFT_SHIFT:
                    value = (long) value << (long) this.parseAdditionSubtraction(tokens, variables);
                    break;

                case OPERATOR_RIGHT_SHIFT:
                    value = (long) value >> (long) this.parseAdditionSubtraction(tokens, variables);
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
                    throw this.createUnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseAdditionSubtraction(Tokens tokens, Variables variables) throws MathSyntaxException {
        double value = this.parseMultiplicationDivision(tokens, variables);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_ADD:
                    value += this.parseMultiplicationDivision(tokens, variables);
                    break;
                case OPERATOR_SUB:
                    value -= this.parseMultiplicationDivision(tokens, variables);
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
                    throw this.createUnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseMultiplicationDivision(Tokens tokens, Variables variables) throws MathSyntaxException {
        double value = this.parseExponent(tokens, variables);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_MUL:
                    value *= this.parseExponent(tokens, variables);
                    break;
                case OPERATOR_DIV:
                    value /= this.parseExponent(tokens, variables);
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
                    throw this.createUnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseExponent(Tokens tokens, Variables variables) throws MathSyntaxException {
        double value = this.parseFactor(tokens, variables);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_EXP:
                    value = Math.pow(value, parseExponent(tokens, variables));
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
                    throw this.createUnexpectedTokenException(tokens, token);
            }
        }
    }

    private double parseFactor(Tokens tokens, Variables variables) throws MathSyntaxException {
        Token token = tokens.getNextToken();

        switch (token.getTokenType()) {
            case FUNCTION_NAME:
                tokens.returnBack();
                return this.parseFunction(tokens, variables);

            case OPERATOR_SUB:
                return -parseFactor(tokens, variables);

            case OPERATOR_ADD:
                return parseFactor(tokens, variables);

            case OPERATOR_BITWISE_NOT:
                long x = (long) parseFactor(tokens, variables);
                return ~x;

            case NUMBER:
                return Double.parseDouble(token.getData());

            case CONSTANT:
                Token finalToken = token;
                Optional<Symbol> symbol = this.tokenizer.getConstants()
                        .stream()
                        .filter(s -> s.getRepresentation().equals(finalToken.getData()))
                        .findFirst();

                return symbol.orElseThrow().getValue();

            case VARIABLE:
                return variables.find(token.getData()).orElseThrow().getValue();

            case LEFT_BRACKET:
                double value = this.parseExpression(tokens, variables);
                token = tokens.getNextToken();
                if (token.getTokenType() != TokenType.RIGHT_BRACKET) {
                    throw this.createUnexpectedTokenException(tokens, token);
                }

                return value;

            default:
                throw this.createUnexpectedTokenException(tokens, token);
        }
    }

    private double parseFunction(Tokens tokens, Variables variables) throws MathSyntaxException {
        String name = tokens.getNextToken().getData();
        Token token = tokens.getNextToken();

        if (token.getTokenType() != TokenType.LEFT_BRACKET) {
            throw new ParseException();
        }

        List<Double> args = new ArrayList<>();
        token = tokens.getNextToken();

        if (token.getTokenType() != TokenType.RIGHT_BRACKET) {
            tokens.returnBack();
            do {
                args.add(parseExpression(tokens, variables));
                token = tokens.getNextToken();

                if ((token.getTokenType() != TokenType.COMMA) && (token.getTokenType() != TokenType.RIGHT_BRACKET)) {
                    throw new ParseException();
                }

            } while (token.getTokenType() == TokenType.COMMA);
        }

        Function function = this.tokenizer.getFunctions().get(name);
        int given = args.size();

        if (function.isVariadic()) {
            if (function.getRequiredArguments() > given) {
                throw function.createException(given);
            } else {
                return function.apply(args);
            }
        }

        if (function.getRequiredArguments() == given) {
            return function.apply(args);
        }

        throw function.createException(given);
    }
}
