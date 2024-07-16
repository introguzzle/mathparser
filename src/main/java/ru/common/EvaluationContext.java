package ru.common;

import ru.symbol.Coefficient;
import ru.symbol.Coefficients;
import ru.symbol.Variable;
import ru.symbol.Variables;

import java.util.Optional;

/**
 *
 */
public class EvaluationContext implements Context {
    private Variables variables = new Variables();
    private Coefficients coefficients = new Coefficients();

    private Context parent;

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
        this.variables.add(variable);
    }

    @Override
    public void addCoefficient(Coefficient coefficient) {
        this.coefficients.add(coefficient);
    }

    @Override
    public Optional<? extends Variable> getVariable(String name) {
        if (parent != null) {
            return parent.getVariable(name);
        }

        return this.variables.find(name);
    }

    @Override
    public Optional<? extends Coefficient> getCoefficient(String name) {
        if (parent != null) {
            return parent.getCoefficient(name);
        }

        return this.coefficients.find(name);
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
