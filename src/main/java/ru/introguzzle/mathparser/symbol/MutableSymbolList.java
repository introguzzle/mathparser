package ru.introguzzle.mathparser.symbol;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.NoSuchNameException;
import ru.introguzzle.mathparser.common.NotUniqueNamingException;

import java.util.*;

public abstract class MutableSymbolList<T extends MutableSymbol> {

    private final Map<String, T> map = new HashMap<>();

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
        if (map.containsKey(item.getName())) {
            throw new NotUniqueNamingException(item.getName(), map.keySet());
        }
    }

    public Optional<? extends T> find(@NotNull String name) {
        return Optional.ofNullable(map.getOrDefault(name, null));
    }

    public void setValue(@NotNull String name, double value) {
        find(name)
                .orElseThrow(() -> new NoSuchNameException(name, map.keySet()))
                .setValue(value);
    }

    public void add(T item) {
        checkUnique(item);
        map.put(item.getName(), item);
    }

    public void addAll(MutableSymbolList<? extends T> items) {
        map.putAll(items.map);
    }

    public void clear() {
        map.clear();
    }

    public boolean remove(T item) {
        return map.remove(item.getName()) != null;
    }

    public boolean remove(String name) {
        return map.remove(name) != null;
    }

    public Map<String, T> getMap() {
        return map;
    }

    public Set<String> getNames() {
        return new HashSet<>(map.keySet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableSymbolList<?> that = (MutableSymbolList<?>) o;
        return size() == that.size() && Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    public int size() {
        return map.size();
    }
}
