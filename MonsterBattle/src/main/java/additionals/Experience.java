package additionals;

public final class Experience {

    private int level;
    private long totalExperience;
    private long experienceBucket;
    private long experienceBucketLimit;

    private final Runnable levelUpAction;

    public Experience(Runnable levelUpAction) {
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
     */
    public void gainExperience(long exp) {
        if (!canGain(exp)) return;
        totalExperience += exp;
        provideExperience(exp);
    }

    /**
     * Проверяет возможность накопить новый опыт (граничные случаи).
     * <p>
     * Вернет true, если уровень не последний и <code>totalExperience</code> можно увеличить на размер опыта
     */
    private boolean canGain(long exp) {
        return level < Integer.MAX_VALUE && Long.MAX_VALUE - exp > totalExperience;
    }

    private void provideExperience(long exp) {
        if (experienceBucket + exp >= experienceBucketLimit) {
            nextLevel(Math.abs(experienceBucketLimit - (experienceBucket + exp)));
        } else {
            experienceBucket += exp;
        }
    }

    private void nextLevel(long remainExp) {
        experienceBucketLimit = (long) (experienceBucketLimit * 1.2);
        experienceBucket = 0;
        level++;

        if (levelUpAction != null) {
            levelUpAction.run();
        }
        provideExperience(remainExp);
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
