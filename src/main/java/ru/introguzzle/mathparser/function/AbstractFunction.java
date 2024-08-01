package ru.introguzzle.mathparser.function;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractFunction implements Function {

    private final String name;
    private final int requiredArguments;

    public AbstractFunction(String name, int requiredArguments) {
        this.name = name;
        this.requiredArguments = requiredArguments;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public int getRequiredArguments() {
        return this.requiredArguments;
    }

    @Override
    public abstract Double apply(List<Double> arguments);

    @Override
    public AbstractFunction clone() throws CloneNotSupportedException {
        return (AbstractFunction) super.clone();
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
