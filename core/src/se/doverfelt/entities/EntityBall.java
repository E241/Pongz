package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityBall.java
 */
public class EntityBall implements Entity, Collidable {

    private SpriteBatch batch;
    private Box2DSprite img;
    private float x = 3, y = 3, xv = 1000, yv =1000;
    private Body body;
    private Fixture fixture;
    private final float WIDTH = 5, HEIGHT = 5;


    public EntityBall(World world) {
        batch = new SpriteBatch();
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(5, 5);
        body = world.createBody(bdef);
        img = new Box2DSprite(new Texture("ball.png"));
        //img.setPosition(x-WIDTH/2, y-HEIGHT/2);
        img.setSize(WIDTH, HEIGHT);

        MassData massData = new MassData();
        massData.mass = 1f;
        body.setMassData(massData);

        CircleShape circle = new CircleShape();
        circle.setRadius(WIDTH/2);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = circle;
        fdef.density = 1f;
        fdef.friction = 0.001f;
        fdef.restitution = 1f;

        fixture = body.createFixture(fdef);

        circle.dispose();
        //body.setLinearVelocity(xv, yv);
        body.applyLinearImpulse(xv, yv, 0, 0, true);

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
        //img.setPosition(body.getPosition().x-WIDTH/2, body.getPosition().y-HEIGHT/2);
        //img.setRotation(MathUtils.radiansToDegrees * body.getAngle());
        //body.setLinearDamping(0f);

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
        return body;
    }
}
