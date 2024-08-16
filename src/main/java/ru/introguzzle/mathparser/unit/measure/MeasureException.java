package ru.introguzzle.mathparser.unit.measure;

public class MeasureException extends RuntimeException {
    public MeasureException(Measure from, Measure to) {
        super("Incompatible measures: " + from + " to " + to);
    }
}
