package se.doverfelt.effects;

import se.doverfelt.PongzStart;

import java.util.Random;

/**
 * Created by rickard on 2016-02-03.
 */
public class EffectRandomColor implements Effect{
    @Override
    public void update(PongzStart world, float delta) {

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
        if(PongzStart.isFlashbanged){
            Random r = new Random();
            for (String s : world.getEffects().keySet()) {
                if (s.contains(PongzStart.effectHandler.getEffectType(EffectFlashbang.class))) {
                    ((EffectFlashbang)world.getEffects().get(s)).setColors(r.nextFloat(), r.nextFloat(), r.nextFloat());
                }
            }
            world.removeEffect(name);
        }else {
            Random r = new Random();
            world.setColor(r.nextFloat(), r.nextFloat(), r.nextFloat());
            world.removeEffect(name);
        }
    }
}
