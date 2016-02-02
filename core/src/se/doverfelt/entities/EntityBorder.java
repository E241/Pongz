package se.doverfelt.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;


/**
 * Created by rickard on 2016-01-29.
 */
public class EntityBorder implements Entity, Collidable {

    private final SpriteBatch batch;
    private final Sprite img;
    private float x, y;

    public EntityBorder(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        batch = new SpriteBatch();
        img = new Sprite(new Texture("white.png"));
        img.setPosition(x, y);
        img.setSize(width, height);
    }

    @Override
    public void render(OrthographicCamera camera) {

    }

    @Override
    public void update(int delta) {

    }
}
