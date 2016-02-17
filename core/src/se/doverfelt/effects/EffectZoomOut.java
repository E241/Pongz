package se.doverfelt.effects;

import se.doverfelt.PongzStart;

/**
 * Created by rickard on 2016-02-10.
 */
public class EffectZoomOut implements Effect {

    private String name;
    private int counter;
    private float zoom = 1f;
    private boolean out = false;

    @Override
    public void update(PongzStart world, int delta) {
        counter += delta;

        if (counter < 11000 && zoom < 6f && !out) zoom += 0.01f*delta;

        if (zoom > 6 && !out) out = true;

        if (counter < 11000 && counter > 9000 && zoom > 1f && out) {
            zoom -= 0.01f*delta;
        }

        if (counter >= 11000) {
            world.camera.zoom = 1f;
            world.removeEffect(name);
            return;
        }

        world.camera.zoom = Math.min(zoom, 6f);
    }

    @Override
    public void create(PongzStart world, String name) {
        if (world.camera.zoom != 1f) {
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
    public int getWeight() {
        return 10;
    }
}
