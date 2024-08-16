package ru.introguzzle.mathparser.unit.mass;

import java.util.Set;

public class KilogramUnit extends MassUnit {
    public KilogramUnit() {
        super(Set.of("kilograms", "kilogram", "килограмм", "килограммов", "килограмма"), "kg");
    }

    @Override
    protected double toKilograms(double value) {
        return value;
    }

    @Override
    protected double fromKilograms(double value) {
        return value;
    }
}
