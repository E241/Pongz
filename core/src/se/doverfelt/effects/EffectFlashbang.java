package se.doverfelt.effects;

import se.doverfelt.PongzStart;

/**
 * Created by robin.boregrim on 2016-02-17.
 */
public class EffectFlashbang implements Effect {
    private String name;
    private float r, g, b, timestamp;

    @Override
    public void update(PongzStart world, int delta) {
        float time = System.currentTimeMillis()- timestamp;
        float tempR, tempG, tempB;
        if (time < 10000f){
            tempR = 1-((1-r)*(time/10000));
            tempG = 1-((1-g)*(time/10000));
            tempB = 1-((1-b)*(time/10000));
            world.setColor(tempR, tempG, tempB);
        }
    }

    @Override
    public void create(PongzStart world, String name) {
        this.name = name;
        if (PongzStart.isFlashbanged){
            for (String s : world.getEffects().keySet()) {
                if (s.contains(getEffectType())) {
                    EffectFlashbang e = (EffectFlashbang)world.getEffects().get(s);
                    r = e.getR();
                    g = e.getG();
                    b = e.getB();
                    world.removeEffect(s);
                }
            }
        }else {
            PongzStart.isFlashbanged = true;
            r = world.getR();
            g = world.getG();
            b = world.getB();
        }
        timestamp = System.currentTimeMillis();
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
        return 25;
    }
    public float getR(){return this.r;}
    public float getG(){return this.g;}
    public float getB(){return this.b;}
}
