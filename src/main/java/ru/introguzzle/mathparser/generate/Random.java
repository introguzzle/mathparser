package ru.introguzzle.mathparser.generate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.common.math.Radix;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public final class Random {

    /**
     *
     * @param min Minimal (included)
     * @param max Maximal (included)
     * @return Random integer
     */
    public static int getRandomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static float getRandomFloat(float min, float max) {
        return ThreadLocalRandom.current().nextFloat(min, max + 1.0f);
    }

    @NotNull public static String getRandomInteger(int min, int max, @NotNull Radix radix) {
        int number = getRandomInteger(min, max);
        return Integer.toString(number, (int) radix.getBase());
    }

    @NotNull public static String getRandomFloat(float min, float max, @NotNull Radix radix) {
        float number = getRandomFloat(min, max);
        String integer = Integer.toString((int) number, (int) radix.getBase());

        String decimal1 = Integer.toString((int) getRandomFloat(min, max), (int) radix.getBase());
        String decimal2 = Integer.toString((int) getRandomFloat(min, max), (int) radix.getBase());

        return integer + "." + decimal1 + decimal2;
    }

    @Nullable public static <T> T fromMap(@NotNull Map<?, ? extends T> map) {
        Object key = fromCollection(map.keySet());
        return map.get(key);
    }

    @Nullable public static <T> T fromMap(@NotNull Map<?, ? extends T> map,
                                          @NotNull Predicate<? super T> predicate) {
        List<? extends T> list = map.values().stream().filter(predicate).toList();
        return fromCollection(list);
    }

    @Nullable public static <T> T fromCollection(@NotNull Collection<? extends T> collection) {
        return collection.stream()
                .skip(Random.getRandomInteger(0, collection.size() - 1))
                .findFirst()
                .orElse(null);
    }
}
