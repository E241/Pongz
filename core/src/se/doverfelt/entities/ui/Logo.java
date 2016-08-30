package se.doverfelt.entities.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.worlds.World;

/**
 * Created by robin.boregrim on 2016-02-24.
 */
public class Logo implements UIElement {
    private Rectangle rectangle;
    private float height,width, y, x;
    private Sprite sprite;
    private World world;

    @Override
    public Rectangle getRect() {

        return rectangle;
    }

    @Override
    public void onClick(float x, float y) {

    }

    @Override
    public void setHover(boolean hovering) {
        if (hovering) ((UIManager)world).setTooltip("");
    }

    @Override
    public void create(String name, float x, float y, World world) {
        sprite = new Sprite(world.getAssetManager().get("logo.png", Texture.class));
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        rectangle = new Rectangle(x, y, width, height);
        this.x = x-(width/2f);
        this.y = y-(height/2f);
        this.world = world;
    }

    @Override
    public void render(OrthographicCamera camera, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void update(float delta) {
        sprite.setPosition(x, y);
        sprite.setSize(width, height);
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
