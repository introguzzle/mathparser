package ru.impl;

import org.junit.Test;
import ru.introguzzle.common.Context;
import ru.introguzzle.common.EvaluationContext;
import ru.introguzzle.common.NotUniqueNameContextException;
import ru.introguzzle.symbol.Coefficient;
import ru.introguzzle.symbol.Variable;

import static org.junit.Assert.*;

public class ContextTest {

    @Test
    public void test_delete() {
        Context context = new EvaluationContext();

        context.addVariable(new Variable("x", 3));
        assertTrue(context.removeVariable("x"));

        assertEquals(context.getVariables().size(), 0);
        assertFalse(context.removeVariable("x"));
    }

    @Test(expected = NotUniqueNameContextException.class)
    public void test_adding_not_unique_fails() {
        Context context = new EvaluationContext();

        context.addVariable(new Variable("x", 3));
        context.addVariable(new Variable("x", 3));
    }

    @Test(expected = NotUniqueNameContextException.class)
    public void test_adding_variable_and_coefficient_with_same_name() {
        Context context = new EvaluationContext();

        context.addVariable(new Variable("x", 3));
        context.addCoefficient(new Coefficient("x", 3));
    }
}
