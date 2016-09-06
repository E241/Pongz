package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.worlds.World;
import se.doverfelt.worlds.WorldPongz;
import se.doverfelt.effects.*;

import java.util.Random;

/**
 * Created by rickard on 2016-02-03.
 */
public class EntityPowerup implements Collidable, Ploppable {

    private World world;
    private Sprite img;
    private final Rectangle bounds;
    private float x, y;
    private Random r;
    private String name;
    private Sound powerUp;

    public EntityPowerup() {
        bounds = new Rectangle(1, 1, 10, 10);
        r = new Random();
    }

    @Override
    public Rectangle getRect() {
        return bounds;
    }

    @Override
    public void collide(Entity other) {
        if (other instanceof EntityBall && world instanceof WorldPongz) {
            WorldPongz.startParticle("Particles/Boom", x, y, false);
            Effect effect = WorldPongz.effectHandler.getRandomEffect();
            ((WorldPongz)world).addEffect(effect, effect.getEffectType() + System.currentTimeMillis());
            powerUp.play();
            ((WorldPongz)world).removeEntity(name);
            WorldPongz.eName(effect.getEffectType());
        }
    }

    @Override
    public void render(OrthographicCamera camera, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        img.draw(batch);
        batch.end();
    }

    @Override
    public void update(float delta) {
        img.setPosition(x, y);
        bounds.setPosition(x, y);
    }

    @Override
    public void dispose() {
        powerUp.dispose();
        img.getTexture().dispose();
    }

    @Override
    public void create(String name, float x, float y, World world) {
        this.name = name;
        this.x = x;
        this.y = y;
        if (!(world instanceof WorldPongz)) return;
        this.world = world;
        img = new Sprite(world.getAssetManager().<Texture>get("powerup.png"));
        img.setSize(10, 10);
        powerUp = world.getAssetManager().get("PowerUp.wav", Sound.class);
    }
}
