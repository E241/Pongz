package se.doverfelt.entities.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import se.doverfelt.worlds.World;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-21
 *         Filnamn: Button.java
 */
public class Button implements UIElement {

    private float width = 0, height = 0, x, y;
    private Rectangle rectangle;
    private SpriteBatch batch = new SpriteBatch();
    private boolean isMouseOver = false;
    private Sound sound;
    private Sprite sprite;
    private World world;
    private ButtonAction action;
    private BitmapFont font;
    private String text = "";
    private SpriteBatch fontBatch;

    public void setDimensions(float width, float height) {
        this.width = width;
        this.height = height;
        rectangle.set(x, y, width, height);
    }

    public void setAction(ButtonAction action) {
        this.action = action;
    }

    public void setText(String s) {
        this.text = s;
    }

    @Override
    public Rectangle getRect() {
        return rectangle;
    }

    @Override
    public void onClick(float x, float y) {
        action.doAction(world);
    }

    @Override
    public void setHover(boolean hovering) {
        isMouseOver = hovering;
    }

    @Override
    public void render(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);
        batch.end();
        fontBatch.begin();
        font.draw(fontBatch, text, camera.project(new Vector3(x, 0, 0)).x + camera.project(new Vector3(width, 0, 0)).y/2f - font.getSpaceWidth()*text.length()/2f, camera.project(new Vector3(0, y, 0)).y + camera.project(new Vector3(0, height, 0)).y/2f - font.getLineHeight()/2f);
        fontBatch.end();
    }

    @Override
    public void update(float delta) {
        sprite.setPosition(x, y);
        sprite.setSize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void create(String name, float x, float y, World world) {
        rectangle = new Rectangle(x, y, width, height);
        this.x = x;
        this.y = y;
        sound = Gdx.audio.newSound(Gdx.files.internal("bounce.wav"));
        sprite = new Sprite(new Texture("button.png"));
        this.world = world;
        this.font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fontBatch = new SpriteBatch();
    }
}
