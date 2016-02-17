package se.doverfelt.effects;

import se.doverfelt.PongzStart;

import java.util.Random;

/**
 * Created by rickard on 2016-02-03.
 */
public class EffectRandomColor implements Effect{
    @Override
    public void update(PongzStart world, int delta) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getEffectType() {
        return "randomColor";
    }

    @Override
    public int getWeight() {
        return 45;
    }

    @Override
    public void create(PongzStart world, String name) {
        Random r = new Random();
        world.setColor(r.nextFloat(), r.nextFloat(), r.nextFloat());
        world.removeEffect(name);
    }
}
