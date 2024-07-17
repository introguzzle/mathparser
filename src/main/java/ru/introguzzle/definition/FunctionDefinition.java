package ru.introguzzle.definition;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.expression.MathExpression;

public class FunctionDefinition extends MathExpression {

    public FunctionDefinition(@NotNull String string) {
        super(string);
    }

    public FunctionDefinition(@NotNull String string, boolean compact) {
        super(string, compact);
    }

    public int getDefinitionSpliterator() {
        return this.getString().indexOf("=");
    }
}
