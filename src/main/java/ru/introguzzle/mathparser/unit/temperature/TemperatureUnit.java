package ru.introguzzle.mathparser.unit.temperature;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.unit.AbstractUnit;
import ru.introguzzle.mathparser.unit.measure.Measure;

import java.util.Set;

public abstract class TemperatureUnit extends AbstractUnit<Measure.TemperatureMeasure, TemperatureUnit> {

    public TemperatureUnit(Set<String> names, String main) {
        super(names, main);
    }

    @Override
    public double transform(double value, TemperatureUnit unit) {
        double valueInKelvin = toKelvin(value);
        return unit.fromKelvin(valueInKelvin);
    }

    protected abstract double toKelvin(double value);
    protected abstract double fromKelvin(double value);

    @Override
    public @NotNull Measure.TemperatureMeasure getMeasure() {
        return Measure.TemperatureMeasure.TEMPERATURE;
    }
}
