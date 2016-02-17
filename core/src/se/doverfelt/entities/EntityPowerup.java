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

            world.addEffect(PongzStart.effectHandler.getRandomEffect(), "" + System.currentTimeMillis());

            int seed = r.nextInt(135);
            String nm;
            /*if (seed < 45) { // 45
                String n = "color" + System.currentTimeMillis();
                world.addEffect(PongzStart.randomColorPool.obtain(), n);
                nm =  "New Color";
            } else if (seed < 90) { // 90
                String n = "sizeUp" + System.currentTimeMillis();
                world.addEffect(PongzStart.sizeUpPool.obtain(), n);
                nm = "Extra carbs!";
            } else if (seed < 100) { // 100
                String n = "spin" + System.currentTimeMillis();
                world.addEffect(PongzStart.spinPool.obtain(), n);
                nm = "Spin!";
            } else if (seed < 120) { // 120
                String n = "zoom" + System.currentTimeMillis();
                world.addEffect(EffectHandler.getPool(), n);
                nm = "Zoooom!";
            } else if (seed < 130) { // 130
                String n = "drunk" + System.currentTimeMillis();
                world.addEffect(PongzStart.drunkPool.obtain(), n);
                nm = "Drunk!";
            } else {
                String n = "ai" + System.currentTimeMillis();
                world.addEffect(PongzStart.autoPilotPool.obtain(), n);
                nm = "Auto Pilot!";
            }*/
            powerUp.play();
            world.removeEntity(name);
            //if (seed >= 45 && seed < 90 && ((EntityBall) other).getLastPaddle() == null) {
                //return;
            //}
            //PongzStart.eName(nm);
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
