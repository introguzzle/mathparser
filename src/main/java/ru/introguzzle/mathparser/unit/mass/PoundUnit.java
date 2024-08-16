package ru.introguzzle.mathparser.unit.mass;

import java.util.Set;

public class PoundUnit extends MassUnit {
    public static final double FACTOR = 0.45359237;

    public PoundUnit() {
        super(Set.of("pound", "pounds", "фунт", "фунта", "фунтов"), "lb");
    }

    @Override
    protected double toKilograms(double value) {
        return value * FACTOR;
    }

    @Override
    protected double fromKilograms(double value) {
        return value / FACTOR;
    }
}
