package ru.constant;

import ru.main.Symbol;

public abstract class Constant implements Symbol {
    private final String representation;
    private final double value;

    public Constant(String representation, double value) {
        this.representation = representation;
        this.value = value;
    }

    @Override
    public String getRepresentation() {
        return representation;
    }

    @Override
    public double getValue() {
        return value;
    }
}
