package ru.introguzzle.mathparser.common;

import ru.introguzzle.mathparser.symbol.Coefficient;
import ru.introguzzle.mathparser.symbol.Coefficients;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.symbol.Variables;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 */
public class EvaluationContext implements Context {
    private Variables variables = new Variables();
    private Coefficients coefficients = new Coefficients();

    private Context parent;

    private final Set<String> names = new HashSet<>();

    public EvaluationContext() {

    }

    public EvaluationContext(Context parent) {
        if (parent != null) {
            this.parent = parent;
            this.variables.getItems().addAll(parent.getVariables().getItems());
            this.coefficients.getItems().addAll(parent.getCoefficients().getItems());
        }
    }

    public EvaluationContext(Variables variables, Coefficients coefficients) {
        this.variables = variables != null ? variables : new Variables();
        this.coefficients = coefficients != null ? coefficients : new Coefficients();
    }

    @Override
    public Variables getVariables() {
        return variables;
    }

    @Override
    public Coefficients getCoefficients() {
        return coefficients;
    }

    @Override
    public Context getParent() {
        return parent;
    }

    @Override
    public void addVariable(Variable variable) {
        String name;
        boolean added = names.add(name = variable.getName());
        if (!added) {
            throw new NotUniqueNameContextException(name, names);
        }

        variables.add(variable);
    }

    @Override
    public void addCoefficient(Coefficient coefficient) {
        String name;
        boolean added = names.add(name = coefficient.getName());
        if (!added) {
            throw new NotUniqueNameContextException(name, names);
        }

        coefficients.add(coefficient);
    }

    @Override
    public boolean removeVariable(Variable variable) {
        names.remove(variable.getName());
        return variables.remove(variable);
    }

    @Override
    public boolean removeCoefficient(Coefficient coefficient) {
        names.remove(coefficient.getName());
        return coefficients.remove(coefficient);
    }

    @Override
    public boolean removeVariable(String name) {
        names.remove(name);
        return variables.remove(name);
    }

    @Override
    public boolean removeCoefficient(String name) {
        names.remove(name);
        return coefficients.remove(name);
    }

    @Override
    public Optional<? extends Variable> getVariable(String name) {
        if (parent != null) {
            return parent.getVariable(name);
        }

        return variables.find(name);
    }

    @Override
    public Optional<? extends Coefficient> getCoefficient(String name) {
        if (parent != null) {
            return parent.getCoefficient(name);
        }

        return coefficients.find(name);
    }

    @Override
    public Set<String> getNames() {
        return new HashSet<>(names);
    }

    @Override
    public void setParent(Context parent) {
        this.parent = parent;
    }

    @Override
    public void setVariables(Variables variables) {
        this.variables = variables;
    }

    @Override
    public void setCoefficients(Coefficients coefficients) {
        this.coefficients = coefficients;
    }
}
