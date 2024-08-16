package ru.introguzzle.mathparser.unit.angle;

import java.util.Set;

public class DegreeUnit extends AngleUnit {

    public DegreeUnit() {
        super(Set.of("degree", "degrees"), "deg");
    }

    @Override
    protected double toRadians(double value) {
        return Math.toRadians(value);
    }

    @Override
    protected double fromRadians(double value) {
        return Math.toDegrees(value);
    }
}
