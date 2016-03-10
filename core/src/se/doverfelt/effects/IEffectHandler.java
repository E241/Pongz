package se.doverfelt.effects;

/**
 * Created by rickard on 2016-03-10.
 */
public interface IEffectHandler {
    void registerEffect(Class<? extends Effect> effect);
    String getEffectType(Class<? extends Effect> effect);
    Effect getRandomEffect();
    void free(Class<? extends Effect> type, Effect effect);
}
