package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityBall.java
 */
public class EntityBall implements Entity, Collidable {

    private SpriteBatch batch;
    private Sprite img;
    private float x = 3, y = 3, xv = 8, yv =2;


    public EntityBall() {
        batch = new SpriteBatch();
        img = new Sprite(new Texture("ball.png"));
        img.setPosition(x, y);
        img.setSize(18, 18);
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
        x += xv;
        y += yv;
        //yv = (float) (yv - 0.2); //gravity for tha lulz

        /*if (xv > 0) xv += 0.1;//accerelation
        if (xv < 0) xv += - 0.1;
        if (yv > 0) yv += 0.1;
        if (yv < 0) yv += - 0.1;*/

        if (x < 0) {
            xv = -xv;
        }
        if (x > Gdx.graphics.getWidth()-img.getWidth()) {
            xv = -xv; //1280
        }
        if (y < 0) {
            yv = -yv;
        }
        if (y > Gdx.graphics.getHeight()-img.getHeight()) {
            yv = -yv;  //720
        }

        img.setPosition(x, y);
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public Body getBody() {
        return null;
    }
}
