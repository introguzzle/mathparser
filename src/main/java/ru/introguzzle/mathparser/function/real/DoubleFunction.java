package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.operator.DoubleOperator;
import ru.introguzzle.mathparser.operator.Operator;

import java.util.List;
import java.util.Objects;

public abstract class DoubleFunction implements Function<Double> {
    private final String name;
    private final int requiredArguments;

    public DoubleFunction(String name, int requiredArguments) {
        this.name = name;
        this.requiredArguments = requiredArguments;
    }

    @Override
    @NotNull
    public DoubleOperator asOperator() {
        Operator<Double> operator = Function.super.asOperator();

        return new DoubleOperator() {
            @Override
            public int getRequiredOperands() {
                return operator.getRequiredOperands();
            }

            @Override
            public Double apply(List<Double> operands) {
                return operator.apply(operands);
            }

            @Override
            public @NotNull String getName() {
                return operator.getName();
            }

            @Override
            public @NotNull Association getAssociation() {
                return Association.LEFT;
            }

            @Override
            public int getPriority() {
                return operator.getPriority();
            }
        };
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public int getRequiredArguments() {
        return requiredArguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleFunction that = (DoubleFunction) o;
        return requiredArguments == that.requiredArguments && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, requiredArguments);
    }

    @Override
    public String toString() {
        return describe() + '{' +
                "name='" + name + '\'' +
                ", requiredArguments=" + requiredArguments + '\'' +
                ", variadic='" + isVariadic() + '\'' +
                '}';
    }
}
