package ru.introguzzle.symbol;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Variables extends MutableSymbolList<Variable> {

    public Variables() {
        super();
    }

    public Variables(List<Variable> variableList) {
        super(variableList);
    }

    public Variables(Variable... variables) {
        super(variables);
    }

    public void add(String name, double value) {
        this.add(new Variable(name, value));
    }

    protected void checkUnique(@NotNull Variable item) {
        if (getNames().contains(item.getName())) {
            throw new NotUniqueVariableException(item);
        }
    }

    public void setValue(String representation, double value) {
        find(representation)
                .orElseThrow(() -> new NoSuchVariableException(representation, getNames()))
                .setValue(value);
    }
}
