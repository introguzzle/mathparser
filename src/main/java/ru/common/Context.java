package ru.common;

import ru.symbol.Coefficient;
import ru.symbol.Coefficients;
import ru.symbol.Variable;
import ru.symbol.Variables;

import java.util.Optional;

public interface Context {
    Variables getVariables();

    Coefficients getCoefficients();

    Context getParent();

    void addVariable(Variable variable);

    void addCoefficient(Coefficient coefficient);

    Optional<? extends Variable> getVariable(String name);

    Optional<? extends Coefficient> getCoefficient(String name);

    void setParent(Context parent);

    void setVariables(Variables variables);

    void setCoefficients(Coefficients coefficients);
}
