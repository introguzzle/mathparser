package ru.introguzzle.common;

import ru.introguzzle.symbol.Coefficient;
import ru.introguzzle.symbol.Coefficients;
import ru.introguzzle.symbol.Variable;
import ru.introguzzle.symbol.Variables;

import java.util.Optional;
import java.util.Set;

public interface Context {
    Variables getVariables();

    Coefficients getCoefficients();

    Context getParent();

    void addVariable(Variable variable);

    void addCoefficient(Coefficient coefficient);

    boolean removeVariable(Variable variable);

    boolean removeCoefficient(Coefficient coefficient);

    boolean removeVariable(String name);

    boolean removeCoefficient(String name);

    Optional<? extends Variable> getVariable(String name);

    Optional<? extends Coefficient> getCoefficient(String name);

    Set<String> getNames();

    void setParent(Context parent);

    void setVariables(Variables variables);

    void setCoefficients(Coefficients coefficients);
}
