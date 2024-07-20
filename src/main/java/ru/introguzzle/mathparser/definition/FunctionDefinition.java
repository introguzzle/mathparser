package ru.introguzzle.mathparser.definition;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.expression.MathExpression;

public class FunctionDefinition extends MathExpression {

    public FunctionDefinition(@NotNull String string) {
        super(string);
    }

    public int getDefinitionSpliterator() {
        return this.getString().indexOf("=");
    }
}
