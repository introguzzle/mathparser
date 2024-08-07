package ru.impl;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.NamingException;
import ru.introguzzle.mathparser.constant.real.DoubleConstant;
import ru.introguzzle.mathparser.constant.real.DoubleConstantReflector;
import ru.introguzzle.mathparser.function.real.DoubleFunction;
import ru.introguzzle.mathparser.function.real.DoubleFunctionReflector;
import ru.introguzzle.mathparser.operator.standard.AdditionOperator;
import ru.introguzzle.mathparser.tokenize.token.type.OperatorType;
import ru.introguzzle.mathparser.symbol.Coefficient;
import ru.introguzzle.mathparser.tokenize.*;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.tokenize.token.Tokens;
import ru.introguzzle.mathparser.tokenize.token.type.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MathTokenizerTest {

    private MathTokenizer tokenizer;

    @Before
    public void setUp() {
        this.tokenizer = new MathTokenizer();
    }

    @Test
    public void test_tokenize_exponentiation() throws Exception {
        Expression expression = new MathExpression("2 ** 3 * (4.5)");
        Context<Double> context = new NamingContext<>();
        Tokens tokens = this.tokenizer.tokenize(expression, context).getTokens();

        assertEquals(tokens.getPosition(), 0);
        assertEquals(8, tokens.size());
        assertEquals(NumberType.NUMBER, tokens.get(0).getType());
        assertEquals("2", tokens.get(0).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(1).getType());
        assertEquals("**", tokens.get(1).getData());

        assertEquals(NumberType.NUMBER, tokens.get(2).getType());
        assertEquals("3", tokens.get(2).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(3).getType());
        assertEquals("*", tokens.get(3).getData());

        assertEquals(ParenthesisType.LEFT, tokens.get(4).getType());
        assertEquals("(", tokens.get(4).getData());

        assertEquals(NumberType.NUMBER, tokens.get(5).getType());
        assertEquals("4.5", tokens.get(5).getData());

        assertEquals(ParenthesisType.RIGHT, tokens.get(6).getType());
        assertEquals(")", tokens.get(6).getData());

        assertEquals(TerminalType.TERMINAL, tokens.get(7).getType());
    }

    @Test
    public void test_tokenize_simple_expression() throws Exception {
        Expression expression = new MathExpression("2 + 2");
        Context<Double> context = new NamingContext<>();
        Tokens tokens = tokenizer.tokenize(expression, context).getTokens();

        assertEquals(tokens.getPosition(), 0);

        assertEquals(4, tokens.size());

        assertEquals(NumberType.NUMBER, tokens.get(0).getType());
        assertEquals("2", tokens.get(0).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(1).getType());
        assertEquals("+", tokens.get(1).getData());

        assertEquals(NumberType.NUMBER, tokens.get(2).getType());
        assertEquals("2", tokens.get(2).getData());

        assertEquals(TerminalType.TERMINAL, tokens.get(3).getType());
    }

    @Test
    public void test_tokenize_expression_with_functions() throws Exception {
        Expression expression = new MathExpression("sin(1) + cos(1)");
        Context<Double> context = new NamingContext<>();
        Tokens tokens = tokenizer.tokenize(expression, context).getTokens();

        assertEquals(tokens.getPosition(), 0);
        assertEquals(10, tokens.size());

        assertEquals(FunctionType.FUNCTION, tokens.get(0).getType());
        assertEquals("sin", tokens.get(0).getData());

        assertEquals(ParenthesisType.LEFT, tokens.get(1).getType());
        assertEquals("(", tokens.get(1).getData());

        assertEquals(NumberType.NUMBER, tokens.get(2).getType());
        assertEquals("1", tokens.get(2).getData());

        assertEquals(ParenthesisType.RIGHT, tokens.get(3).getType());
        assertEquals(")", tokens.get(3).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(4).getType());
        assertEquals("+", tokens.get(4).getData());

        assertEquals(FunctionType.FUNCTION, tokens.get(5).getType());
        assertEquals("cos", tokens.get(5).getData());

        assertEquals(ParenthesisType.LEFT, tokens.get(6).getType());
        assertEquals("(", tokens.get(6).getData());

        assertEquals(NumberType.NUMBER, tokens.get(7).getType());
        assertEquals("1", tokens.get(7).getData());

        assertEquals(ParenthesisType.RIGHT, tokens.get(8).getType());
        assertEquals(")", tokens.get(8).getData());

        assertEquals(TerminalType.TERMINAL, tokens.get(9).getType());
    }

    @Test(expected = UnknownSymbolTokenizeException.class)
    public void test_tokenize_expression_unknown_variables() throws Exception {
        Expression expression = new MathExpression("x + y");
        Context<Double> context = new NamingContext<>();

        tokenizer.tokenize(expression, context);
    }

    @Test
    public void test_tokenize_expression_with_variables() throws Exception {
        Expression expression = new MathExpression("x + A");
        Context<Double> context = new NamingContext<>();
        context.getSymbols().add(new Variable<>("x", 4.0));
        context.getSymbols().add(new Coefficient<>("A", 4.0));

        Tokens tokens = tokenizer.tokenize(expression, context).getTokens();

        assertEquals(4, tokens.size());

        assertEquals(SymbolType.VARIABLE, tokens.get(0).getType());
        assertEquals("x", tokens.get(0).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(1).getType());
        assertEquals("+", tokens.get(1).getData());

        assertEquals(SymbolType.COEFFICIENT, tokens.get(2).getType());
        assertEquals("A", tokens.get(2).getData());

        assertEquals(TerminalType.TERMINAL, tokens.get(3).getType());

        assertEquals(tokens.getVariableCount(), 1);
        assertEquals(tokens.getCoefficientCount(), 1);
    }

    @Test(expected = TokenizeException.class)
    public void test_mismatched_brackets() throws Exception {
        Expression expression = new MathExpression("sin(1 + cos(2)");
        Context<Double> context = new NamingContext<>();
        tokenizer.tokenize(expression, context);
        fail("Expected TokenizeException due to mismatched brackets");
    }

    @Test(expected = TokenizeException.class)
    public void test_invalid_function() throws Exception {
        Expression expression = new MathExpression("xx(3, 4)");
        Context<Double> context = new NamingContext<>();
        tokenizer.tokenize(expression, context);
        fail("Expected TokenizeException due to invalid function name");
    }

    @Test
    public void test_multiple_arguments_in_functions() throws Exception {
        Expression expression = new MathExpression("sin(1, 3, 4)");
        Context<Double> context = new NamingContext<>();
        Tokens tokens = tokenizer.tokenize(expression, context).getTokens();

        assertEquals(9, tokens.size());
        assertEquals(FunctionType.FUNCTION, tokens.get(0).getType());
        assertEquals("sin", tokens.get(0).getData());

        assertEquals(ParenthesisType.LEFT, tokens.get(1).getType());
        assertEquals("(", tokens.get(1).getData());

        assertEquals(NumberType.NUMBER, tokens.get(2).getType());
        assertEquals("1", tokens.get(2).getData());

        assertEquals(SpecialType.COMMA, tokens.get(3).getType());
        assertEquals(",", tokens.get(3).getData());

        assertEquals(NumberType.NUMBER, tokens.get(4).getType());
        assertEquals("3", tokens.get(4).getData());

        assertEquals(SpecialType.COMMA, tokens.get(5).getType());
        assertEquals(",", tokens.get(5).getData());

        assertEquals(NumberType.NUMBER, tokens.get(6).getType());
        assertEquals("4", tokens.get(6).getData());

        assertEquals(ParenthesisType.RIGHT, tokens.get(7).getType());
        assertEquals(")", tokens.get(7).getData());

        assertEquals(TerminalType.TERMINAL, tokens.get(8).getType());

    }

    @Test
    public void test_expression_reset() throws Exception {
        Expression expression = new MathExpression("2");
        Context<Double> context = new NamingContext<>();
        tokenizer.tokenize(expression, context);
    }

    @Test
    public void test_last_operator() throws Exception {
        Expression expression = new MathExpression("2.344*");
        Context<Double> context = new NamingContext<>();
        Tokens tokens = tokenizer.tokenize(expression, context).getTokens();

        assertEquals(3, tokens.size());
        assertEquals(NumberType.NUMBER, tokens.get(0).getType());
        assertEquals("2.344", tokens.get(0).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(1).getType());
        assertEquals("*", tokens.get(1).getData());

        assertEquals(TerminalType.TERMINAL, tokens.get(2).getType());
    }

    @Test
    public void test_count() throws Exception {
        Expression expression = new MathExpression("pi + 3 + e + a + b / c ** d");
        Context<Double> context = new NamingContext<>();

        context.addSymbol(new Variable<>("a", 0.0));
        context.addSymbol(new Variable<>("b", 0.0));
        context.addSymbol(new Variable<>("c", 0.0));
        context.addSymbol(new Variable<>("d", 0.0));

        Tokens tokens = tokenizer.tokenize(expression, context).getTokens();

        assertEquals(tokens.getConstantCount(), 2);
        assertEquals(tokens.getVariableCount(), 4);
    }

    @Test
    public void test_custom_function() throws Exception {
        tokenizer.getOptions().addFunction(new DoubleFunction("test_function", 1) {
            @Override
            public @NotNull Double apply(List<Double> arguments) {
                return 0.0;
            }

            @Override
            public boolean isVariadic() {
                return false;
            }
        }).addFunction(new DoubleFunction("clown", 0) {
            @Override
            public @NotNull Double apply(List<Double> arguments) {
                return ThreadLocalRandom.current().nextDouble();
            }

            @Override
            public boolean isVariadic() {
                return false;
            }
        });

        Expression expression = new MathExpression("test_function(3) ^ test_function(clown()) * clown()");
        Context<Double> context = new NamingContext<>();
        Tokens tokens = tokenizer.tokenize(expression, context).getTokens();

        assertEquals(FunctionType.FUNCTION, tokens.get(0).getType());
        assertEquals("test_function", tokens.get(0).getData());
    }

    @Test
    public void test_custom_constants_and_functions() throws Exception {
        MathTokenizer tokenizer = new MathTokenizer(
                Map.of("zzzz", new DoubleFunction("zzzz", 1) {
                    @Override
                    public @NotNull Double apply(List<Double> arguments) {
                        return null;
                    }

                    @Override
                    public boolean isVariadic() {
                        return false;
                    }
                })
        );

        tokenizer.tokenize(new MathExpression("zzzz(1)"), new NamingContext<>());
    }

    @Test
    public void test_custom_constants() throws Exception {
        tokenizer.getOptions().addConstant(new DoubleConstant("A", 1) {})
                .addConstant(new DoubleConstant("B", 3) {});

        tokenizer.tokenize(new MathExpression("A + B"), new NamingContext<>());
        tokenizer.getOptions().clearConstants()
                .clearFunctions()
                .withConstants(DoubleConstantReflector.get().values())
                .withFunctions(DoubleFunctionReflector.get().values());

        tokenizer.getOptions().overrideConstant("PI", 3)
                .overrideConstant("PI", 4)
                .overrideFunction("sin", 2, false, args -> 0.0)
                .overrideConstant("PI", 5);

        tokenizer.tokenize(new MathExpression("PI + PI"), new NamingContext<>());
    }

    @Test
    public void test_binary_operators() throws Exception {
        Expression expression = new MathExpression("1 >> 2 | 3 & 4 << 5");
        Context<Double> context = new NamingContext<>();
        Tokens tokens = tokenizer.tokenize(expression, context).getTokens();
        assertEquals(tokens.size(), 10);

        assertEquals(NumberType.NUMBER, tokens.get(0).getType());
        assertEquals("1", tokens.get(0).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(1).getType());
        assertEquals(">>", tokens.get(1).getData());

        assertEquals(NumberType.NUMBER, tokens.get(2).getType());
        assertEquals("2", tokens.get(2).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(3).getType());
        assertEquals("|", tokens.get(3).getData());

        assertEquals(NumberType.NUMBER, tokens.get(4).getType());
        assertEquals("3", tokens.get(4).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(5).getType());
        assertEquals("&", tokens.get(5).getData());

        assertEquals(NumberType.NUMBER, tokens.get(6).getType());
        assertEquals("4", tokens.get(6).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(7).getType());
        assertEquals("<<", tokens.get(7).getData());

        assertEquals(NumberType.NUMBER, tokens.get(8).getType());
        assertEquals("5", tokens.get(8).getData());

        assertEquals(TerminalType.TERMINAL, tokens.get(9).getType());
    }

    @Test
    public void supress() {
        MathTokenizer t = new MathTokenizer();
        t.getOptions().setDigitPredicate(null)
                .setAllowedOperatorSymbols(null)
                .setLetterPredicate(null)
                .setDigitPredicate(null)
                .withFunctions(List.of())
                .withOperators(List.of())
                .overrideOperator("+", new AdditionOperator())
                .addName(new Variable<>("x", 3.0))
                .addOperator(new AdditionOperator())
                .clearConstants()
                .clearOperators();
    }

    @Test
    public void test_comparison_operators() throws Exception {
        Expression expression = new MathExpression("0 < 1 <= 2 >= 3 > 4");
        Context<Double> context = new NamingContext<>();
        Tokens tokens = tokenizer.tokenize(expression, context).getTokens();

        assertEquals(tokens.size(), 10);

        assertEquals(NumberType.NUMBER, tokens.get(0).getType());
        assertEquals("0", tokens.get(0).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(1).getType());
        assertEquals("<", tokens.get(1).getData());

        assertEquals(NumberType.NUMBER, tokens.get(2).getType());
        assertEquals("1", tokens.get(2).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(3).getType());
        assertEquals("<=", tokens.get(3).getData());

        assertEquals(NumberType.NUMBER, tokens.get(4).getType());
        assertEquals("2", tokens.get(4).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(5).getType());
        assertEquals(">=", tokens.get(5).getData());

        assertEquals(NumberType.NUMBER, tokens.get(6).getType());
        assertEquals("3", tokens.get(6).getData());

        assertEquals(OperatorType.OPERATOR, tokens.get(7).getType());
        assertEquals(">", tokens.get(7).getData());

        assertEquals(NumberType.NUMBER, tokens.get(8).getType());
        assertEquals("4", tokens.get(8).getData());

        assertEquals(TerminalType.TERMINAL, tokens.get(9).getType());
    }

    @Test
    public void test_long_symbols() throws Exception {
        Expression expression = new MathExpression("sin(theta)");
        tokenizer.getOptions().addConstant(new DoubleConstant("theta", 3) {});

        Context<Double> parent = new NamingContext<>();

        Context<Double> context = new NamingContext<>(parent);
        tokenizer.tokenize(expression, context);
    }

    @Test(expected = NamingException.class)
    public void test_long_symbols_with_strict_mode() throws Exception {
        Expression expression = new MathExpression("sin(theta)");
        tokenizer.getOptions().addConstant(new DoubleConstant("theta", 3) {});
        tokenizer.getOptions().strictMode = true;

        Context<Double> parent = new NamingContext<>();
        parent.getSymbols().add(new Variable<>("theta", 3.0));
        parent.getSymbols().add(new Coefficient<>("theta", 3.0));

        Context<Double> context = new NamingContext<>(parent);
        tokenizer.tokenize(expression, context);
    }
}