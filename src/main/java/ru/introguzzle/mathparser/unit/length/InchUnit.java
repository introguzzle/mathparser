package ru.introguzzle.mathparser.unit.length;

import java.util.Set;

public class InchUnit extends LengthUnit {
    public static final double FACTOR = 39.3701;

    public static InchUnit get() {
        return new InchUnit();
    }

    public InchUnit() {
        super(Set.of("inch", "inches", "дюйм", "дюйма", "дюймов"), "inch");
    }

    @Override
    protected double toMeters(double value) {
        return value / FACTOR;
    }

    @Override
    protected double fromMeters(double value) {
        return value * FACTOR;
    }
}
