package ru.impl;

import org.junit.Before;
import org.junit.Test;
import ru.exceptions.TokenizeException;
import ru.contract.Expression;
import ru.function.AbstractFunction;
import ru.tokens.TokenType;
import ru.tokens.Tokens;

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
        Tokens tokens = this.tokenizer.tokenize(expression);

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
        Tokens tokens = tokenizer.tokenize(expression);

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
        Tokens tokens = tokenizer.tokenize(expression);

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

    @Test
    public void test_tokenize_expression_with_variables() throws Exception {
        Expression expression = new MathExpression("x + y");
        Tokens tokens = tokenizer.tokenize(expression);

        assertEquals(4, tokens.size());

        assertEquals(TokenType.VARIABLE, tokens.get(0).getTokenType());
        assertEquals("x", tokens.get(0).getData());

        assertEquals(TokenType.OPERATOR_ADD, tokens.get(1).getTokenType());
        assertEquals("+", tokens.get(1).getData());

        assertEquals(TokenType.VARIABLE, tokens.get(2).getTokenType());
        assertEquals("y", tokens.get(2).getData());

        assertEquals(TokenType.EOF, tokens.get(3).getTokenType());
    }

    @Test(expected = TokenizeException.class)
    public void test_mismatched_brackets() throws Exception {
        Expression expression = new MathExpression("sin(1 + cos(2)");
        tokenizer.tokenize(expression);
        fail("Expected TokenizeException due to mismatched brackets");
    }

    @Test(expected = TokenizeException.class)
    public void test_invalid_function() throws Exception {
        Expression expression = new MathExpression("xx(3, 4)");
        tokenizer.tokenize(expression);
        fail("Expected TokenizeException due to invalid function name");
    }

    @Test
    public void test_multiple_arguments_in_functions() throws Exception {
        Expression expression = new MathExpression("sin(1, 3, 4)");
        Tokens tokens = tokenizer.tokenize(expression);

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
        tokenizer.tokenize(expression);

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

        Tokens tokens = tokenizer.tokenize(expression);

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
        Tokens tokens = tokenizer.tokenize(expression);

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
        Tokens tokens = tokenizer.tokenize(expression);

        assertEquals(TokenType.FUNCTION_NAME, tokens.get(0).getTokenType());
        assertEquals("test_function", tokens.get(0).getData());
    }

    @Test
    public void test_binary_operators() throws Exception {
        Expression expression = new MathExpression("1 >> 2 | 3 & 4 << 5");

        Tokens tokens = tokenizer.tokenize(expression);

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

        Tokens tokens = tokenizer.tokenize(expression);

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
}