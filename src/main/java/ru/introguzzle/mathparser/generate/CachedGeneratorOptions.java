package ru.introguzzle.mathparser.generate;

public class CachedGeneratorOptions extends GeneratorOptions {
    public CachedGeneratorOptions() {
        setMaxFloating(1);
        setFlags(GeneratorOptions.INCLUDE_FLOATS);
    }
}
