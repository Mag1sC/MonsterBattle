package utils;

import java.util.Random;
import java.util.random.RandomGenerator;

public class RandomValue {

    /**
     * Возвращает случайное целочисленное значение [from ; to]
     * */
    public static int getRandomValue(int from, int to) {
        return Random.from(RandomGenerator.getDefault()).nextInt(from, to + 1);
    }
}
