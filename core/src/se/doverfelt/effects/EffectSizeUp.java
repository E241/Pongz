package se.doverfelt.effects;

import se.doverfelt.Start;
import se.doverfelt.worlds.WorldPongz;
import se.doverfelt.entities.EntityBall;
import se.doverfelt.entities.EntityPaddle;

/**
 * Created by rickard on 2016-02-04.
 */
public class EffectSizeUp implements Effect {

    private EntityPaddle paddle;
    private String name;
    private long timestamp;
    private float heightAdd = 0, origHeight;

    @Override
    public void update(WorldPongz world, float delta) {
        long current = System.currentTimeMillis();
        if (current - timestamp < 1000) {
            heightAdd += 100f * delta;
            paddle.setHeight(Math.min(origHeight + heightAdd, origHeight + 15));
            if (origHeight + heightAdd < origHeight + 15) paddle.moveY(-(100f*delta)/2f);
        }

        if (current - timestamp > 10000 && current - timestamp < 11000) {
            if (heightAdd > 15) heightAdd = 15;
            heightAdd -= 100f * delta;
            paddle.setHeight(Math.max(origHeight, origHeight + heightAdd));
            if (origHeight + heightAdd > origHeight) paddle.moveY((100f*delta)/2f);
        }

        if (current - timestamp > 11000) {
            paddle.setHeight(paddle.getOrigHeight());
            world.removeEffect(name);
        }
    }

    @Override
    public void create(WorldPongz world, String name) {
        this.paddle = ((EntityBall) WorldPongz.entities.get("ball")).getLastPaddle();

        if (paddle == null) {
            world.removeEffect(name);
            return;
        } else if (paddle.getHeight() != paddle.getOrigHeight()) {
            world.removeEffect(name);
            return;
        }
        this.name = name;
        timestamp = System.currentTimeMillis();
        origHeight = paddle.getOrigHeight();
        heightAdd = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEffectType() {
        return "sizeUp";
    }

    @Override
    public int getWeight() {
        return Start.getPreferences().getInteger(getEffectType()+"Chance");
    }

    @Override
    public boolean isTimed() {
        return true;
    }

    @Override
    public long totalTime() {
        return 11000;
    }

    @Override
    public long currentTime() {
        return (System.currentTimeMillis() - timestamp);
    }

    @Override
    public boolean isSided() {
        return paddle != null;
    }

    @Override
    public boolean isLeft() {
        return paddle.isLeft();
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof EffectSizeUp)) return false;
        EffectSizeUp obj = (EffectSizeUp) o;
        return obj.name.equals(this.name);
    }
}
