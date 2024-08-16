package ru.introguzzle.mathparser.unit.time;

import java.util.Set;

public class SecondUnit extends TimeUnit {
    public SecondUnit() {
        super(Set.of("seconds", "second", "секунды", "секунд", "секунда"), "s");
    }

    @Override
    protected double toSeconds(double value) {
        return value;
    }

    @Override
    protected double fromSeconds(double value) {
        return value;
    }
}
