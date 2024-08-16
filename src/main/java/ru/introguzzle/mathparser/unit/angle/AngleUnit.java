package ru.introguzzle.mathparser.unit.angle;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.unit.AbstractUnit;
import ru.introguzzle.mathparser.unit.measure.AngleMeasure;

import java.util.Set;

public abstract class AngleUnit extends AbstractUnit<AngleMeasure, AngleUnit> {
    public AngleUnit(Set<String> names, String main) {
        super(names, main);
    }

    @Override
    public double transform(double value, AngleUnit unit) {
        double valueInRadians = toRadians(value);
        return unit.fromRadians(valueInRadians);
    }

    protected abstract double toRadians(double value);
    protected abstract double fromRadians(double value);

    @Override
    public @NotNull AngleMeasure getMeasure() {
        return AngleMeasure.ANGLE;
    }
}
