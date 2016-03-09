package se.doverfelt.effects;

import se.doverfelt.Start;
import se.doverfelt.worlds.WorldPongz;

/**
 * Created by robin.boregrim on 2016-02-17.
 */
public class EffectFlashbang implements Effect {
    private String name;
    private float r, g, b, time = 0;
    private boolean isRemoved = false;

    @Override
    public void update(WorldPongz world, float delta) {
        if (!isRemoved) {
            time += delta;
            float tempR = 0, tempG = 0, tempB = 0;
            if (time < 10f) {
                tempR = 1 - ((1 - r) * (time / 10f));
                tempG = 1 - ((1 - g) * (time / 10f));
                tempB = 1 - ((1 - b) * (time / 10f));
                world.setColor(tempR, tempG, tempB);
            }
            if (tempB <= r + 0.01 && tempG <= g + 0.01 && tempR <= r + 0.01) {
                world.setColor(r, g, b);
                WorldPongz.isFlashbanged = false;
                    world.removeEffect(name);
            }
        } else {
            isRemoved = false;
        }
    }

    @Override
    public void create(WorldPongz world, String name) {
        this.name = name;
        if (WorldPongz.isFlashbanged){
            for (String s : world.getEffects().keySet()) {
                if (s.contains(getEffectType()) && !s.equals(name)) {
                    EffectFlashbang e = (EffectFlashbang)world.getEffects().get(s);
                    r = e.getR();
                    g = e.getG();
                    b = e.getB();
                    world.removeEffect(s);
                    isRemoved = true;
                }
            }
        }else {
            WorldPongz.isFlashbanged = true;
            r = world.getR();
            g = world.getG();
            b = world.getB();
        }
        time = 0;
        world.setColor(1,1,1);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEffectType() {
        return "flashbang";
    }

    @Override
    public int getWeight() {
        return Start.getPreferences().getInteger(getEffectType()+"Chance");
    }
    public float getR(){return this.r;}
    public float getG(){return this.g;}
    public float getB(){return this.b;}

    public void setColors(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
