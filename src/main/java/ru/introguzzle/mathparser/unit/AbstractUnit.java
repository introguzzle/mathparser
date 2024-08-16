package ru.introguzzle.mathparser.unit;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.unit.measure.Measure;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractUnit<M extends Measure, U extends AbstractUnit<M, U>>
        implements Unit<M, U> {
    private final Set<String> names = new HashSet<>();
    private final String main;

    public AbstractUnit(Set<String> names, String main) {
        this.names.addAll(names);
        this.main = main;
    }

    @Override
    public @NotNull String getName() {
        return main;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUnit<?, ?> that = (AbstractUnit<?, ?>) o;
        return Objects.equals(main, that.main);
    }

    @Override
    public String toString() {
        return describe() + "{" +
                "name='" + main + '\'' +
                '}';
    }

    @Override
    public @NotNull Set<String> getNames() {
        return names;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(main);
    }
}
