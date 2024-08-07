package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.symbol.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 */
public class NamingContext<N extends Number> implements Context<N> {
    private final MutableSymbolList<MutableSymbol<N>, N> symbols = new MutableSymbolList<>() {};
    private final Set<String> names = new HashSet<>();
    private Context<N> parent;

    public NamingContext() {

    }

    public NamingContext(Context<N> parent) {
        registerParent(parent);
    }

    public NamingContext(@NotNull MutableSymbolList<? extends MutableSymbol<N>, N> symbols) {
        names.addAll(symbols.getNames());
        this.symbols.addAll(symbols);
    }

    private void registerParent(Context<N> parent) {
        if (parent != null) {
            this.parent = parent;
            this.names.addAll(parent.getNames());
            this.symbols.addAll(parent.getSymbols());
        }
    }

    @Override
    public MutableSymbolList<MutableSymbol<N>, N> getSymbols() {
        return symbols;
    }

    @Override
    public Context<N> getParent() {
        return parent;
    }

    @Override
    public void addSymbol(MutableSymbol<N> symbol) {
        String name;
        boolean added = names.add(name = symbol.getName());
        if (!added) {
            throw new NotUniqueNamingException(name, names);
        }

        symbols.add(symbol);
    }

    @Override
    public boolean removeSymbol(String name) {
        symbols.remove(name);
        return names.remove(name);
    }

    @Override
    public boolean removeSymbol(MutableSymbol<N> symbol) {
        return symbols.remove(symbol) & names.remove(symbol.getName());
    }

    @Override
    public Optional<MutableSymbol<N>> getSymbol(String name) {
        Optional<MutableSymbol<N>> optional = symbols.find(name);

        if (optional.isEmpty() && parent != null) {
            return parent.getSymbol(name);
        }

        return optional;
    }

    @Override
    public Set<String> getNames() {
        Set<String> names = this.names;
        Context<N> parent = this.parent;

        while (parent != null) {
            names.addAll(parent.getNames());
            parent = parent.getParent();
        }

        return names;
    }

    @Override
    public void setParent(Context<N> parent) {
        registerParent(parent);
    }

    @Override
    public void setSymbols(MutableSymbolList<? extends MutableSymbol<N>, N> symbols) {
        this.symbols.clear();
        this.symbols.addAll(symbols);
    }

    @Override
    public String toString() {
        return "NamingContext{" +
                "symbols=" + symbols +
                ", names=" + names +
                ", parent=" + parent +
                '}';
    }

    @Override
    public boolean contains(CharSequence name) {
        if (parent != null) {
            return parent.contains(name) || names.contains(name.toString());
        }

        return names.contains(name.toString());
    }
}
