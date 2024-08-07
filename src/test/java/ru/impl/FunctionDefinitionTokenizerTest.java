package ru.impl;

import org.junit.Before;
import org.junit.Test;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.definition.FunctionDefinition;
import ru.introguzzle.mathparser.definition.FunctionDefinitionType;
import ru.introguzzle.mathparser.group.FunctionGroup;
import ru.introguzzle.mathparser.symbol.Coefficient;
import ru.introguzzle.mathparser.symbol.MutableSymbol;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.tokenize.FunctionDefinitionTokenizer;
import ru.introguzzle.mathparser.tokenize.TokenizeException;

import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;


public class FunctionDefinitionTokenizerTest {

    private FunctionDefinitionTokenizer<Double> tokenizer;

    @Before
    public void setUp() {
        tokenizer = new FunctionDefinitionTokenizer<>() {

            @Override
            public Supplier<MutableSymbol<Double>> getDefaultFactory(CharSequence name, Double value) {
                return () -> new Coefficient<>(name.toString(), value);
            }

            @Override
            public Double getDefaultValue() {
                return 0.0;
            }
        };
    }

    @Test
    public void test_function_definition() throws Exception {
        FunctionDefinition<Double> definition = new FunctionDefinition<>("f(x)    = x + x ** 3", new Variable<>("x", 0.0));
        Context<Double> context = new NamingContext<>();
        FunctionGroup group = tokenizer.tokenizeDefinition(definition, context);

        System.out.println(group.getTokens().toExpression());
        group.getTokens().forEach(System.out::println);
    }

    @Test
    public void test_explicit_function_with_parameters() throws TokenizeException {
        Context<Double> context = new NamingContext<>();

        var definition = new FunctionDefinition<>("f(z) = z + a", new Variable<>("z", 0.0));
        var group = tokenizer.tokenizeDefinition(definition, context);
        System.out.println(context);
        assertEquals(FunctionDefinitionType.EXPLICIT_FUNCTION_WITH_PARAMETERS, group.getType());
    }

    @Test
    public void test_parametric_function() throws TokenizeException {
        Context<Double> context = new NamingContext<>();

        var definition = new FunctionDefinition<>("sin(t)", new Variable<>("t", 0.0));
        var group = tokenizer.tokenizeDefinition(definition, context);

        assertEquals(FunctionDefinitionType.PARAMETRIC_FUNCTION, group.getType());
    }

    @Test
    public void test_explicit_function() throws TokenizeException {
        Context<Double> context = new NamingContext<>();

        var definition = new FunctionDefinition<>("g(x) = x^2", new Variable<>("x", 0.0));
        var group = tokenizer.tokenizeDefinition(definition, context);
        System.out.println(group.getTokens().get(0));
        assertEquals(FunctionDefinitionType.EXPLICIT_FUNCTION, group.getType());
    }

    @Test
    public void test_parametric_function_with_parameters() throws TokenizeException {
        Context<Double> context = new NamingContext<>();

        var definition = new FunctionDefinition<>("cos(t) + b", new Variable<>("t", 0.0));
        var group = tokenizer.tokenizeDefinition(definition, context);

        assertEquals(FunctionDefinitionType.PARAMETRIC_FUNCTION_WITH_PARAMETERS, group.getType());
    }
}

