package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.DoubleOperator;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.tokenize.token.*;
import ru.introguzzle.mathparser.tokenize.token.type.*;

import java.util.*;

public class PrefixTokenProcessor implements TokenProcessor {

    private final Map<String, Operator<?>> operators = new HashMap<>();

    public PrefixTokenProcessor(Map<String, ? extends Operator<?>> operators) {
        this.operators.putAll(operators);
    }

    @Override
    public Tokens process(Tokens tokens) throws UnknownOperatorException {
        Tokens output = new SimpleTokens();
        Stack<Token> operatorTokens = new Stack<>();

        for (Token token : tokens) {
            String string = token.getData();
            Type type = token.getType();

            if (type instanceof NumberType || type instanceof SymbolType) {
                output.add(token);
            } else if (type == ParenthesisType.LEFT) {
                operatorTokens.push(token);
            } else if (type == ParenthesisType.RIGHT) {
                while (!operatorTokens.isEmpty() && operatorTokens.peek().getType() != ParenthesisType.LEFT) {
                    output.add(operatorTokens.pop());
                }
                if (!operatorTokens.isEmpty() && operatorTokens.peek().getType() == ParenthesisType.LEFT) {
                    operatorTokens.pop();
                }
            } else if (type instanceof FunctionType || operators.containsKey(string)) {
                Operator<?> op = operators.get(string);

                if (op == null && type instanceof FunctionType) {
                    op = createDummy();
                    operators.put(string, op);
                }

                if (op == null) {
                    throw new UnknownOperatorException(string, tokens.toExpression(), tokens.getPosition());
                }

                while (!operatorTokens.isEmpty()
                        && operators.containsKey(operatorTokens.peek().getData())
                        && (
                        (op.getPriority() > operators.get(operatorTokens.peek().getData()).getPriority())
                                || (op.getPriority() == operators.get(operatorTokens.peek().getData()).getPriority() && !op.isRightAssociative())
                )) {
                    output.add(operatorTokens.pop());
                }

                operatorTokens.push(token);
            }
        }

        while (!operatorTokens.isEmpty()) {
            output.add(operatorTokens.pop());
        }

        return output;
    }

    private static @NotNull DoubleOperator createDummy() {
        return new DoubleOperator() {
            @Override
            public int getRequiredOperands() {
                return 0;
            }

            @Override
            public Double apply(List<Double> doubles) {
                return 0.0;
            }

            @Override
            public @NotNull String getName() {
                return "";
            }

            @Override
            public Association getAssociation() {
                return null;
            }

            @Override
            public int getPriority() {
                return 0;
            }
        };
    }
}
