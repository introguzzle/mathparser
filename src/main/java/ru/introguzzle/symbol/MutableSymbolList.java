package ru.introguzzle.symbol;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public abstract class MutableSymbolList<T extends MutableSymbol>
        implements Iterable<T> {

    private final List<T> items = new ArrayList<>();
    private final Set<String> names = new HashSet<>();

    public MutableSymbolList() {
    }

    @SafeVarargs
    public MutableSymbolList(@NotNull T... items) {
        for (T item : items) {
            this.add(item);
        }
    }

    public MutableSymbolList(@NotNull List<? extends T> items) {
        for (T item : items) {
            this.add(item);
        }
    }

    protected void checkUnique(@NotNull T item) {
        if (names.contains(item.getName())) {
            throw new MutableSymbolListException("Unique constrain violation");
        }
    }

    public Optional<? extends T> find(@NotNull String name) {
        for (T item : items) {
            if (item.getName().equals(name)) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

    public void setValue(String name, double value) {
        find(name)
                .orElseThrow(() -> new MutableSymbolListException("No such element with following name: " + name))
                .setValue(value);
    }

    public void add(T item) {
        this.checkUnique(item);
        items.add(item);
        names.add(item.getName());
    }

    public boolean remove(T item) {
        return items.remove(item) && names.remove(item.getName());
    }

    public boolean remove(String name) {
        return items.removeIf(Symbol.match(name)) && names.remove(name);
    }

    public List<T> getItems() {
        return items;
    }

    public Set<String> getNames() {
        return names;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableSymbolList<?> that = (MutableSymbolList<?>) o;
        return Objects.equals(items, that.items) && Objects.equals(names, that.names);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, names);
    }

    @Override
    public String toString() {
        return items.toString();
    }

    public int size() {
        return items.size();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

    public Stream<T> stream() {
        return items.stream();
    }

    public Stream<T> parallelStream() {
        return items.parallelStream();
    }
}
