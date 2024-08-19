package ru.introguzzle.mathparser.unit.measure;

/**
 * Interface for representing physical measures.
 * <p>
 * This interface is used to define types of physical measures,
 * such as length, mass, time, etc.
 * Implementations of this interface can be used to group units of measurement.
 * </p>
 */
public interface Measure {
    /**
     * Measure for angles (degrees and radians)
     */
    enum AngleMeasure implements Measure {
        ANGLE
    }

    /**
     * Measure for length
     */
    enum LengthMeasure implements Measure {
        LENGTH
    }

    /**
     * Measure for mass
     */
    enum MassMeasure implements Measure {
        MASS
    }

    /**
     * Measure for time
     */
    enum TimeMeasure implements Measure {
        TIME;
    }

    enum TemperatureMeasure implements Measure {
        TEMPERATURE
    }

    /**
     * Returns the ordinal number of this measure.
     * <p>
     * This number can be used to compare different measures,
     * order them, or store them in sequences.
     * </p>
     *
     * @return the ordinal number of this measure
     */
    int ordinal();

    /**
     * Returns the name of this measure.
     * <p>
     * The name should be unique for each implementation and can be used
     * to identify the measure.
     * </p>
     *
     * @return the name of this measure
     */
    String name();

    default String describe() {
        return name() + " <" + getClass().getSimpleName() + ">";
    }
}
