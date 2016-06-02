package se.doverfelt.effects;

import se.doverfelt.Start;
import se.doverfelt.worlds.WorldPongz;

import java.util.Random;

/**
 * Created by rickard on 2016-02-03.
 */
public class EffectRandomColor implements Effect{

    String name;

    @Override
    public void update(WorldPongz world, float delta) {

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
        return Start.getPreferences().getInteger(getEffectType()+"Chance");
    }

    @Override
    public boolean isTimed() {
        return false;
    }

    @Override
    public long totalTime() {
        return 0;
    }

    @Override
    public long currentTime() {
        return 0;
    }

    @Override
    public boolean isSided() {
        return false;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public void create(WorldPongz world, String name) {
        this.name = name;
        if(WorldPongz.isFlashbanged){
            Random r = new Random();
            for (String s : world.getEffects().keySet()) {
                if (s.contains(WorldPongz.effectHandler.getEffectType(EffectFlashbang.class))) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof EffectRandomColor)) return false;
        EffectRandomColor obj = (EffectRandomColor) o;
        return obj.name == this.name;
    }
}
