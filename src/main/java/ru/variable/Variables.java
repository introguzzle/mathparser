package ru.variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Variables {
    private List<Variable> variables = new ArrayList<>();
    public Variables() {

    }

    public Variables(Variable... variables) {
        Collections.addAll(this.variables, variables);
    }

    public Variables(List<Variable> variables) {
        this.variables = variables;
    }

    public Optional<Variable> find(String representation) {
        for (var variable: this.variables) {
            if (variable.getRepresentation().equals(representation)) {
                return Optional.of(variable);
            }
        }

        return Optional.empty();
    }

    public void setValue(String representation, double value) {
        find(representation).orElseThrow().setValue(value);
    }

    public void add(String representation, double value) {
        this.variables.add(new Variable(representation, value));
    }

    public void add(Variable variable) {
        this.variables.add(variable);
    }

    @Override
    public String toString() {
        return this.variables.toString();
    }

    public int size() {
        return this.variables.size();
    }
}
