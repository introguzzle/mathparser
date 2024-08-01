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
import java.util.*;

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
    public Double parse(@NotNull Expression expression, @NotNull Context context) throws SyntaxException {
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
        return parseExpression(tokens, context, Integer.MAX_VALUE);
    }

    private double parseExpression(Tokens tokens, Context context, int priority) throws SyntaxException {
        double leftValue = parseFactor(tokens, context);

        while (true) {
            Token token = tokens.getNextToken();
            ScalarOperatorType operator = tokenizer.getOperators().get(token.getData());

            if (operator == null || operator.getPriority() >= priority) {
                tokens.returnBack();
                break;
            }

            int nextPriority = operator.isRightAssociative()
                    ? operator.getPriority() + 1
                    : operator.getPriority();

            double rightValue = parseExpression(tokens, context, nextPriority);
            leftValue = operator.apply(List.of(leftValue, rightValue));
        }

        return leftValue;
    }

    private double parseFactor(Tokens tokens, Context context) throws SyntaxException {
        Token token = tokens.getNextToken();

        switch (token.getType()) {
            case FunctionType.FUNCTION:
                tokens.returnBack();
                return parseFunction(tokens, context);

            // Special case of unary minus on level of factor
            case OperatorType.SUBTRACTION:
                return -parseFactor(tokens, context);

            // Special case of unary plus on level of factor
            case OperatorType.ADDITION:
                return parseFactor(tokens, context);

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
                if (token.getType() instanceof ScalarOperatorType op) {
                    if (op.operands() == ScalarOperatorType.UNARY) {
                        return op.apply(List.of(parseFactor(tokens, context)));
                    }
                }

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
