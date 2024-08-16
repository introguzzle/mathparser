package ru.introguzzle.mathparser.unit.length;

import java.util.Set;

public class KilometerUnit extends LengthUnit {
    public static final double FACTOR = 1000.0;

    public static KilometerUnit get() {
        return new KilometerUnit();
    }

    public KilometerUnit() {
        super(Set.of("kilometer", "kilometers", "километр", "километра", "километров"), "km");
    }

    @Override
    protected double toMeters(double value) {
        return value * FACTOR;
    }

    @Override
    protected double fromMeters(double value) {
        return value / FACTOR;
    }
}
