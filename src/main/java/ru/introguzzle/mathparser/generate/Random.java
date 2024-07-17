package ru.introguzzle.mathparser.generate;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

class Random {

    /**
     *
     * @param min Minimal (included)
     * @param max Maximal (included)
     * @return Random integer
     */
    static int randomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    static float randomFloat(float min, float max) {
        return ThreadLocalRandom.current().nextFloat(min, max + 1.0f);
    }

    static <T> T pickFromMap(Map<?, ? extends T> map) {
        Object key = pickFromCollection(map.keySet());
        return map.get(key);
    }

    static <T> T pickFromCollection(Collection<? extends T> collection) {
        return collection.stream()
                .skip(Random.randomInteger(0, collection.size() - 1))
                .findFirst()
                .orElse(null);
    }
}
