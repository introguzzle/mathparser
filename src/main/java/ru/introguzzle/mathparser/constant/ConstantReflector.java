package ru.introguzzle.mathparser.constant;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Reflector;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;

import java.util.*;

public class ConstantReflector {
    private static final Map<String, ImmutableSymbol> CONSTANTS = new HashMap<>();
    private static final String PACKAGE = "ru.introguzzle.mathparser.constant";
    private static final Reflector<ImmutableSymbol> REFLECTOR = new Reflector<>();

    @NotNull
    private static Map<String, ImmutableSymbol> load() {
        return REFLECTOR.getInstances(ImmutableSymbol.class, CONSTANTS, PACKAGE);
    }

    public static Map<String, ImmutableSymbol> get() {
        if (ConstantReflector.CONSTANTS.isEmpty()) {
            ConstantReflector.CONSTANTS.putAll(ConstantReflector.load());
        }

        return ConstantReflector.CONSTANTS;
    }
}
