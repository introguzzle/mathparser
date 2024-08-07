package ru.introguzzle.mathparser.definition;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.symbol.Variable;

public class FunctionDefinition<T extends Number> extends MathExpression {

    private final Variable<T> variable;

    public FunctionDefinition(@NotNull String string, Variable<T> variable) {
        super(string);
        this.variable = variable;
    }

    public int getDefinitionSpliterator() {
        int index = getString().indexOf("=");

        return index == -1
                ? 0
                : index;
    }

    public Variable<T> getVariable() {
        return variable;
    }
}
