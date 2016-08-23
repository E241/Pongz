package se.doverfelt.effects;

import se.doverfelt.Start;
import se.doverfelt.worlds.WorldPongz;
import se.doverfelt.entities.EntityBall;

import java.util.Random;

/**
 * Created by Robin on 2016-02-06.
 */
public class EffectDrunk implements Effect {
    private EntityBall ball;
    private long timestamp,timestamp1 = 0;//timestamp2 = 0, timestamp3 = 0;
    private float mod , mod2;
    private String name;

    @Override
    public void update(WorldPongz world, float delta) {
        Random r = new Random();
        //Mod 1
        if (System.currentTimeMillis() - timestamp1 < 4000){
            ball.yv += mod;
        }else if (r.nextInt(100)<20){
            timestamp1 = System.currentTimeMillis();
            mod = (r.nextFloat()*4f) - 2f;
        }
        //Mod 2
        if (System.currentTimeMillis() - timestamp1 < 1000){
            ball.yv += mod2;
        }else if (r.nextInt(100)<10){
            //timestamp2 = System.currentTimeMillis();
            mod2 = (r.nextFloat()*6f) - 3f;
        }
      //  //Mod 3
      //  if (System.currentTimeMillis() - timestamp < 6000){
      //      ball.yv += mod3;
      //  }else if (r.nextInt(100)<10){
      //      timestamp3 = System.currentTimeMillis();
      //      mod3 = (r.nextFloat()*0.004f) - 0.002f;
      //  }
        if (System.currentTimeMillis() - timestamp > 30000){
            world.removeEffect(name);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEffectType() {
        return "drunk";
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
        return 30000;
    }

    @Override
    public long currentTime() {
        return (System.currentTimeMillis() - timestamp);
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
        this.ball = (EntityBall) WorldPongz.entities.get("ball");
        timestamp = System.currentTimeMillis();
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof EffectDrunk)) return false;
        EffectDrunk obj = (EffectDrunk) o;
        return obj.name.equals(this.name);
    }

}
