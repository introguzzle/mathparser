package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.operator.DoubleOperator;
import ru.introguzzle.mathparser.operator.Operator;

import java.util.List;

public abstract class DoubleFunction implements Function<Double> {
    private final String name;
    private final int requiredArguments;

    public DoubleFunction(String name, int requiredArguments) {
        this.name = name;
        this.requiredArguments = requiredArguments;
    }

    @Override
    public DoubleOperator toOperator() {
        Operator<Double> operator = Function.super.toOperator();

        return new DoubleOperator() {
            @Override
            public int getRequiredOperands() {
                return operator.getRequiredOperands();
            }

            @Override
            public Double apply(List<Double> doubles) {
                return operator.apply(doubles);
            }

            @Override
            public @NotNull String getName() {
                return operator.getName();
            }

            @Override
            public Association getAssociation() {
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
    public abstract Double apply(List<Double> arguments);

    @Override
    public DoubleFunction clone() throws CloneNotSupportedException {
        return (DoubleFunction) super.clone();
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
