package se.doverfelt.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.worlds.World;


/**
 * Created by rickard on 2016-01-29.
 */
public class EntityBorder implements Collidable {

    private final Sprite img;
    private float x, y;
    private Rectangle bounds;

    public EntityBorder(float x, float y, float width, float height, World world) {
        this.x = x;
        this.y = y;
        img = new Sprite(world.getAssetManager().<Texture>get("white.png"));
        img.setPosition(x, y);
        img.setSize(width, height);
        bounds = new Rectangle(x, y, width, height);
    }

    @Override
    public void render(OrthographicCamera camera, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        img.draw(batch);
        batch.end();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void dispose() {
        img.getTexture().dispose();
    }

    @Override
    public Rectangle getRect() {
        return bounds;
    }

    @Override
    public void collide(Entity other) {

    }
}
