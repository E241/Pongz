package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityBall.java
 */
public class EntityBall implements Entity, Collidable {

    private SpriteBatch batch;
    private Sprite img;
    private float x = 3, y = 3, xv = 10000, yv =10000;
    private Body body;
    private Fixture fixture;


    public EntityBall(World world) {
        batch = new SpriteBatch();
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(50, 50);
        body = world.createBody(bdef);
        img = new Sprite(new Texture("ball.png"));
        img.setPosition(x-4.5f, y-4.5f);
        img.setSize(18, 18);

        CircleShape circle = new CircleShape();
        circle.setRadius(9);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = circle;
        fdef.density = 0.01f;
        fdef.friction = 0.4f;
        fdef.restitution = 0.6f;

        fixture = body.createFixture(fdef);

        circle.dispose();

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

        /*if (x < 0) {
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
        }*/

        body.applyForceToCenter(xv, yv, false);
        img.setPosition(body.getPosition().x-9, body.getPosition().y-9);
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
