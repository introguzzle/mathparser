package ru.introguzzle.mathparser.unit.time;

import java.util.Set;

public class MinuteUnit extends TimeUnit {
    public MinuteUnit() {
        super(Set.of("minutes", "minute", "минут", "минуты", "минута"), "min");
    }

    @Override
    protected double toSeconds(double value) {
        return value / 60;
    }

    @Override
    protected double fromSeconds(double value) {
        return value * 60;
    }
}
