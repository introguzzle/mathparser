package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.tokenize.token.*;
import ru.introguzzle.mathparser.tokenize.token.type.*;

import java.util.*;
import java.util.stream.Collectors;

public class PrefixTokenProcessor implements TokenProcessor {

    private static final Map<String, Priorable> OPERATORS =
            Arrays.stream(OperatorType.values())
                    .collect(Collectors.toMap(OperatorType::getRepresentation, op -> op));

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
            } else if (type instanceof FunctionType || OPERATORS.containsKey(string)) {
                Priorable p = OPERATORS.get(string);

                if (p == null && type instanceof FunctionType functionType) {
                    p = new Priorable() {
                        @Override
                        public int getPriority() {
                            return functionType.getPriority();
                        }

                        @Override
                        public Association getAssociation() {
                            return functionType.getAssociation();
                        }
                    };

                    OPERATORS.put(string, p);
                }

                if (p == null) {
                    throw new UnknownOperatorException(string, tokens.toExpression(), tokens.getPosition());
                }

                while (!operatorTokens.isEmpty()
                        && OPERATORS.containsKey(operatorTokens.peek().getData())
                        && (
                        (p.getPriority() > OPERATORS.get(operatorTokens.peek().getData()).getPriority())
                                || (p.getPriority() == OPERATORS.get(operatorTokens.peek().getData()).getPriority() && !p.isRightAssociative())
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
}
