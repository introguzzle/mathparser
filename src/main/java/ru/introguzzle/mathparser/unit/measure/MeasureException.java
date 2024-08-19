package ru.introguzzle.mathparser.unit.measure;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class MeasureException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5810701527594156809L;

    public MeasureException(@NotNull String message) {
        super(message);
    }

    public MeasureException(Measure from, Measure to) {
        super("Incompatible measures: " + from.describe() + " to " + to.describe());
    }
}
