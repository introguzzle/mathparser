package ru.introguzzle.mathparser.unit.length;

import java.util.Set;

public class MeterUnit extends LengthUnit {
    public MeterUnit() {
        super(Set.of("meter", "meters", "метр", "метра", "метров"), "m");
    }

    public static MeterUnit get() {
        return new MeterUnit();
    }

    @Override
    protected double toMeters(double value) {
        return value;
    }

    @Override
    protected double fromMeters(double value) {
        return value;
    }
}
