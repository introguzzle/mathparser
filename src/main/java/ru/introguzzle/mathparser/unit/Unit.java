package ru.introguzzle.mathparser.unit;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;
import ru.introguzzle.mathparser.tokenize.token.type.Type;
import ru.introguzzle.mathparser.tokenize.token.type.UnitType;
import ru.introguzzle.mathparser.unit.measure.Measure;

/**
 * The Unit interface represents a unit of measurement for a specific measure (e.g., length, mass).
 *
 * @param <M> The type of measure (e.g., LengthMeasure, MassMeasure).
 * @param <U> The specific unit type that extends this interface (e.g., MeterUnit, PoundUnit).
 */
public interface Unit<M extends Measure, U extends Unit<M, U>> extends MultiNameable {

    /**
     * Returns the type of the unit, which is used for tokenization and parsing.
     *
     * @return The Type representing the unit type.
     */
    @Override
    default @NotNull Type type() {
        return UnitType.UNIT;
    }

    /**
     * Returns the measure associated with this unit (e.g., LENGTH, MASS).
     *
     * @return The measure associated with this unit.
     */
    @NotNull M getMeasure();

    /**
     * Converts a given value from this unit to another unit of the same measure.
     *
     * @param value The value in the current unit.
     * @param unit  The target unit to which the value should be converted.
     * @return The converted value in the target unit.
     */
    double transform(double value, U unit);
}
