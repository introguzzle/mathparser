package ru.contract.generator;

public abstract class GeneratorOptions {
    private int flags;
    public static final int INCLUDE_FLOATS = 1;
    public static final int INCLUDE_INTEGERS = 1 << 1;

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

    public boolean match(int flag) {
        return (this.getFlags() & flag) == flag;
    }

    public boolean matchAll(int... flags) {
        boolean result = true;
        for (int flag: flags) {
            result &= match(flag);
        }

        return result;
    }

    public boolean matchAny(int... flags) {
        boolean result = false;
        for (int flag: flags) {
            result |= match(flag);
        }

        return result;
    }

    public void setFlags(int... flags) {
        for (int flag: flags) {
            this.flags |= flag;
        }
    }

    public int getFlags() {
        return this.flags;
    }
}
