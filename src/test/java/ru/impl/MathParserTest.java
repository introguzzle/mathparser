package ru.impl;

import org.junit.Before;
import org.junit.Test;

import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.NotUniqueNamingException;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.parse.Parser;
import ru.introguzzle.mathparser.tokenize.Tokenizer;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.parse.MathParser;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.tokenize.MathTokenizer;
import ru.introguzzle.mathparser.tokenize.UnknownSymbolTokenizeException;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.symbol.Variables;

import static org.junit.Assert.assertEquals;

public class MathParserTest {

    private Parser<Double> parser;

    @Before
    public void setUp() {
        Tokenizer tokenizer = new MathTokenizer();
        parser = new MathParser(tokenizer);
    }

    @Test
    public void test_simple_expression1() throws Exception {
        Expression expression = new MathExpression("3 + 4 * 2 / (1 - 5) ** 2 ** 3");
        Double result = parser.parse(expression);
        assertEquals(Double.valueOf(3.0001220703125), result);
    }

    @Test
    public void test_simple_expression2() throws Exception {
        Expression expression = new MathExpression("2 ** 2 ** 2 ** 3");
        Double result = parser.parse(expression);
        assertEquals(Double.valueOf(Math.pow(2, 256)), result);
    }

    @Test
    public void test_subtraction() throws Exception {
        Expression expression = new MathExpression("-1 - 1");
        Double result = parser.parse(expression);
        assertEquals(Double.valueOf(-2.0), result);
    }

    @Test(expected = UnknownSymbolTokenizeException.class)
    public void test_illegal_variables() throws Exception {
        Expression expression = new MathExpression("max(a, b, c)");

        Variables<Double> variables = new Variables<>();
        variables.add(new Variable<>("a", 2.0));

        parser.parse(expression, new NamingContext<>(variables));
    }

    @Test
    public void test_variadic_functions_and_changing_variables() throws Exception {
        Expression expression = new MathExpression("max(a, b, c)");

        Variable<Double> a = new Variable<>("a", 2.0);
        Variable<Double> b = new Variable<>("b", 3.0);
        Variable<Double> c = new Variable<>("c", 444.0);
        Variables<Double> variables = new Variables<>(a, b, c);

        NamingContext<Double> context = new NamingContext<>(variables);

        Double result = parser.parse(expression, context);
        assertEquals(Double.valueOf(444.0), result);

        variables.setValue("a", 666.0);

        result = parser.parse(expression, context);
        assertEquals(Double.valueOf(666.0), result);

        c.setValue(1000.0);
        result = parser.parse(expression, context);
        assertEquals(Double.valueOf(1000.0), result);
    }

    @Test
    public void test_operator_precedence() throws Exception {
        assertEquals(Double.valueOf(111 << (2 + 3 * 4)), parser.parse(new MathExpression("111 << 2 + 3 * 4")));
        assertEquals(Double.valueOf((2 + 3 * 4) << 1), parser.parse(new MathExpression("2 + 3 * 4 << 1")));
        assertEquals(Double.valueOf(2 * 3 + 2), parser.parse(new MathExpression("2 * 3 + 4 / 2")));
        assertEquals(Double.valueOf(7 / 2.0 + 1), parser.parse(new MathExpression("7 / 2 + 1")));
        assertEquals(Double.valueOf(3 * 2 + 3), parser.parse(new MathExpression("3 * 2 + 3")));
        assertEquals(Double.valueOf(2 + 3 * 2), parser.parse(new MathExpression("2 + 3 * 2")));
        assertEquals(Double.valueOf(1 / 2.0 * 1), parser.parse(new MathExpression("1 / 2 * 1")));
        assertEquals(Double.valueOf(5 / 2.0 * 1), parser.parse(new MathExpression("5 / 2 * 1")));
        assertEquals(Double.valueOf(1.0), parser.parse(new MathExpression("1 + 2 == 3")));
        assertEquals(Double.valueOf(4 << 2), parser.parse(new MathExpression("4 << 2")));
        assertEquals(Double.valueOf(5 >> 2), parser.parse(new MathExpression("5 >> 2")));
        assertEquals(Double.valueOf(4 * Math.pow(2, 2)), parser.parse(new MathExpression("4 * 2 ** 2")));
        assertEquals(Double.valueOf(Math.pow(2, 2)), parser.parse(new MathExpression("2 ** 2")));
        assertEquals(Double.valueOf(1.0), parser.parse(new MathExpression("5 < 6")));
        assertEquals(Double.valueOf(0.0), parser.parse(new MathExpression("5 > 6")));
        assertEquals(Double.valueOf(1.0), parser.parse(new MathExpression("5 == 5")));
        assertEquals(Double.valueOf(0.0), parser.parse(new MathExpression("5 != 5")));
        assertEquals(Double.valueOf(3 | 4), parser.parse(new MathExpression("3 | 4")));
        assertEquals(Double.valueOf(4 & 5), parser.parse(new MathExpression("4 & 5")));
        assertEquals(Double.valueOf(5 ^ 4), parser.parse(new MathExpression("5 ^ 4")));
        assertEquals(Double.valueOf(3 & 6 | 2), parser.parse(new MathExpression("3 & 6 | 2")));
        assertEquals(Double.valueOf(5 & 3 | 4), parser.parse(new MathExpression("5 & 3 | 4")));
        assertEquals(Double.valueOf(1 & 3 | 2), parser.parse(new MathExpression("1 & 3 | 2")));
        assertEquals(Double.valueOf(3 & 2 | 4), parser.parse(new MathExpression("3 & 2 | 4")));
        assertEquals(Double.valueOf(12 | 5 & 7), parser.parse(new MathExpression("12 | 5 & 7")));
        assertEquals(Double.valueOf(8 & 2 | 1), parser.parse(new MathExpression("8 & 2 | 1")));
    }

