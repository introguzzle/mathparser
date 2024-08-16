package ru.introguzzle.mathparser.generate;

import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.common.options.Options;
import ru.introguzzle.mathparser.operator.DoubleOperatorReflector;

public abstract class GeneratorOptions implements Options {
    public static final int INCLUDE_FLOATS = 1;
    public static final int INCLUDE_INTEGERS = 1 << 1;
    public static final int ONLY_DEFAULT_VARIABLE = 1 << 2;

    private int flags;

    private String defaultVariable = "x";

    private String[] operatorsSymbols = DoubleOperatorReflector.get().values()
            .stream().map(Nameable::getName).toArray(String[]::new);

    private int min = 0;
    private int max = 100;

    private int maxFloating = 3;

    private int maxLength = 10;

    private int maxDepth = 2;

    private int maxAdditionalVariadicArgs = 3;

    private Distribution distribution = new Distribution();

    public GeneratorOptions() {
        this.flags = 0;
    }

    public GeneratorOptions(int... flags) {
        this.setFlags(flags);
    }

    public void setFlags(int... flags) {
        for (int flag: flags) {
            this.flags |= flag;
        }
    }

    @Override
    public int getFlags() {
        return this.flags;
    }

    public String getDefaultVariable() {
        return defaultVariable;
    }

    public void setDefaultVariable(String defaultVariable) {
        this.defaultVariable = defaultVariable;
    }

    public String[] getOperatorsSymbols() {
        return operatorsSymbols;
    }

    public void setOperatorsSymbols(String[] operatorsSymbols) {
        this.operatorsSymbols = operatorsSymbols;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMaxFloating() {
        return maxFloating;
    }

    public void setMaxFloating(int maxFloating) {
        this.maxFloating = maxFloating;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getMaxAdditionalVariadicArgs() {
        return maxAdditionalVariadicArgs;
    }

    public void setMaxAdditionalVariadicArgs(int maxAdditionalVariadicArgs) {
        this.maxAdditionalVariadicArgs = maxAdditionalVariadicArgs;
    }

    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }
}
