package ru.variable;

import org.jetbrains.annotations.NotNull;
import ru.exceptions.NotUniqueVariableException;

import java.util.*;

public class Variables {
    private final List<Variable> variables = new ArrayList<>();
    private final Set<String> representations = new HashSet<>();

    public Variables() {
    }

    public Variables(Variable @NotNull ... variables) {
        for (Variable variable : variables) {
            this.add(variable);
        }
    }

    public Variables(@NotNull List<Variable> variables) {
        for (Variable variable : variables) {
            this.add(variable);
        }
    }

    private void checkUnique(@NotNull Variable variable) {
        if (representations.contains(variable.getRepresentation())) {
            throw new NotUniqueVariableException(variable);
        }
    }

    public Optional<Variable> find(String representation) {
        for (Variable variable : variables) {
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
        this.add(new Variable(representation, value));
    }

    public void add(Variable variable) {
        this.checkUnique(variable);
        variables.add(variable);
        representations.add(variable.getRepresentation());
    }

    @Override
    public String toString() {
        return variables.toString();
    }

    public int size() {
        return variables.size();
    }
}
