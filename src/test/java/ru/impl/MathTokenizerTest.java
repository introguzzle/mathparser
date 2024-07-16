package ru.impl;

import org.junit.Before;
import org.junit.Test;
import ru.common.Context;
import ru.constant.Constant;
import ru.symbol.Coefficient;
import ru.tokenize.*;
import ru.expression.Expression;
import ru.expression.MathExpression;
import ru.function.AbstractFunction;
import ru.common.EvaluationContext;
import ru.symbol.Variable;

import java.util.List;

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
        Context context = new EvaluationContext();
        Tokens tokens = this.tokenizer.tokenize(expression, context);

        assertEquals(tokens.getPosition(), 0);
        assertEquals(8, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).getTokenType());
        assertEquals("2", tokens.get(0).getData());

        assertEquals(TokenType.OPERATOR_EXP, tokens.get(1).getTokenType());
        assertEquals("**", tokens.get(1).getData());

        assertEquals(TokenType.NUMBER, tokens.get(2).getTokenType());
        assertEquals("3", tokens.get(2).getData());

        assertEquals(TokenType.OPERATOR_MUL, tokens.get(3).getTokenType());
        assertEquals("*", tokens.get(3).getData());

        assertEquals(TokenType.LEFT_BRACKET, tokens.get(4).getTokenType());
        assertEquals("(", tokens.get(4).getData());

        assertEquals(TokenType.NUMBER, tokens.get(5).getTokenType());
        assertEquals("4.5", tokens.get(5).getData());

        assertEquals(TokenType.RIGHT_BRACKET, tokens.get(6).getTokenType());
        assertEquals(")", tokens.get(6).getData());

        assertEquals(TokenType.EOF, tokens.get(7).getTokenType());
    }

    @Test
    public void test_tokenize_simple_expression() throws Exception {
        Expression expression = new MathExpression("2 + 2");
        Context context = new EvaluationContext();
        Tokens tokens = tokenizer.tokenize(expression, context);

        assertEquals(tokens.getPosition(), 0);

        assertEquals(4, tokens.size());

        assertEquals(TokenType.NUMBER, tokens.get(0).getTokenType());
        assertEquals("2", tokens.get(0).getData());

        assertEquals(TokenType.OPERATOR_ADD, tokens.get(1).getTokenType());
        assertEquals("+", tokens.get(1).getData());

        assertEquals(TokenType.NUMBER, tokens.get(2).getTokenType());
        assertEquals("2", tokens.get(2).getData());

        assertEquals(TokenType.EOF, tokens.get(3).getTokenType());
    }

    @Test
    public void test_tokenize_expression_with_functions() throws Exception {
        Expression expression = new MathExpression("sin(1) + cos(1)");
        Context context = new EvaluationContext();
        Tokens tokens = tokenizer.tokenize(expression, context);

        assertEquals(tokens.getPosition(), 0);
        assertEquals(10, tokens.size());

        assertEquals(TokenType.FUNCTION_NAME, tokens.get(0).getTokenType());
        assertEquals("sin", tokens.get(0).getData());

        assertEquals(TokenType.LEFT_BRACKET, tokens.get(1).getTokenType());
        assertEquals("(", tokens.get(1).getData());

        assertEquals(TokenType.NUMBER, tokens.get(2).getTokenType());
        assertEquals("1", tokens.get(2).getData());

        assertEquals(TokenType.RIGHT_BRACKET, tokens.get(3).getTokenType());
        assertEquals(")", tokens.get(3).getData());

        assertEquals(TokenType.OPERATOR_ADD, tokens.get(4).getTokenType());
        assertEquals("+", tokens.get(4).getData());

        assertEquals(TokenType.FUNCTION_NAME, tokens.get(5).getTokenType());
        assertEquals("cos", tokens.get(5).getData());

        assertEquals(TokenType.LEFT_BRACKET, tokens.get(6).getTokenType());
        assertEquals("(", tokens.get(6).getData());

        assertEquals(TokenType.NUMBER, tokens.get(7).getTokenType());
        assertEquals("1", tokens.get(7).getData());

        assertEquals(TokenType.RIGHT_BRACKET, tokens.get(8).getTokenType());
        assertEquals(")", tokens.get(8).getData());

        assertEquals(TokenType.EOF, tokens.get(9).getTokenType());
    }

    @Test(expected = UnknownSymbolTokenizeException.class)
    public void test_tokenize_expression_unknown_variables() throws Exception {
        Expression expression = new MathExpression("x + y");
        Context context = new EvaluationContext();

        tokenizer.tokenize(expression, context);
    }

    @Test
    public void test_tokenize_expression_with_variables() throws Exception {
        Expression expression = new MathExpression("x + A");
        Context context = new EvaluationContext();
        context.getVariables().add("x", 3);
        context.getCoefficients().add("A", 4);

        Tokens tokens = tokenizer.tokenize(expression, context);

        assertEquals(4, tokens.size());

        assertEquals(TokenType.VARIABLE, tokens.get(0).getTokenType());
        assertEquals("x", tokens.get(0).getData());

        assertEquals(TokenType.OPERATOR_ADD, tokens.get(1).getTokenType());
        assertEquals("+", tokens.get(1).getData());

        assertEquals(TokenType.COEFFICIENT, tokens.get(2).getTokenType());
        assertEquals("A", tokens.get(2).getData());

        assertEquals(TokenType.EOF, tokens.get(3).getTokenType());

        assertEquals(tokens.getVariableCount(), 1);
        assertEquals(tokens.getCoefficientCount(), 1);
    }

    @Test(expected = TokenizeException.class)
    public void test_mismatched_brackets() throws Exception {
        Expression expression = new MathExpression("sin(1 + cos(2)");
        Context context = new EvaluationContext();
        tokenizer.tokenize(expression, context);
        fail("Expected TokenizeException due to mismatched brackets");
    }

    @Test(expected = TokenizeException.class)
    public void test_invalid_function() throws Exception {
        Expression expression = new MathExpression("xx(3, 4)");
        Context context = new EvaluationContext();
        tokenizer.tokenize(expression, context);
        fail("Expected TokenizeException due to invalid function name");
    }

    @Test
    public void test_multiple_arguments_in_functions() throws Exception {
        Expression expression = new MathExpression("sin(1, 3, 4)");
        Context context = new EvaluationContext();
        Tokens tokens = tokenizer.tokenize(expression, context);

        assertEquals(9, tokens.size());
        assertEquals(TokenType.FUNCTION_NAME, tokens.get(0).getTokenType());
        assertEquals("sin", tokens.get(0).getData());

        assertEquals(TokenType.LEFT_BRACKET, tokens.get(1).getTokenType());
        assertEquals("(", tokens.get(1).getData());

        assertEquals(TokenType.NUMBER, tokens.get(2).getTokenType());
        assertEquals("1", tokens.get(2).getData());

        assertEquals(TokenType.COMMA, tokens.get(3).getTokenType());
        assertEquals(",", tokens.get(3).getData());

        assertEquals(TokenType.NUMBER, tokens.get(4).getTokenType());
        assertEquals("3", tokens.get(4).getData());

        assertEquals(TokenType.COMMA, tokens.get(5).getTokenType());
        assertEquals(",", tokens.get(5).getData());

        assertEquals(TokenType.NUMBER, tokens.get(6).getTokenType());
        assertEquals("4", tokens.get(6).getData());

        assertEquals(TokenType.RIGHT_BRACKET, tokens.get(7).getTokenType());
        assertEquals(")", tokens.get(7).getData());

        assertEquals(TokenType.EOF, tokens.get(8).getTokenType());

    }

    @Test
    public void test_expression_reset() throws Exception {
        Expression expression = new MathExpression("2");
        Context context = new EvaluationContext();
        tokenizer.tokenize(expression, context);

        assertEquals(expression.getPosition(), 0);
    }

    @Test
    public void test_strip_delete_spaces() {
        Expression expression = new MathExpression(" 2 / 3 .4 5");
        assertEquals(expression.getString(), "2/3.45");
    }

    @Test
    public void test_last_operator() throws Exception {
        Expression expression = new MathExpression("2.344*");
        Context context = new EvaluationContext();
        Tokens tokens = tokenizer.tokenize(expression, context);

        assertEquals(3, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).getTokenType());
        assertEquals("2.344", tokens.get(0).getData());

        assertEquals(TokenType.OPERATOR_MUL, tokens.get(1).getTokenType());
        assertEquals("*", tokens.get(1).getData());

        assertEquals(TokenType.EOF, tokens.get(2).getTokenType());
    }

    @Test
    public void test_count() throws Exception {
        Expression expression = new MathExpression("pi + 3 + e + a + b / c ** d");
        Context context = new EvaluationContext();

        context.addVariable(new Variable("a", 0));
        context.addVariable(new Variable("b", 0));
        context.addVariable(new Variable("c", 0));
        context.addVariable(new Variable("d", 0));

        Tokens tokens = tokenizer.tokenize(expression, context);

        assertEquals(tokens.getConstantCount(), 2);
        assertEquals(tokens.getVariableCount(), 4);
    }

    @Test
    public void test_custom_function() throws Exception {
        tokenizer.addFunction(new AbstractFunction("test_function", 1) {
            @Override
            public Double apply(List<Double> arguments) {
                return 0.0;
            }

            @Override
            public boolean isVariadic() {
                return false;
            }
        });

        Expression expression = new MathExpression("test_function(3) ^ test_function(4)");
        Context context = new EvaluationContext();
        Tokens tokens = tokenizer.tokenize(expression, context);

        assertEquals(TokenType.FUNCTION_NAME, tokens.get(0).getTokenType());
        assertEquals("test_function", tokens.get(0).getData());
    }

    @Test
    public void test_binary_operators() throws Exception {
        Expression expression = new MathExpression("1 >> 2 | 3 & 4 << 5");
        Context context = new EvaluationContext();
        Tokens tokens = tokenizer.tokenize(expression, context);

        assertEquals(tokens.size(), 10);

        assertEquals(TokenType.NUMBER, tokens.get(0).getTokenType());
        assertEquals("1", tokens.get(0).getData());

        assertEquals(TokenType.OPERATOR_RIGHT_SHIFT, tokens.get(1).getTokenType());
        assertEquals(">>", tokens.get(1).getData());

        assertEquals(TokenType.NUMBER, tokens.get(2).getTokenType());
        assertEquals("2", tokens.get(2).getData());

        assertEquals(TokenType.OPERATOR_OR, tokens.get(3).getTokenType());
        assertEquals("|", tokens.get(3).getData());

        assertEquals(TokenType.NUMBER, tokens.get(4).getTokenType());
        assertEquals("3", tokens.get(4).getData());

        assertEquals(TokenType.OPERATOR_AND, tokens.get(5).getTokenType());
        assertEquals("&", tokens.get(5).getData());

        assertEquals(TokenType.NUMBER, tokens.get(6).getTokenType());
        assertEquals("4", tokens.get(6).getData());

        assertEquals(TokenType.OPERATOR_LEFT_SHIFT, tokens.get(7).getTokenType());
        assertEquals("<<", tokens.get(7).getData());

        assertEquals(TokenType.NUMBER, tokens.get(8).getTokenType());
        assertEquals("5", tokens.get(8).getData());

        assertEquals(TokenType.EOF, tokens.get(9).getTokenType());
    }

    @Test
    public void test_comparison_operators() throws Exception {
        Expression expression = new MathExpression("0 < 1 <= 2 >= 3 > 4");
        Context context = new EvaluationContext();
        Tokens tokens = tokenizer.tokenize(expression, context);

        assertEquals(tokens.size(), 10);

        assertEquals(TokenType.NUMBER, tokens.get(0).getTokenType());
        assertEquals("0", tokens.get(0).getData());

        assertEquals(TokenType.OPERATOR_LESS, tokens.get(1).getTokenType());
        assertEquals("<", tokens.get(1).getData());

        assertEquals(TokenType.NUMBER, tokens.get(2).getTokenType());
        assertEquals("1", tokens.get(2).getData());

        assertEquals(TokenType.OPERATOR_LESS_OR_EQUALS, tokens.get(3).getTokenType());
        assertEquals("<=", tokens.get(3).getData());

        assertEquals(TokenType.NUMBER, tokens.get(4).getTokenType());
        assertEquals("2", tokens.get(4).getData());

        assertEquals(TokenType.OPERATOR_GREATER_OR_EQUALS, tokens.get(5).getTokenType());
        assertEquals(">=", tokens.get(5).getData());

        assertEquals(TokenType.NUMBER, tokens.get(6).getTokenType());
        assertEquals("3", tokens.get(6).getData());

        assertEquals(TokenType.OPERATOR_GREATER, tokens.get(7).getTokenType());
        assertEquals(">", tokens.get(7).getData());

        assertEquals(TokenType.NUMBER, tokens.get(8).getTokenType());
        assertEquals("4", tokens.get(8).getData());

        assertEquals(TokenType.EOF, tokens.get(9).getTokenType());
    }

    @Test
    public void test_long_symbols() throws Exception {
        Expression expression = new MathExpression("sin(theta)");
        tokenizer.addConstant(new Constant("theta", 3) {});

        Context parent = new EvaluationContext();
        parent.addVariable(new Variable("theta", 3));
        parent.addCoefficient(new Coefficient("theta", 3));

        Context context = new EvaluationContext(parent);
        tokenizer.tokenize(expression, context);
    }

    @Test(expected = VariableMismatchException.class)
    public void test_long_symbols_with_strict_mode() throws Exception {
        Expression expression = new MathExpression("sin(theta)");
        tokenizer.addConstant(new Constant("theta", 3) {});
        tokenizer.getOptions().strictMode = true;

        Context parent = new EvaluationContext();
        parent.addVariable(new Variable("theta", 3));
        parent.addCoefficient(new Coefficient("theta", 3));

        Context context = new EvaluationContext(parent);
        tokenizer.tokenize(expression, context);
    }
}