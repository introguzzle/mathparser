package ru.impl;

import ru.constant.Constant;
import ru.constant.Constants;
import ru.exceptions.TokenizeException;
import ru.function.Function;
import ru.function.Functions;
import ru.contract.*;
import ru.tokens.Token;
import ru.tokens.TokenType;
import ru.tokens.Tokens;

import java.util.*;

public class MathTokenizer implements Tokenizer {

    private final Tokens currentTokens = new Tokens();
    private Map<String, Function> functions = new HashMap<>();
    private List<Symbol> constants = new ArrayList<>();

    public MathTokenizer() {
        this.functions = Functions.get();
        this.constants = Constants.get();
    }

    public MathTokenizer(Map<String, ? extends Function> functions) {
        this.functions.putAll(functions);
    }

    public MathTokenizer withFunctions(Map<String, ? extends Function> functions) {
        this.functions.putAll(functions);
        return this;
    }

    public MathTokenizer withConstants(Collection<? extends Symbol> constants) {
        this.constants.addAll(constants);
        return this;
    }

    public MathTokenizer overrideFunction(String name, Function replace) {
        this.functions.replace(name, replace);
        return this;
    }

    public MathTokenizer addFunction(Function function) {
        this.functions.put(function.getName(), function);
        return this;
    }

    public MathTokenizer addConstant(Constant constant) {
        this.constants.add(constant);
        return this;
    }

    @Override
    public synchronized Tokens tokenize(Expression expression) throws TokenizeException {
        Stack<Character> bracketStack = new Stack<>();

        while (expression.hasNext()) {
            char current = expression.current();
            switch (current) {
                case '(':
                    currentTokens.add(TokenType.LEFT_BRACKET, current);
                    bracketStack.push(current);
                    expression.next();
                    continue;
                case ')':
                    currentTokens.add(TokenType.RIGHT_BRACKET, current);
                    if (bracketStack.isEmpty())
                        throw new TokenizeException("Illegal brackets");
                    else
                        bracketStack.pop();
                    expression.next();
                    continue;
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                    handleOperator(expression);
                    expression.next();
                    continue;
                case ',':
                    currentTokens.add(TokenType.COMMA, current);
                    expression.next();
                    continue;
                default:
                    if (expression.isCurrentDigit() || current == '.') {
                        handleNumber(expression);
                        continue;
                    }

                    if (expression.isCurrentLetter()) {
                        handleSymbols(expression);
                        continue;
                    }

                    if (current != ' ') {
                        throw new TokenizeException("Unexpected character: '" + current + "' at pos " + expression.getPosition() + " in expression");
                    }

                    expression.next();
            }
        }

        if (!bracketStack.isEmpty()) {
            throw new TokenizeException("Bracket count don't match");
        }

        currentTokens.add(TokenType.EOF, "");

        Tokens out = new Tokens(new ArrayList<>(this.currentTokens.getTokens()));
        out.setConstantCount(currentTokens.getConstantCount());
        out.setVariableCount(currentTokens.getVariableCount());

        currentTokens.reset();
        currentTokens.clear();

        expression.reset();

        return out;
    }

    @Override
    public List<Symbol> getConstants() {
        return this.constants;
    }

    @Override
    public Map<String, Function> getFunctions() {
        return this.functions;
    }

    private void handleNumber(Expression expression) {
        StringBuilder number = new StringBuilder();
        char current = expression.current();

        while (expression.hasNext() && expression.isCurrentDigit() || current == '.') {
            number.append(current);
            expression.next();
            if (!expression.hasNext()) {
                break;
            }

            current = expression.current();
        }

        currentTokens.add(TokenType.NUMBER, number.toString());
    }

    private void handleOperator(Expression expression) {
        char current = expression.current();

        Token token = switch (current) {
            case '+' -> new Token(TokenType.OPERATOR_ADD, current);
            case '-' -> new Token(TokenType.OPERATOR_SUB, current);
            case '*' -> {
                if (expression.getPosition() + 1 < expression.getLength() && expression.peekNext() == '*') {
                    expression.next();
                    yield new Token(TokenType.OPERATOR_EXP, "**");
                }

                yield new Token(TokenType.OPERATOR_MUL, current);
            }
            case '/' -> new Token(TokenType.OPERATOR_DIV, current);
            case '^' -> new Token(TokenType.OPERATOR_EXP, current);
            default -> throw new IllegalArgumentException("Unknown operator: " + current);
        };

        currentTokens.add(token);
    }

    private void handleSymbols(Expression expression) throws TokenizeException {
        char current = expression.current();

        for (Symbol symbol: this.constants) {
            if (symbol instanceof Constant constant) {
                if (constant.getRepresentation().equals(Character.toString(current))) {
                    currentTokens.add(TokenType.CONSTANT, current);
                    currentTokens.incrementConstantCount();
                    expression.next();
                    return;
                }
            }
        }

        if (!expression.isNextLetter()) {
            currentTokens.add(TokenType.VARIABLE, current);
            currentTokens.incrementVariableCount();
            expression.next();
            return;
        }

        StringBuilder symbols = new StringBuilder();

        while (expression.hasNext() && expression.isCurrentLetter()) {
            symbols.append(expression.next());
            if (!expression.hasNext()) {
                break;
            }
        }

        boolean match = false;

        if (functions.containsKey(symbols.toString())) {
            currentTokens.add(TokenType.FUNCTION_NAME, symbols);
            match = true;
        }

        for (Symbol symbol: this.constants) {
            if (symbol instanceof Constant constant) {
               if (constant.getRepresentation().contentEquals(symbols)) {
                   currentTokens.add(TokenType.CONSTANT, symbols);
                   currentTokens.incrementConstantCount();
                   match = true;
               }
            }
        }

        if (!match) {
            throw new TokenizeException("Unexpected function: " + symbols);
        }
    }
}
