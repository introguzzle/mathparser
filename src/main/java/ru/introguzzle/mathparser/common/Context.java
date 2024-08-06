package ru.introguzzle.mathparser.common;

import ru.introguzzle.mathparser.symbol.*;

import java.util.Optional;
import java.util.Set;

public interface Context<N extends Number> {
    Context<N> getParent();
    void setParent(Context<N> parent);

    MutableSymbolList<MutableSymbol<N>, N> getSymbols();
    Optional<MutableSymbol<N>> getSymbol(String name);
    void addSymbol(MutableSymbol<? extends Number> symbol);
    boolean removeSymbol(String name);
    boolean removeSymbol(MutableSymbol<N> symbol);
    void setSymbols(MutableSymbolList<? extends MutableSymbol<N>, N> symbols);

    Set<String> getNames();
    boolean contains(CharSequence name);
}
