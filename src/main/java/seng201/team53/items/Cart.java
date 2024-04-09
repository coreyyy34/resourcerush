package seng201.team53.items;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import seng201.team53.App;
import seng201.team53.game.Tickable;

import java.util.EnumSet;

public class Cart implements Tickable {
    private final int maxCapacity;
    private final float velocity;
    private final EnumSet<ResourceType> acceptedResources;
    private final int spawnAfterTicks; // so we can spawn them after each other - don't all render on top of each other
    private int x;
    private int y;
    private int currentCapacity;
    private int lifetimeTicks = 0;
    private PathTransition pathTransition;
    private boolean completedPath = false;

    public Cart(int maxCapacity, float velocity, EnumSet<ResourceType> acceptedResources, int spawnAfterTicks) {
        this.maxCapacity = maxCapacity;
        this.velocity = velocity;
        this.acceptedResources = acceptedResources;
        this.spawnAfterTicks = spawnAfterTicks;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCapacity() {
        return currentCapacity;
    }

    public boolean isFull() {
        return this.getCapacity() >= this.getMaxCapacity();
    }

    public EnumSet<ResourceType> getAcceptedResources() {
        return acceptedResources;
    }

    public PathTransition getPathTransition() {
        return pathTransition;
    }

    public boolean isCompletedPath() {
        return completedPath;
    }

    @Override
    public void tick() {
        if (spawnAfterTicks == lifetimeTicks) {
            if (!completedPath) {
                var gameEnvironment = App.getApp().getGameEnvironment();
                var map = gameEnvironment.getRound().getMap();
                var polylinePath = map.getPolylinePath();
                var imageView = new ImageView(gameEnvironment.getMapLoader().getCartImage());
                imageView.setX(polylinePath.getPoints().get(1));
                imageView.setY(polylinePath.getPoints().get(0));
                gameEnvironment.getWindow().getController().test.getChildren().add(imageView);
                pathTransition = new PathTransition();
                pathTransition.setNode(imageView);
                pathTransition.setDuration(Duration.seconds(10));
                pathTransition.setPath(polylinePath);
                pathTransition.setInterpolator(Interpolator.LINEAR);
                pathTransition.play();
                pathTransition.setOnFinished(event -> {
                    pathTransition = null;
                    completedPath = true;
                    gameEnvironment.getWindow().getController().test.getChildren().remove(imageView);
                });
            }
        }
        lifetimeTicks++;
    }
}
