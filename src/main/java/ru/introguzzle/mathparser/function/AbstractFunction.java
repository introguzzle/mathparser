package ru.introguzzle.mathparser.function;

import java.util.List;

public abstract class AbstractFunction implements Function {

    private final String name;
    private final int requiredArguments;

    public AbstractFunction(String name, int requiredArguments) {
        this.name = name;
        this.requiredArguments = requiredArguments;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getRequiredArguments() {
        return this.requiredArguments;
    }

    @Override
    public abstract Double apply(List<Double> arguments);

    @Override
    public String toString() {
        return "AbstractFunction{" +
                "name='" + name + '\'' +
                ", requiredArguments=" + requiredArguments + '\'' +
                ", variadic='" + isVariadic() + '\'' +
                '}';
    }
}
