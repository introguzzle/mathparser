package ru.introguzzle.mathparser.unit.angle;

import java.util.Set;

public class RadianUnit extends AngleUnit {
    public RadianUnit() {
        super(Set.of("radian", "radians"), "rad");
    }

    @Override
    protected double toRadians(double value) {
        return value;
    }

    @Override
    protected double fromRadians(double value) {
        return value;
    }
}
