package se.doverfelt.client;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.google.gwt.core.client.GWT;
import se.doverfelt.effects.Effect;
import se.doverfelt.effects.IEffectHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by rickard on 2016-03-10.
 */
public class GWTEffectHandler implements IEffectHandler {

    private HashMap<Class, Pool> pools = new HashMap<Class, Pool>();

    private ArrayList<Class<? extends Effect>> effectRegistry = new ArrayList<Class<? extends Effect>>();
    private HashMap<Class<? extends Effect>, String> types = new HashMap<Class<? extends Effect>, String>();

    public void registerEffect(Class<? extends Effect> effect) {
        effectRegistry.add(effect);
        pools.put(effect, Pools.get(effect));
        Effect temp = (Effect)pools.get(effect).obtain();
        types.put(effect, temp.getEffectType());
        pools.get(effect).free(temp);
    }

    public String getEffectType(Class<? extends Effect> effect) {
        return types.get(effect);
    }

    public Effect getRandomEffect() {
        int total = 0;
        for (Class<? extends Effect> aClass : effectRegistry) {
            total += ((Effect)GWT.create(aClass)).getWeight();
        }

        int offset = 0;
        Random random = new Random();
        int seed = random.nextInt(total);
        for (Class<? extends Effect> aClass : effectRegistry) {
            if (seed < ((Effect)GWT.create(aClass)).getWeight()+offset) {
                return (Effect) pools.get(aClass).obtain();
            } else {
                offset += ((Effect)GWT.create(aClass)).getWeight();
            }
        }
        return null;
    }

    public void free(Class<? extends Effect> effectType, Effect effect) {
        pools.get(effectType).free(effect);
    }

}
