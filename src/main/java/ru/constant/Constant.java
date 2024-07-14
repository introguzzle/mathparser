package ru.constant;

import ru.contract.Symbol;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public abstract class Constant implements Symbol, Serializable {

    @Serial
    private static final long serialVersionUID = 215493212718L;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constant constant = (Constant) o;
        return Double.compare(value, constant.value) == 0 && Objects.equals(representation, constant.representation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(representation, value);
    }

    @Override
    public String toString() {
        return "Constant{" +
                "representation='" + representation + '\'' +
                ", value=" + value +
                '}';
    }
}
