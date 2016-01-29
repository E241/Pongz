package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityPaddle.java
 */
public class EntityPaddle implements Entity, Collidable {

    private SpriteBatch batch;
    private Sprite img;
    private float x,y , width = 10, height = 22, yv = 5;
    private Body body;
    private Fixture fixture;
    boolean isRight;

    public EntityPaddle(float xIn,float yIn, World world, boolean Right){
        x = xIn;
        y = yIn;
        isRight = Right;
        batch = new SpriteBatch();
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.KinematicBody;
        bDef.position.set(50,50);
        body = world.createBody(bDef);
        img = new Sprite(new Texture("white.png"));
        img.setPosition(x, y);
        img.setSize(width, height);

        PolygonShape polygon = new PolygonShape();
        polygon.set(new float[]{x, y, x + width, y + height});

        FixtureDef fdef = new FixtureDef();
        fdef.shape = polygon;
        fdef.density = 0.01f;
        fdef.friction = 0.4f;
        fdef.restitution = 0.6f;

        fixture = body.createFixture(fdef);

        polygon.dispose();
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
        if(isRight) {
            if (Gdx.input.isButtonPressed(Input.Keys.UP)){
                y += yv;
            }
            if (Gdx.input.isButtonPressed(Input.Keys.DOWN)){
                y += -yv;
            }
        } else {
            if (Gdx.input.isButtonPressed(Input.Keys.W)){
                y += yv;
            }
            if (Gdx.input.isButtonPressed(Input.Keys.S)){
                y += -yv;
            }
        }
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
