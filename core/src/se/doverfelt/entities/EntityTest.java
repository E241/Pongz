package se.doverfelt.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.Random;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-27
 *         Filnamn: EntityTest.java
 */
public class EntityTest implements Entity, Collidable {

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
}
