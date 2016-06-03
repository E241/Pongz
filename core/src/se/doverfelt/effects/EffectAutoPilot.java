package se.doverfelt.effects;

import se.doverfelt.Start;
import se.doverfelt.worlds.World;
import se.doverfelt.worlds.WorldPongz;
import se.doverfelt.entities.EntityBall;
import se.doverfelt.entities.EntityPaddle;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-10
 *         Filnamn: EffectAutoPilot.java
 */
public class EffectAutoPilot implements Effect {
    private String name;
    private EntityPaddle paddle;
    private EntityBall ball;
    private long timeStamp;
    private World world;

    @Override
    public void update(WorldPongz world, float delta) {
        paddle.setY((ball.getY()-paddle.getHeight()/2f));
        if (System.currentTimeMillis() - timeStamp > 5000) {
            world.removeEffect(name);
        }
    }

    @Override
    public void create(WorldPongz world, String name) {
        this.name = name;
        ball = (EntityBall) WorldPongz.entities.get("ball");
        paddle = ball.getLastPaddle();
        if (paddle == null) {
            world.removeEffect(name);
            return;
        }
        timeStamp = System.currentTimeMillis();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEffectType() {
        return "autoPilot";
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
        return 5000;
    }

    @Override
    public long currentTime() {
        return (System.currentTimeMillis() - timeStamp);
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
        if (!(o instanceof EffectAutoPilot)) return false;
        EffectAutoPilot obj = (EffectAutoPilot) o;
        return obj.name == this.name;
    }
}
