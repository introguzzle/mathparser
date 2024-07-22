package ru.impl;

import org.junit.Before;
import org.junit.Test;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.definition.FunctionDefinition;
import ru.introguzzle.mathparser.definition.FunctionDefinitionType;
import ru.introguzzle.mathparser.symbol.Coefficient;
import ru.introguzzle.mathparser.symbol.MutableSymbol;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.tokenize.FunctionDefinitionTokenizer;
import ru.introguzzle.mathparser.tokenize.TokenizeException;

import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;


public class FunctionDefinitionTokenizerTest {

    private FunctionDefinitionTokenizer tokenizer;

    @Before
    public void setUp() {
        tokenizer = new FunctionDefinitionTokenizer() {
            @Override
            public Supplier<MutableSymbol> defaultSupplier(CharSequence name, double value) {
                return () -> new Coefficient(name.toString(), value);
            }

            @Override
            public double getDefaultValue() {
                return 0;
            }
        };
    }

    @Test
    public void test_explicit_function_with_parameters() throws TokenizeException {
        Context context = new NamingContext();

        var definition = new FunctionDefinition("f(z) = z + a", new Variable("z", 0));
        var result = tokenizer.tokenize(definition, context);
        assertEquals(FunctionDefinitionType.EXPLICIT_FUNCTION_WITH_PARAMETERS, result.getType());
    }

    @Test
    public void test_parametric_function() throws TokenizeException {
        Context context = new NamingContext();

        var definition = new FunctionDefinition("sin(t)", new Variable("t", 0));
        var result = tokenizer.tokenize(definition, context);

        assertEquals(FunctionDefinitionType.PARAMETRIC_FUNCTION, result.getType());
    }

    @Test
    public void test_explicit_function() throws TokenizeException {
        Context context = new NamingContext();

        var definition = new FunctionDefinition("g(x) = x^2", new Variable("x", 0));
        var result = tokenizer.tokenize(definition, context);
        assertEquals(FunctionDefinitionType.EXPLICIT_FUNCTION, result.getType());
    }

    @Test
    public void test_parametric_function_with_parameters() throws TokenizeException {
        Context context = new NamingContext();

        var definition = new FunctionDefinition("cos(t) + b", new Variable("t", 0));
        var result = tokenizer.tokenize(definition, context);

        assertEquals(FunctionDefinitionType.PARAMETRIC_FUNCTION_WITH_PARAMETERS, result.getType());
    }
}

