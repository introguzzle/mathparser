package ru.introguzzle.mathparser.common;

import ru.introguzzle.mathparser.symbol.*;

import java.util.Optional;
import java.util.Set;

public interface Context {
    Context getParent();
    void setParent(Context parent);

    MutableSymbolList<MutableSymbol> getSymbols();
    Optional<MutableSymbol> getSymbol(String name);
    void addSymbol(MutableSymbol symbol);
    boolean removeSymbol(String name);
    boolean removeSymbol(MutableSymbol symbol);
    void setSymbols(MutableSymbolList<? extends MutableSymbol> symbols);

    Set<String> getNames();
    boolean contains(CharSequence name);
}
