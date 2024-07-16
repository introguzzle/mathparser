package ru.symbol;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Coefficients extends MutableSymbolList<Coefficient> {
    public Coefficients() {
        super();
    }

    public Coefficients(@NotNull Coefficient... items) {
        super(items);
    }

    public Coefficients(@NotNull List<? extends Coefficient> items) {
        super(items);
    }

    public void add(String name, double value) {
        this.add(new Coefficient(name, value));
    }

    protected void checkUnique(@NotNull Coefficient item) {
        if (getNames().contains(item.getName())) {
            throw new NotUniqueCoefficientException(item);
        }
    }

    public void setValue(String representation, double value) {
        find(representation)
                .orElseThrow(() -> new NoSuchCoefficientException(representation, getNames()))
                .setValue(value);
    }
}

