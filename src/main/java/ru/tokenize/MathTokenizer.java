package ru.tokenize;

import ru.symbol.*;
import ru.common.Context;
import ru.constant.Constant;
import ru.constant.ConstantReflector;
import ru.expression.Expression;
import ru.function.Function;
import ru.function.FunctionReflector;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class MathTokenizer implements Tokenizer, Serializable {

    private TokenizerOptions options = new TokenizerOptions() {};

    @Override
    public void setOptions(TokenizerOptions options) {
        this.options = options;
    }

    @Override
    public TokenizerOptions getOptions() {
        return options;
    }

    private static class Carrier {
        Expression expression;
        Tokens tokens;
        Context context;

        Carrier(Expression expression, Tokens tokens, Context context) {
            this.expression = expression;
            this.tokens = tokens;
            this.context = context;
        }
    }

    @Serial
    private static final long serialVersionUID = -54943912839L;
    private transient Map<String, Function> functions = new HashMap<>();
    private List<ImmutableSymbol> constants = new ArrayList<>();

    public MathTokenizer() {
        this.functions = FunctionReflector.get();
        this.constants = ConstantReflector.get();
    }

    public MathTokenizer(Map<String, ? extends Function> functions) {
        this.functions.putAll(functions);
    }

    public MathTokenizer withFunctions(Map<String, ? extends Function> functions) {
        this.functions.putAll(functions);
        return this;
    }

    public MathTokenizer withConstants(Collection<? extends ImmutableSymbol> constants) {
        this.constants.addAll(constants);
        return this;
    }

    public MathTokenizer overrideFunction(String name, Function replace) {
        this.functions.replace(name, replace);
        return this;
    }

    public MathTokenizer overrideConstant(String representation, double value) {
        this.constants.removeIf(symbol -> symbol.getName().equals(representation));
        this.constants.add(new Constant(representation, value) {});
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

    public MathTokenizer clearFunctions() {
        this.functions.clear();
        return this;
    }

    public MathTokenizer clearConstants() {
        this.constants.clear();
        return this;
    }

    @Override
    public synchronized Tokens tokenize(Expression expression, Context context) throws TokenizeException {
        Stack<Character> bracketStack = new Stack<>();
        Tokens tokens = new Tokens();
        Carrier carrier = new Carrier(expression, tokens, context);

        while (expression.hasNext()) {
            char current = expression.current();
            switch (current) {
                case ' ':
                    expression.next();
                    continue;
                case '(':
                    tokens.add(TokenType.LEFT_BRACKET, current);
                    bracketStack.push(current);
                    expression.next();
                    continue;
                case ')':
                    tokens.add(TokenType.RIGHT_BRACKET, current);
                    if (bracketStack.isEmpty()) {
                        throw new IllegalBracketStartException();
                    } else {
                        bracketStack.pop();
                    }

                    expression.next();
                    continue;
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                case '>':
                case '<':
                case '=':
                case '!':
                case '&':
                case '|':
                case '~':
                    handleOperator(carrier);
                    expression.next();
                    continue;
                case ',':
                    tokens.add(TokenType.COMMA, current);
                    expression.next();
                    continue;
                default:
                    if (expression.isCurrentDigit() || current == '.') {
                        handleNumber(carrier);
                        continue;
                    }

                    if (expression.isCurrentLetter()) {
                        handleSymbols(carrier);
                        continue;
                    }

                    throw new TokenizeException("Unexpected character: '" + current + "' at pos " + expression.getPosition() + " in expression");

            }
        }

        if (!bracketStack.isEmpty()) {
            throw new BracketMismatchException();
        }

        tokens.add(TokenType.EOF, "");
        expression.reset();

        if (options.strictMode) {
            int size;
            if ((size = context.getVariables().size()) != tokens.getVariableCount()) {
                throw new VariableMismatchException(size, tokens.getVariableCount());
            }

            if ((size = context.getCoefficients().size()) != tokens.getCoefficientCount()) {
                throw new CoefficientMismatchException(size, tokens.getCoefficientCount());
            }
        }

        return tokens;
    }

    @Override
    public List<ImmutableSymbol> getConstants() {
        return this.constants;
    }

    @Override
    public Map<String, Function> getFunctions() {
        return this.functions;
    }

    private void handleNumber(Carrier carrier) {
        StringBuilder number = new StringBuilder();

        Expression expression = carrier.expression;
        char current = expression.current();

        while (expression.hasNext() && expression.isCurrentDigit() || current == '.') {
            number.append(current);
            expression.next();
            if (!expression.hasNext()) {
                break;
            }

            current = expression.current();
        }

        carrier.tokens.add(TokenType.NUMBER, number.toString());
    }

    private void handleOperator(Carrier carrier) {
        Expression expression = carrier.expression;
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
            case '^' -> new Token(TokenType.OPERATOR_XOR, current);
            case '&' -> new Token(TokenType.OPERATOR_AND, current);
            case '|' -> new Token(TokenType.OPERATOR_OR, current);
            case '~' -> new Token(TokenType.OPERATOR_BITWISE_NOT, current);
            case '<' -> {
                if (expression.getPosition() + 1 < expression.getLength() && expression.peekNext() == '<') {
                    expression.next();
                    yield new Token(TokenType.OPERATOR_LEFT_SHIFT, "<<");
                } else if (expression.peekNext() == '=') {
                    expression.next();
                    yield new Token(TokenType.OPERATOR_LESS_OR_EQUALS, "<=");
                }

                yield new Token(TokenType.OPERATOR_LESS, current);
            }
            case '>' -> {
                if (expression.getPosition() + 1 < expression.getLength() && expression.peekNext() == '>') {
                    expression.next();
                    yield new Token(TokenType.OPERATOR_RIGHT_SHIFT, ">>");
                } else if (expression.peekNext() == '=') {
                    expression.next();
                    yield new Token(TokenType.OPERATOR_GREATER_OR_EQUALS, ">=");
                }

                yield new Token(TokenType.OPERATOR_GREATER, current);
            }
            case '=' -> {
                if (expression.getPosition() + 1 < expression.getLength() && expression.peekNext() == '=') {
                    expression.next();
                    yield new Token(TokenType.OPERATOR_EQUALS, "==");
                }

                throw new IllegalArgumentException("Invalid operator: " + current);
            }
            case '!' -> {
                if (expression.getPosition() + 1 < expression.getLength() && expression.peekNext() == '=') {
                    expression.next();
                    yield new Token(TokenType.OPERATOR_NOT_EQUALS, "!=");
                }

                yield new Token(TokenType.OPERATOR_NOT, "!");
            }

            default -> throw new IllegalArgumentException("Unknown operator: " + current);
        };

        carrier.tokens.add(token);
    }

    private void handleSymbols(Carrier carrier) throws TokenizeException {
        Expression expression = carrier.expression;
        boolean match;

        StringBuilder sequence = new StringBuilder();

        while (expression.hasNext() && expression.isCurrentLetter()) {
            sequence.append(expression.next());
            if (!expression.hasNext()) {
                break;
            }
        }

        match = handleFunction(carrier, sequence)
                || handleConstant(carrier, sequence)
                || handleMutableList(carrier, sequence);

        if (!match) {
            throw new UnknownSymbolTokenizeException(sequence);
        }
    }

    private boolean handleFunction(Carrier carrier, CharSequence sequence) {
        AtomicBoolean match = new AtomicBoolean(false);
        this.functions.keySet().stream()
                .filter(s -> s.contentEquals(sequence))
                .findFirst()
                .ifPresent(s -> {
                    carrier.tokens.add(TokenType.FUNCTION_NAME, sequence);
                    match.set(true);
                });

        return match.get();
    }

    private boolean handleConstant(Carrier carrier, CharSequence sequence) {
        AtomicBoolean match = new AtomicBoolean(false);
        Predicate<ImmutableSymbol> isConstant = s -> s instanceof Constant;
        this.constants
                .stream()
                .parallel()
                .filter(isConstant.and(Symbol.match(sequence)))
                .findFirst()
                .ifPresent(s -> {
                    carrier.tokens.add(TokenType.CONSTANT, sequence);
                    match.set(true);
                });

        return match.get();
    }

    private boolean handleMutableList(Carrier carrier, CharSequence sequence) {
        AtomicBoolean match = new AtomicBoolean(false);
        carrier.context.getVariables()
                .stream()
                .parallel()
                .filter(s -> s.getName().contentEquals(sequence))
                .findFirst()
                .ifPresent(s -> {
                    carrier.tokens.add(TokenType.VARIABLE, sequence);
                    match.set(true);
                });

        if (match.get()) {
            return true;
        }

        carrier.context.getCoefficients()
                .stream()
                .parallel()
                .filter(s -> s.getName().contentEquals(sequence))
                .findFirst()
                .ifPresent(s -> {
                    carrier.tokens.add(TokenType.COEFFICIENT, sequence);
                    match.set(true);
                });

        return match.get();
    }
}
