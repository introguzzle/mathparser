package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;

import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.symbol.MutableSymbol;
import ru.introguzzle.mathparser.tokenize.*;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.Tokens;
import ru.introguzzle.mathparser.tokenize.token.type.*;

import java.util.*;

public class PrefixNotationParser implements Parser<Double> {
    private final Tokenizer tokenizer;
    private final TokenProcessor processor;

    public PrefixNotationParser(@NotNull Tokenizer tokenizer) {
        this(tokenizer, new PrefixTokenProcessor());
    }

    public PrefixNotationParser(@NotNull Tokenizer tokenizer, @NotNull TokenProcessor processor) {
        this.tokenizer = tokenizer;
        this.processor = processor;
    }

    @Override
    public Double parse(@NotNull Expression expression) throws SyntaxException {
        return parse(expression, new NamingContext());
    }

    @Override
    public Double parse(@NotNull Expression expression, @NotNull Context context) throws SyntaxException {
        Tokens tokens = tokenizer.tokenize(expression, context);
        if (tokens.get(0).getType().isTerminal()) {
            return Double.NaN;
        }

        return parse(tokens, context);
    }

    @Override
    public Double parse(@NotNull Tokens tokens, Context context) throws SyntaxException {
        Tokens infix = processor.process(tokens);
        Stack<Double> stack = new Stack<>();
        infix.skipDeclaration();

        int position = 0;
        while (position < infix.size()) {
            Token token = infix.getNextToken();
            Type type = token.getType();

            if (type instanceof OperatorType operator) {
                processOperator(stack, operator, tokens, position);
                position++;
                continue;
            }

            if (type instanceof NumberType) {
                stack.push(Double.parseDouble(token.getData()));
                position++;
                continue;
            }

            if (type instanceof FunctionType) {
                Function function = tokenizer.findFunction(token).orElseThrow();

                if (function.isVariadic()) {
                    throw new RuntimeException("Variadic functions are not supported in this parser. Try MathParser instead.");
                }

                processOperator(stack, function.toOperator(), tokens, position);
                position++;
                continue;
            }

            if (type instanceof SymbolType) {
                switch (type) {
                    case SymbolType.CONSTANT -> {
                        Optional<ImmutableSymbol> symbol = tokenizer.findConstant(token);
                        stack.push(symbol.orElseThrow().getValue());
                    }

                    case SymbolType.VARIABLE, SymbolType.COEFFICIENT -> {
                        MutableSymbol mutableSymbol = context.getSymbol(token.getData()).orElseThrow();
                        stack.push(mutableSymbol.getValue());
                    }

                    default -> throw new UnexpectedTokenException(tokens, position);
                }

                position++;
                continue;
            }

            position++;
        }

        if (stack.size() != 1) {
            throw new UnexpectedTokenException(tokens, position);
        }

        return stack.pop();
    }

    private void processOperator(Stack<Double> stack,
                                 Operator<Double> operator,
                                 Tokens tokens,
                                 int position) throws SyntaxException {
        int operandCount = operator.operands();
        if (operator == OperatorType.SUBTRACTION && stack.size() == 1) {
            // Handle special case of unary minus
            // We can't define enum for this operation for various reasons,
            // mostly because unary and binary minuses have same symbol representation

            stack.push(-stack.pop());
            return;

        } else if (stack.size() < operandCount) {
            throw new UnexpectedTokenException(tokens, position);
        }

        Double[] operands = new Double[operandCount];
        for (int i = operandCount - 1; i >= 0; i--) {
            operands[i] = stack.pop();
        }

        double result = operator.apply(List.of(operands));
        stack.push(result);
    }
}
