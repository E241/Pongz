package se.doverfelt.effects;

import se.doverfelt.PongzStart;

/**
 * Created by rickard on 2016-02-03.
 */
public interface Effect {
    void update(PongzStart world, int delta);
    void create(PongzStart world, String name);
    String getName();
}

