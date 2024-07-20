package ru.impl;

import org.junit.Test;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.common.NotUniqueNamingException;
import ru.introguzzle.mathparser.symbol.Coefficient;
import ru.introguzzle.mathparser.symbol.Variable;

import static org.junit.Assert.*;

public class ContextTest {

    @Test
    public void test_delete() {
        Context context = new NamingContext();

        context.addSymbol(new Variable("x", 3));
        assertTrue(context.removeSymbol("x"));

        assertEquals(context.getSymbols().size(), 0);
        assertFalse(context.removeSymbol("x"));
    }

    @Test(expected = NotUniqueNamingException.class)
    public void test_adding_not_unique_fails() {
        Context context = new NamingContext();

        context.addSymbol(new Variable("x", 3));
        context.addSymbol(new Variable("x", 3));
    }

    @Test(expected = NotUniqueNamingException.class)
    public void test_adding_variable_and_coefficient_with_same_name() {
        Context context = new NamingContext();

        context.addSymbol(new Variable("x", 3));
        context.addSymbol(new Coefficient("x", 3));
    }
}
