package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.symbol.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 */
public class NamingContext implements Context {
    private final MutableSymbolList<MutableSymbol> symbols = new MutableSymbolList<>() {};

    private Context parent;

    private final Set<String> names = new HashSet<>();

    public NamingContext() {

    }

    public NamingContext(Context parent) {
        if (parent != null) {
            this.parent = parent;

            symbols.getItems().addAll(parent.getSymbols().getItems());
            names.addAll(parent.getNames());
        }
    }

    public NamingContext(@NotNull MutableSymbolList<? extends MutableSymbol> symbols) {
        names.addAll(symbols.getNames());
        this.symbols.getItems().addAll(symbols.getItems());
    }

    @Override
    public MutableSymbolList<MutableSymbol> getSymbols() {
        return symbols;
    }

    @Override
    public Context getParent() {
        return parent;
    }

    @Override
    public void addSymbol(MutableSymbol symbol) {
        String name;
        boolean added = names.add(name = symbol.getName());
        if (!added) {
            throw new NotUniqueNamingException(name, names);
        }

        symbols.add(symbol);
    }

    @Override
    public boolean removeSymbol(String name) {
        symbols.getItems().removeIf(symbol -> symbol.nameEquals(name));
        return names.remove(name);
    }

    @Override
    public boolean removeSymbol(MutableSymbol symbol) {
        return symbols.remove(symbol) && names.remove(symbol.getName());
    }

    @Override
    public Optional<? extends MutableSymbol> getSymbol(String name) {
        Optional<? extends MutableSymbol> symbol = symbols.find(name);

        if (symbol.isEmpty() && parent != null) {
            return parent.getSymbol(name);
        }

        return symbol;
    }

    @Override
    public Set<String> getNames() {
        return new HashSet<>(names);
    }

    @Override
    public void setParent(Context parent) {
        this.parent = parent;
    }

    @Override
    public void setSymbols(MutableSymbolList<? extends MutableSymbol> symbols) {
        this.symbols.clear();
        this.symbols.addAll(symbols);
    }

    @Override
    public boolean contains(CharSequence name) {
        return names.contains(name.toString());
    }
}
