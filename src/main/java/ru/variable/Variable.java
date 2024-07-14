package ru.variable;

import ru.contract.Symbol;

public class Variable implements Symbol {
    private final String representation;
    private transient double value;

    public Variable(String representation, double value) {
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

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "representation='" + representation + '\'' +
                ", value=" + value +
                '}';
    }
}
