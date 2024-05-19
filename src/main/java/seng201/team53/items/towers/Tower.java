package seng201.team53.items.towers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.items.Item;
import seng201.team53.items.ResourceType;
import seng201.team53.items.upgrade.Upgradeable;

public class Tower implements Item<Tower>, Upgradeable {
    private final TowerType type;
    private final BooleanProperty brokenProperty = new SimpleBooleanProperty(false);
    private final LongProperty lastGeneratePropertyTime = new SimpleLongProperty(System.currentTimeMillis());
    private double reloadSpeedModifier = 1;
    private int xpLevel = 0;

    protected Tower(TowerType type) {
        this.type = type;
    }

    public TowerType getPurchasableType() {
        return type;
    }

    public BooleanProperty getBrokenProperty() {
        return brokenProperty;
    }

    public boolean isBroken() {
        return brokenProperty.get();
    }

    public void setBroken(boolean broken) {
        brokenProperty.set(broken);
    }

    public LongProperty getLastGenerateTimeProperty() {
        return lastGeneratePropertyTime;
    }

    public long getLastGenerateTime() {
        return lastGeneratePropertyTime.get();
    }

    public void setLastGenerateTime(long time) {
        lastGeneratePropertyTime.set(time);
    }

    public int getXpLevel() {
        return xpLevel;
    }

    public void incrementXpLevel(int amount) {
        this.xpLevel += amount;
    }

    public void addReloadSpeedModifier() {
        reloadSpeedModifier += 0.2;
    }

    public void resetReloadSpeedModifier() {
        reloadSpeedModifier = 1;
    }

    /**
     * @return True if this tower is ready to generate now, or false if it needs more time to reload.
     */
    public boolean canGenerate() {
        if (isBroken())
            return false;

        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        long reloadSpeed = type.getReloadSpeed().toMillis();
        reloadSpeed /= (long)difficulty.getTowerReloadModifier();
        reloadSpeed /= (long)reloadSpeedModifier;

        long deltaTime = System.currentTimeMillis() - getLastGenerateTime();
        if (deltaTime < reloadSpeed)
            return false;

        setLastGenerateTime(System.currentTimeMillis());
        return true;
    }

    public interface Type {
        TowerType LUMBER_MILL = new TowerType("Lumber Mill Tower",
            "A Lumber Mill produces wood",
            ResourceType.WOOD,
            100,
            1,
            java.time.Duration.ofSeconds(1));


        TowerType MINE = new TowerType("Mine Tower",
            "A Mine produces ores",
            ResourceType.STONE,
            120,
            1,
            java.time.Duration.ofSeconds(1));

        TowerType QUARRY = new TowerType("Quarry Tower",
            "A Quarry produces stone",
            ResourceType.ORE,
            150,
            1,
            java.time.Duration.ofSeconds(1));

        TowerType WIND_MILL = new TowerType("Windmill Tower",
            "A wind mill produces energy",
            ResourceType.ENERGY,
            200,
            1,
            java.time.Duration.ofSeconds(1));
    }
}