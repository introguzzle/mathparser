package ru.introguzzle.mathparser.unit;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.tokenize.token.type.OperatorType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public class UnitConverter implements Nameable {
    @Override
    public @NotNull String getName() {
        return "to";
    }

    @Override
    public @NotNull Type type() {
        return OperatorType.CONVERTER;
    }
}
