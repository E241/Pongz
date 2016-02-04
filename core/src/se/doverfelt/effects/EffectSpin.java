package se.doverfelt.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import se.doverfelt.PongzStart;

/**
 * Created by rickard on 2016-02-03.
 */
public class EffectSpin implements Effect {

    private float rotation = 0;
    private float zoom = 1;
    private String name;

    public EffectSpin(String name) {
        this.name = name;
    }

    @Override
    public void update(PongzStart world, int delta) {
        world.camera.rotate(1f);
        //world.camera.zoom = 10f;
        rotation += 1f;
        if (rotation <= 180) {
            zoom += 0.02;
        } else {
            zoom -= 0.02;
        }
        world.camera.zoom = Math.min(zoom, 2f);
        world.camera.zoom = Math.max(world.camera.zoom, 1);
        // Messed up stuff: world.camera.viewportHeight = ((world.camera.viewportWidth/2f) * (float)Math.tan((rotation * MathUtils.radiansToDegrees)));
        //float h = world.camera.viewportHeight + ((world.camera.viewportWidth/2f) * (float)Math.sin((rotation)));
        //float w = h / (Gdx.graphics.getWidth()/Gdx.graphics.getHeight());
        //world.camera.zoom = ((world.camera.viewportWidth/2f) * (float)Math.sin((Math.abs(rotation-180))) + world.camera.viewportHeight) / world.camera.viewportHeight;
        //world.camera.zoom = 200f/w;
        if (rotation >= 360) {
            world.camera.zoom = 1f;
            world.camera.rotate(360-rotation);
            world.removeEffect(name);
            //world.camera.viewportWidth = 200f;
            //world.camera.viewportHeight = 200f * world.aspect;
        }
    }

    @Override
    public void create(PongzStart world) {

    }
}
