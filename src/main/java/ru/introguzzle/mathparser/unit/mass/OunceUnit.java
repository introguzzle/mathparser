package ru.introguzzle.mathparser.unit.mass;

import java.util.Set;

public class OunceUnit extends MassUnit {
    public static final double FACTOR = 0.0283495231;

    public OunceUnit() {
        super(Set.of("ounce", "ounces", "унция", "унций"), "oz");
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
