package se.doverfelt.effects;

import se.doverfelt.worlds.WorldPongz;

/**
 * Created by rickard on 2016-02-03.
 */
public interface Effect {
    void update(WorldPongz world, float delta);
    void create(WorldPongz world, String name);
    String getName();
    String getEffectType();
    int getWeight();
    boolean isTimed();
    long totalTime();
    long currentTime();
    boolean isSided();
    boolean isLeft();
}

