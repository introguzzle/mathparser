package ru.introguzzle.mathparser.unit.time;

import java.util.Set;

public class HourUnit extends TimeUnit {
    public HourUnit() {
        super(Set.of("hours", "hour", "часов", "часа", "час"), "h");
    }

    @Override
    protected double toSeconds(double value) {
        return value / 3600;
    }

    @Override
    protected double fromSeconds(double value) {
        return value * 3600;
    }
}
