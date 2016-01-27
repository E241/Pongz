package se.doverfelt.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-27
 *         Filnamn: EntityTest.java
 */
public class EntityTest implements Entity {

    private SpriteBatch batch;
    private Texture img;
    private float x = 1, y = 1;
    private Random r = new Random();
    int timer = 0;

    public EntityTest() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(img, x, y);
        batch.end();
    }

    @Override
    public void update(int delta) {
        x += r.nextBoolean() ? -10*(delta) : 10 * (delta);
        y += r.nextBoolean() ? -10*(delta) : 10 * (delta);
        if (x < 0) x = 0;
        if (x > 1280) x = 1280;
        if (y < 0) y = 0;
        if (y > 720) y = 720;
    }
}
