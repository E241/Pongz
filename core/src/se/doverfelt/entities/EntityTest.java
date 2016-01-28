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
    private float x = 300, y = 300, xv = 8, yv =8;
    private Random r = new Random();
    int timer = 0;


    public EntityTest() {
        batch = new SpriteBatch();
        img = new Texture("ball.png");
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(img, x, y);
        batch.end();
    }

    @Override
    public void update(int delta) {
        //x += r.nextBoolean() ? -10*(delta) : 10 * (delta);
        //y += r.nextBoolean() ? -10*(delta) : 10 * (delta);
        x += xv;
        y += yv;
        yv = (float) (yv - 0.2); //gravity for tha lulz

        /*if (xv > 0) xv += 0.1;//accerelation
        if (xv < 0) xv += - 0.1;
        if (yv > 0) yv += 0.1;
        if (yv < 0) yv += - 0.1;*/

        if (x < 0) {
            xv = -xv;
        }
        if (x > 1262) {
            xv = -xv; //1280
        }
        if (y < 0) {
            yv = -yv;
        }
        if (y > 702) {
            yv = -yv;  //720
        }
    }
}
