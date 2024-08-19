package ru.introguzzle.mathparser.unit.temperature;

import java.util.Set;

public class FahrenheitUnit extends TemperatureUnit {
    public static final double SHIFT = 459.67;

    public FahrenheitUnit() {
        super(Set.of("fahrenheit", "fahrenheits"), "F");
    }

    @Override
    protected double toKelvin(double value) {
        return (value + SHIFT) * 5 / 9;
    }

    @Override
    protected double fromKelvin(double value) {
        return value * 9 / 5 - SHIFT;
    }

    @Override
    public boolean isDefinedOnNegative() {
        return true;
    }
}
