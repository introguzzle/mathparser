package ru.introguzzle.mathparser.unit.temperature;

import ru.introguzzle.mathparser.unit.Unit;

import java.util.Set;

public class CelsiusUnit extends TemperatureUnit {
    public CelsiusUnit() {
        super(Set.of("Celsius"), "C");
    }

    @Override
    protected double toKelvin(double value) {
        return value + 273.15;
    }

    @Override
    protected double fromKelvin(double value) {
        return value - 273.15;
    }

    @Override
    public boolean isDefinedOnNegative() {
        return true;
    }

    public static void main(String[] args) {
        var unit = new CelsiusUnit();
        System.out.println(unit.apply(-30.0, new FahrenheitUnit()));
    }
}
