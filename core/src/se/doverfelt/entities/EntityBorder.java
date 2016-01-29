package se.doverfelt.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * Created by rickard on 2016-01-29.
 */
public class EntityBorder implements Entity, Collidable {

    private final SpriteBatch batch;
    private final Body body;
    private final Box2DSprite img;
    private final Fixture fixture;
    private float x, y;

    public EntityBorder(World world, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        batch = new SpriteBatch();
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x, y);
        body = world.createBody(bdef);
        img = new Box2DSprite(new Texture("white.png"));
        //img.setPosition(x, y);
        img.setSize(width, height);
        PolygonShape rect = new PolygonShape();
        rect.set(new float[]{x, y, x+width, y, x, y+height, x+width, y+height});

        FixtureDef fdef = new FixtureDef();
        fdef.shape = rect;
        fdef.density = 0.01f;
        fdef.friction = 0.4f;
        fdef.restitution = 1f;

        fixture = body.createFixture(fdef);

        rect.dispose();
    }

    @Override
    public Body getBody() {
        return body;
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
    public void render(OrthographicCamera camera) {

    }

    @Override
    public void update(int delta) {

    }
}
