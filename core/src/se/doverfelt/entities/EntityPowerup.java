package se.doverfelt.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.PongzStart;
import se.doverfelt.effects.EffectRandomColor;
import se.doverfelt.effects.EffectSpin;

import java.util.Random;

/**
 * Created by rickard on 2016-02-03.
 */
public class EntityPowerup implements Collidable {

    private final PongzStart world;
    private final Sprite img;
    private final SpriteBatch batch;
    private final Rectangle bounds;
    private float x, y;
    private Random r;
    private String name;

    public EntityPowerup(PongzStart world, float x, float y, String name) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.name = name;
        img = new Sprite(new Texture("powerup.png"));
        img.setSize(10, 10);
        img.setPosition(x, y);
        bounds = new Rectangle(x, y, 10, 10);
        batch = new SpriteBatch();
        r = new Random();
    }

    @Override
    public Rectangle getRect() {
        return bounds;
    }

    @Override
    public void collide(Entity other) {
        if (other instanceof EntityBall) {
            if (r.nextInt(100) > 101) {
                String n = "color" + System.currentTimeMillis();
                world.addEffect(new EffectRandomColor(n), n);
            } else {
                String n = "spin" + System.currentTimeMillis();
                world.addEffect(new EffectSpin(n), n);
            }
            world.removeEntity(name);
        }
    }

    @Override
    public void render(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        img.draw(batch);
        batch.end();
    }

    @Override
    public void update(int delta) {

    }
}
