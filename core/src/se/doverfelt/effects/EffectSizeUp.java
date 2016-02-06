package se.doverfelt.effects;

import se.doverfelt.PongzStart;
import se.doverfelt.entities.Entity;
import se.doverfelt.entities.EntityPaddle;

/**
 * Created by rickard on 2016-02-04.
 */
public class EffectSizeUp implements Effect {

    private EntityPaddle paddle;
    private String name;
    private long timestamp;
    private float heightAdd = 0, origHeight;

    public EffectSizeUp(String name, EntityPaddle paddle) {
        this.name = name;
        this.paddle = paddle;
    }

    @Override
    public void update(PongzStart world, int delta) {
        long current = System.currentTimeMillis();
        if (current - timestamp < 1000) {
            heightAdd += 0.1f * delta;
            paddle.setHeight(Math.min(origHeight + heightAdd, origHeight + 15));
            if (origHeight + heightAdd < origHeight + 15) paddle.moveY(-(0.1f*delta)/2f);
        }

        if (current - timestamp > 10000 && current - timestamp < 11000) {
            if (heightAdd > 15) heightAdd = 15;
            heightAdd -= 0.1f * delta;
            paddle.setHeight(Math.max(origHeight, origHeight + heightAdd));
            if (origHeight + heightAdd > origHeight) paddle.moveY((0.1f*delta)/2f);
        }

        if (current - timestamp > 11000) world.removeEffect(name);
    }

    @Override
    public void create(PongzStart world) {
        if (paddle == null) {
            world.removeEffect(name);
            return;
        } else if (paddle.getHeight() != paddle.getOrigHeight()) {
            world.removeEffect(name);
            return;
        }
        timestamp = System.currentTimeMillis();
        origHeight = paddle.getOrigHeight();
    }
}
