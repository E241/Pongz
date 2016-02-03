package se.doverfelt.effects;

import se.doverfelt.PongzStart;

import java.util.Random;

/**
 * Created by rickard on 2016-02-03.
 */
public class EffectRandomColor implements Effect{
    private String name;

    public EffectRandomColor(String name) {
        this.name = name;
    }

    @Override
    public void update(PongzStart world, int delta) {

    }

    @Override
    public void create(PongzStart world) {
        Random r = new Random();
        world.setColor(r.nextFloat(), r.nextFloat(), r.nextFloat());
        world.removeEffect(name);
    }
}
