package se.doverfelt.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.PongzStart;

import java.util.Random;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-27
 *         Filnamn: EntityTest.java
 */
public class EntityTest implements Collidable {

    private Random r = new Random();
    int timer = 0;

    public EntityTest() {

    }

    @Override
    public void render(OrthographicCamera camera) {

    }

    @Override
    public void update(int delta) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public Rectangle getRect() {
        return null;
    }

    @Override
    public void collide(Entity other) {

    }
}