    @Test
    public void test_division_by_zero() throws Exception {
        Expression positiveInfinity = new MathExpression("1 / 0");
        Expression negativeInfinity = new MathExpression("-1 / 0");

        Double result1 = parser.parse(positiveInfinity);
        Double result2 = parser.parse(negativeInfinity);

        assertEquals((Double) Double.POSITIVE_INFINITY, result1);
        assertEquals((Double) Double.NEGATIVE_INFINITY, result2);
    }

    @Test
    public void test_negative_number_to_real_power() throws Exception {
        Expression expression = new MathExpression("-1 ** 0.33");

        assertEquals(parser.parse(expression), (Double) Double.NaN);
    }

    @Test
    public void test_logarithm() throws Exception {
        Expression expression = new MathExpression("ln(e)");

        assertEquals(parser.parse(expression), (Double) 1.0);
    }

    @Test
    public void test_expression_with_variables() throws Exception {
        Expression expression = new MathExpression("a + b + c + d");

        Variables<Double> variables = new Variables<>();
        variables.add("a", 5.0);
        variables.add("b", Math.PI);
        variables.add("c", 10.0);
        variables.add("d", 2.0);

        Context<Double> context = new NamingContext<>(variables);

        Double result = parser.parse(expression, context);
        assertEquals(Double.valueOf(5.0 + Math.PI + 10 + 2), result);
    }

    @Test
    public void test_expression_with_constants() throws Exception {
        Expression expression = new MathExpression("pi * 2");
        Double result = parser.parse(expression);
        assertEquals(Double.valueOf(Math.PI * 2), result);
    }

    @Test
    public void test_expression_with_functions() throws Exception {
        Expression expression = new MathExpression("sin(pi / 2)");
        Double result = parser.parse(expression);
        assertEquals(Double.valueOf(1.0), result);
    }

    @Test
    public void test_complex_expression() throws Exception {
        Expression expression = new MathExpression("3 + 4 * 2 / (1 - 5) ** 2 ** 3 + sin(pi / 2)");
        Double result = parser.parse(expression);
        assertEquals(Double.valueOf(3.0001220703125 + 1.0), result);
    }

    @Test
    @SuppressWarnings("unused")
    public void test_unique_variables() {
        Variables<Double> variables = new Variables<>();
        variables.add("a", 1.43);
        variables.add("b", 21221.0);

        variables = new Variables<>(
                new Variable<>("a", 9.0),
                new Variable<>("b", 4.0)
        );

        variables.size();
    }

    @Test(expected = NotUniqueNamingException.class)
    public void test_not_unique_variables_throws1() {
        Variables<Double> variables = new Variables<>(
            new Variable<>("a", 3.0),
            new Variable<>("a", 3.0)
        );

        variables.size();
    }

    @Test(expected = NotUniqueNamingException.class)
    public void test_not_unique_variables_throws2() {
        Variables<Double> variables = new Variables<>();

        variables.add(new Variable<>("a", 3.0));
        variables.add(new Variable<>("a", 4.0));
    }
}
