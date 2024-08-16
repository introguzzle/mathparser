package ru.introguzzle.mathparser.unit.mass;

import java.util.Set;

public class GramUnit extends MassUnit {

    public static final double FACTOR = 1000.0;

    public GramUnit() {
        super(Set.of("gram", "grams", "грамм", "граммов", "грамма"), "g");
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
