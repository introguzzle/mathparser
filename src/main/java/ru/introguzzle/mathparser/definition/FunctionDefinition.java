package ru.introguzzle.mathparser.definition;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.symbol.Variable;

public class FunctionDefinition extends MathExpression {

    private final Variable<Double> variable;

    public FunctionDefinition(@NotNull String string, Variable<Double> variable) {
        super(string);
        this.variable = variable;
    }

    public int getDefinitionSpliterator() {
        int index = getString().indexOf("=");

        return index == -1
                ? 0
                : index;
    }

    public Variable<Double> getVariable() {
        return variable;
    }
}
