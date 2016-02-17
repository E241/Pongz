package se.doverfelt.effects;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by rickard on 2016-02-17.
 */
public class EffectHandler {
    public static Pool<EffectDrunk> drunkPool = Pools.get(EffectDrunk.class);
    public static Pool<EffectRandomColor> randomColorPool = Pools.get(EffectRandomColor.class);
    public static Pool<EffectSizeUp> sizeUpPool = Pools.get(EffectSizeUp.class);
    public static Pool<EffectSpin> spinPool = Pools.get(EffectSpin.class);
    public static Pool<EffectZoomOut> zoomOutPool = Pools.get(EffectZoomOut.class);
    public static Pool<EffectAutoPilot> autoPilotPool = Pools.get(EffectAutoPilot.class);

    private HashMap<Class, Pool> pools = new HashMap<Class, Pool>();

    private ArrayList<Class<? extends Effect>> effectRegistry = new ArrayList<Class<? extends Effect>>();

    public void registerEffect(Class<? extends Effect> effect) {
        effectRegistry.add(effect);
        pools.put(effect, Pools.get(effect));
    }

    public Effect getRandomEffect() {
        int total = 0;
        for (Class<? extends Effect> aClass : effectRegistry) {
            try {
                total += aClass.newInstance().getWeight();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        int offset = 0;
        Random random = new Random();
        int seed = random.nextInt(total);
        for (Class<? extends Effect> aClass : effectRegistry) {
            try {
                if (seed < aClass.newInstance().getWeight()+offset) {
                    return (Effect) pools.get(aClass).obtain();
                } else {
                    offset += aClass.newInstance().getWeight();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void free(Class<? extends Effect> effectType, Effect effect) {
        pools.get(effectType).free(effect);
    }
}
