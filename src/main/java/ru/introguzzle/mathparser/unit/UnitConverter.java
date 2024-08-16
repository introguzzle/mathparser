package ru.introguzzle.mathparser.unit;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.tokenize.token.type.OperatorType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;
import ru.introguzzle.mathparser.unit.measure.Measure;

public class UnitConverter implements Nameable {
    public <M extends Measure, U extends Unit<M, U>>
    Double transform(double value, U from, U to) {
        return from.transform(value, to);
    }

    @Override
    public @NotNull String getName() {
        return "to";
    }

    @Override
    public @NotNull Type type() {
        return OperatorType.CONVERTER;
    }
}
