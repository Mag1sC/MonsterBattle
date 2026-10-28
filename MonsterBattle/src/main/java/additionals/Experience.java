package additionals;

import java.util.List;
import java.util.concurrent.Callable;

public final class Experience {

    private int level;
    private long totalExperience;
    private long experienceBucket;
    private long experienceBucketLimit;

    private final Callable<Integer> levelUpAction;

    public Experience(Callable<Integer> levelUpAction) {
        level = 0;
        totalExperience = 0;
        experienceBucket = 0;
        experienceBucketLimit = 100;

        this.levelUpAction = levelUpAction;
    }

    public Experience(int level) {
        this(null);
        this.level = level;
    }

    /**
     * Накапливает опыт в пределах текущего уровня.
     * <p>
     * Если выходит за пределы уровня, повышает его и повторяет операцию.
     * @param levelUps принимает аккумулятор повышений. Сохраняет коды всех повышений,
     *                которые были получены во время этой итерации
     */
    public void gainExperience(long exp, List<Integer> levelUps) throws Exception {
        if (!canGain(exp)) return;
        totalExperience += exp;
        provideExperience(exp, levelUps);
    }

    /**
     * Проверяет возможность накопить новый опыт (граничные случаи).
     * <p>
     * Вернет true, если уровень не последний и <code>totalExperience</code> можно увеличить на размер опыта
     */
    private boolean canGain(long exp) {
        return level < Integer.MAX_VALUE && Long.MAX_VALUE - exp > totalExperience;
    }

    private void provideExperience(long exp, List<Integer> levelUps) throws Exception {
        if (experienceBucket + exp >= experienceBucketLimit) {
            nextLevel(Math.abs(experienceBucketLimit - (experienceBucket + exp)), levelUps);
        } else {
            experienceBucket += exp;
        }
    }

    private void nextLevel(long remainExp, List<Integer> levelUps) throws Exception {
        experienceBucketLimit = (long) (experienceBucketLimit * 1.2);
        experienceBucket = 0;
        level++;

        if (levelUpAction != null) {
            levelUps.add(levelUpAction.call());
        }
        provideExperience(remainExp, levelUps);
    }

    public int getLevel() {
        return level;
    }

    public long getExperienceBucket() {
        return experienceBucket;
    }

    public long getExperienceBucketLimit() {
        return experienceBucketLimit;
    }
}
