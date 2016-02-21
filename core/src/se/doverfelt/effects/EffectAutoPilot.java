package se.doverfelt.effects;

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

    @Override
    public void update(WorldPongz world, float delta) {
        paddle.setY((ball.getY()-paddle.getHeight()/2f));
        if (System.currentTimeMillis() - timeStamp > 10000) {
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
        return 5;
    }
}
