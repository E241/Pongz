package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityPaddle.java
 */
public class EntityPaddle implements Entity, Collidable {

    private SpriteBatch batch;
    private Box2DSprite img;
    private float x,y , width = 2f, height = 15, yv = 40;
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
        bDef.position.set(xIn,yIn);
        body = world.createBody(bDef);
        img = new Box2DSprite(new Texture("white.png"));
        //img.setPosition(x-width/2f, y-height/2f);
        img.setSize(width, height);

        PolygonShape polygon = new PolygonShape();
        polygon.set(new float[]{x, y, x, y+height, x + width, y, x+width, y + height});

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
        img.draw(batch, body);
        batch.end();

    }

    @Override
    public void update(int delta) {
        if(isRight) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                body.setLinearVelocity(0f, yv);
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                body.setLinearVelocity(0f, -yv);
            } else {
                body.setLinearVelocity(0f, 0f);
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.W)){
                body.setLinearVelocity(0f, yv);
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)){
                body.setLinearVelocity(0f, -yv);
            } else {
                body.setLinearVelocity(0f, 0f);
            }
        }
        //img.setPosition(body.getPosition().x+width/2f, body.getPosition().y+height/15f);
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
