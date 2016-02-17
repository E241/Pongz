package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.PongzStart;
import se.doverfelt.effects.*;

import java.util.Random;

/**
 * Created by rickard on 2016-02-03.
 */
public class EntityPowerup implements Collidable, Ploppable {

    private PongzStart world;
    private final Sprite img;
    private final SpriteBatch batch;
    private final Rectangle bounds;
    private float x, y;
    private Random r;
    private String name;
    private Sound powerUp;

    public EntityPowerup() {
        img = new Sprite(new Texture("powerup.png"));
        img.setSize(10, 10);
        bounds = new Rectangle(1, 1, 10, 10);
        batch = new SpriteBatch();
        r = new Random();
        powerUp = Gdx.audio.newSound(Gdx.files.internal("PowerUp.wav"));
    }

    @Override
    public Rectangle getRect() {
        return bounds;
    }

    @Override
    public void collide(Entity other) {
        if (other instanceof EntityBall) {
            Effect effect = PongzStart.effectHandler.getRandomEffect();
            world.addEffect(effect, effect.getEffectType() + System.currentTimeMillis());
            powerUp.play();
            world.removeEntity(name);
            PongzStart.eName(effect.getEffectType());
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
        img.setPosition(x, y);
        bounds.setPosition(x, y);
    }

    @Override
    public void dispose() {
        powerUp.dispose();
        img.getTexture().dispose();
        batch.dispose();
    }

    @Override
    public void create(String name, float x, float y, PongzStart world) {
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
    }
}
