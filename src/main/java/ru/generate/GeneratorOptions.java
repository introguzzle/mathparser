package ru.generate;

import ru.common.Options;

public abstract class GeneratorOptions implements Options {

    public static final int INCLUDE_FLOATS = 1;
    public static final int INCLUDE_INTEGERS = 1 << 1;
    public static final int ONLY_DEFAULT_VARIABLE = 1 << 2;

    private int flags;

    public String defaultVariable = "x";

    public String[] operators = new String[]{
        "+", "-", "*", "/", "**"
    };

    public int min = 0;
    public int max = 100;

    public int maxFloating = 3;

    public int maxLength = 10;

    public int maxDepth = 2;

    public int maxAdditionalVariadicArgs = 3;

    public Distribution distribution = new Distribution();

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
}
