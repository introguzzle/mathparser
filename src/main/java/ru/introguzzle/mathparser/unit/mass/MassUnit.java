package ru.introguzzle.mathparser.unit.mass;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.unit.AbstractUnit;
import ru.introguzzle.mathparser.unit.measure.MassMeasure;

import java.util.Set;

public abstract class MassUnit extends AbstractUnit<MassMeasure, MassUnit> {
    public MassUnit(Set<String> names, String main) {
        super(names, main);
    }

    @Override
    public @NotNull MassMeasure getMeasure() {
        return MassMeasure.MASS;
    }

    /**
     * Метод для конвертации текущей единицы массы в другую единицу массы.
     *
     * @param unit Целевая единица измерения массы
     * @return Значение, преобразованное в целевую единицу измерения
     */
    @Override
    public double transform(double value, MassUnit unit) {
        double valueInKilograms = toKilograms(value);
        return unit.fromKilograms(valueInKilograms);
    }

    /**
     * Преобразование текущей единицы в килограммы.
     *
     * @param value Значение в текущих единицах
     * @return Значение в килограммах
     */
    protected abstract double toKilograms(double value);

    /**
     * Преобразование из килограммов в текущую единицу.
     *
     * @param value Значение в килограммах
     * @return Значение в текущих единицах
     */
    protected abstract double fromKilograms(double value);
}

