package ru.implementation;

import ru.constant.Constants;
import ru.exceptions.MathSyntaxException;
import ru.exceptions.ParseException;
import ru.function.Function;
import ru.function.Functions;
import ru.main.*;
import ru.tokens.Token;
import ru.variable.Variables;

import java.util.ArrayList;
import java.util.List;

public class MathParser implements Parser<Double> {

    private final Tokenizer tokenizer;

    public MathParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

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

        return this.expression(tokens, variables);
    }

    @Override
    public Double parseOrNull(Expression expression) {
        try {
            return this.parse(expression);
        } catch (MathSyntaxException e) {
            return null;
        }
    }

    @Override
    public Double parseOrNull(Expression expression, Variables variables) {
        try {
            return this.parse(expression, variables);
        } catch (MathSyntaxException e) {
            return null;
        }
    }

    private Double expression(Tokens tokens, Variables variables) throws MathSyntaxException {
        Token token = tokens.getNextToken();
        if (token.getTokenType() == TokenType.EOF) {
            return 0.0;
        } else {
            tokens.returnBack();
            return this.addSubtract(tokens, variables);
        }
    }

    public double addSubtract(Tokens tokens, Variables variables) throws MathSyntaxException {
        double value = this.multiplyDivide(tokens, variables);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_ADD:
                    value += this.multiplyDivide(tokens, variables);
                    break;
                case OPERATOR_SUB:
                    value -= this.multiplyDivide(tokens, variables);
                    break;
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

    public double multiplyDivide(Tokens tokens, Variables variables) throws MathSyntaxException {
        double value = this.exp(tokens, variables);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_MUL:
                    value *= this.exp(tokens, variables);
                    break;
                case OPERATOR_DIV:
                    value /= this.exp(tokens, variables);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                case OPERATOR_ADD:
                case OPERATOR_SUB:
                    tokens.returnBack();

                    return value;
                default:
                   throw this.createUnexpectedTokenException(tokens, token);
            }
        }
    }

    public double exp(Tokens tokens, Variables variables) throws MathSyntaxException {
        double value = this.factor(tokens, variables);
        while (true) {
            Token token = tokens.getNextToken();
            switch (token.getTokenType()) {
                case OPERATOR_EXP:
                    value = Math.pow(value, exp(tokens, variables));
                    break;

                case OPERATOR_MUL:
                case OPERATOR_DIV:
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                case OPERATOR_ADD:
                case OPERATOR_SUB:
                    tokens.returnBack();

                    return value;

                default:
                    throw this.createUnexpectedTokenException(tokens, token);
            }
        }
    }

    public double factor(Tokens tokens, Variables variables) throws MathSyntaxException {
        Token token = tokens.getNextToken();

        switch (token.getTokenType()) {
            case FUNCTION_NAME:
                tokens.returnBack();
                return this.function(tokens, variables);

            case OPERATOR_SUB:
                return -factor(tokens, variables);

            case NUMBER:
                return Double.parseDouble(token.getData());

            case CONSTANT:
                var optionalSymbol = Constants.find(token.getData());
                return optionalSymbol.orElseThrow().getValue();

            case VARIABLE:
                return variables.find(token.getData()).orElseThrow().getValue();

            case LEFT_BRACKET:
                double value = this.expression(tokens, variables);
                token = tokens.getNextToken();
                if (token.getTokenType() != TokenType.RIGHT_BRACKET) {
                    throw this.createUnexpectedTokenException(tokens, token);
                }

                return value;

            default:
                throw this.createUnexpectedTokenException(tokens, token);
        }
    }

    public double function(Tokens tokens, Variables variables) throws MathSyntaxException {
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
                args.add(expression(tokens, variables));
                token = tokens.getNextToken();

                if ((token.getTokenType() != TokenType.COMMA) && (token.getTokenType() != TokenType.RIGHT_BRACKET)) {
                    throw new ParseException();
                }

            } while (token.getTokenType() == TokenType.COMMA);
        }

        Function function = Functions.get().get(name);
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
