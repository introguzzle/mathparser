package ru.introguzzle.mathparser.unit.length;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.math.Number;
import ru.introguzzle.mathparser.unit.AbstractUnit;
import ru.introguzzle.mathparser.unit.measure.LengthMeasure;

import java.util.Set;

public abstract class LengthUnit extends AbstractUnit<LengthMeasure, LengthUnit> {
    public LengthUnit(Set<String> names, String main) {
        super(names, main);
    }

    /**
     * Метод для конвертации текущей единицы в другую единицу длины.
     *
     * @param unit Целевая единица измерения
     * @return Значение, преобразованное в целевую единицу измерения
     */
    @Override
    public double transform(double value, LengthUnit unit) {
        double valueInMeters = toMeters(value);
        return unit.fromMeters(valueInMeters);
    }

    public static void main(String[] args) {
        Number number = new Number(1);
        System.out.println(number.transform(KilometerUnit.get(), MeterUnit.get()));
    }

    /**
     * Преобразование текущей единицы в метры.
     *
     * @param value Значение в текущих единицах
     * @return Значение в метрах
     */
    protected abstract double toMeters(double value);

    /**
     * Преобразование из метров в текущую единицу.
     *
     * @param value Значение в метрах
     * @return Значение в текущих единицах
     */
    protected abstract double fromMeters(double value);

    @Override
    public @NotNull LengthMeasure getMeasure() {
        return LengthMeasure.LENGTH;
    }
}
