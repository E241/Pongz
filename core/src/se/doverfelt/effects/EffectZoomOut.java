package se.doverfelt.effects;

import se.doverfelt.Start;
import se.doverfelt.worlds.WorldPongz;

/**
 * Created by rickard on 2016-02-10.
 */
public class EffectZoomOut implements Effect {

    private String name;
    private float counter;
    private float zoom = 1f;
    private boolean out = false;

    @Override
    public void update(WorldPongz world, float delta) {
        counter += delta;

        if (counter < 11 && zoom < 6f && !out) zoom += 10f*delta;

        if (zoom >= 6 && !out) out = true;

        if (counter < 11 && counter > 9 && zoom > 1f && out) {
            zoom -= 10f*delta;
        }

        if (counter >= 11) {
            world.camera.zoom = 1f;
            world.removeEffect(name);
            return;
        }

        world.camera.zoom = Math.max(Math.min(zoom, 6f), 1f);
    }

    @Override
    public void create(WorldPongz world, String name) {
        if (world.camera.zoom != 1f) {
            world.removeHUD(name, "center");
            world.removeEffect(name);
        }
        counter = 0;
        zoom = 0;
        out = false;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEffectType() {
        return "zoom";
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
        return 11000;
    }

    @Override
    public long currentTime() {
        return (long)(counter*1000);
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
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof EffectZoomOut)) return false;
        EffectZoomOut obj = (EffectZoomOut) o;
        return obj.name.equals(this.name);
    }
}
