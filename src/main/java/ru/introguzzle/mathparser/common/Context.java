package ru.introguzzle.mathparser.common;

import ru.introguzzle.mathparser.symbol.*;

import java.util.Optional;
import java.util.Set;

public interface Context {
    MutableSymbolList<MutableSymbol> getSymbols();

    Context getParent();

    void addSymbol(MutableSymbol symbol);


    boolean removeSymbol(String name);

    boolean removeSymbol(MutableSymbol symbol);

    Optional<? extends MutableSymbol> getSymbol(String name);

    Set<String> getNames();

    void setParent(Context parent);

    void setSymbols(MutableSymbolList<? extends MutableSymbol> symbols);

    boolean contains(CharSequence name);
}
