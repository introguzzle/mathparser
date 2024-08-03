package ru.introguzzle.mathparser.symbol;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.NoSuchNameException;
import ru.introguzzle.mathparser.common.NotUniqueNamingException;

import java.util.*;

public abstract class MutableSymbolList<T extends MutableSymbol> {

    private final Map<String, T> values = new HashMap<>();

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

    private void checkUnique(@NotNull T item) {
        if (values.containsKey(item.getName())) {
            throw new NotUniqueNamingException(item.getName(), values.keySet());
        }
    }

    public Optional<T> find(@NotNull String name) {
        return Optional.ofNullable(values.getOrDefault(name, null));
    }

    public void setValue(@NotNull String name, double value) {
        find(name)
                .orElseThrow(() -> new NoSuchNameException(name, values.keySet()))
                .setValue(value);
    }

    public void add(T item) {
        checkUnique(item);
        values.put(item.getName(), item);
    }

    public void addAll(MutableSymbolList<? extends T> items) {
        values.putAll(items.values);
    }

    public void clear() {
        values.clear();
    }

    public boolean remove(T item) {
        return values.remove(item.getName()) != null;
    }

    public boolean remove(String name) {
        return values.remove(name) != null;
    }

    public Map<String, T> getValues() {
        return values;
    }

    public Set<String> getNames() {
        return new HashSet<>(values.keySet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableSymbolList<?> that = (MutableSymbolList<?>) o;
        return size() == that.size() && Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public String toString() {
        return values.toString();
    }

    public int size() {
        return values.size();
    }
}
