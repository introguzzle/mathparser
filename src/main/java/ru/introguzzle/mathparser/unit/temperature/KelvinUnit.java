package ru.introguzzle.mathparser.unit.temperature;

import java.util.Set;

public class KelvinUnit extends TemperatureUnit {
    public KelvinUnit() {
        super(Set.of("kelvin", "kelvins"), "K");
    }

    @Override
    protected double toKelvin(double value) {
        return value;
    }

    @Override
    protected double fromKelvin(double value) {
        return value;
    }
}
