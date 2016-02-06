package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
<<<<<<< HEAD
import com.badlogic.gdx.audio.Sound;
=======
>>>>>>> 8625cedf4eba7bb1a9334e903e9146fa39ebdd96
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.PongzStart;
import se.doverfelt.effects.EffectRandomColor;
import se.doverfelt.effects.EffectSizeUp;
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
    private Sound powerUp;
    BitmapFont font;

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
        powerUp = Gdx.audio.newSound(Gdx.files.internal("PowerUp.wav"));
    }

    @Override
    public Rectangle getRect() {
        return bounds;
    }

    @Override
    public void collide(Entity other) {
        if (other instanceof EntityBall) {
            int seed = r.nextInt(100);
            font = new BitmapFont();
            String nm;
            if (seed < 45) {
                String n = "color" + System.currentTimeMillis();
                world.addEffect(new EffectRandomColor(n), n);
                nm =  "New Color";
            } else if (seed < 90) {
                String n = "sizeUp" + System.currentTimeMillis();
                world.addEffect(new EffectSizeUp(n, ((EntityBall) other).getLastPaddle()), n);
                nm = "Extra carbs!";
            } else {
                String n = "spin" + System.currentTimeMillis();
                world.addEffect(new EffectSpin(n), n);
                nm = "Spin!";
            }
            powerUp.play();
            world.removeEntity(name);
            PongzStart.eName(nm);
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
