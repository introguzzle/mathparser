package ru.introguzzle.mathparser.unit.time;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.unit.AbstractUnit;
import ru.introguzzle.mathparser.unit.measure.Measure;

import java.util.Set;

public abstract class TimeUnit extends AbstractUnit<Measure.TimeMeasure, TimeUnit> {
    public TimeUnit(Set<String> names, String main) {
        super(names, main);
    }

    @Override
    public double transform(double value, TimeUnit unit) {
        double valueInSeconds = toSeconds(value);
        return unit.fromSeconds(valueInSeconds);
    }

    protected abstract double toSeconds(double value);
    protected abstract double fromSeconds(double value);

    @Override
    public @NotNull Measure.TimeMeasure getMeasure() {
        return Measure.TimeMeasure.TIME;
    }
}
