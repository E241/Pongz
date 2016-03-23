package se.doverfelt.entities.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.worlds.World;

/**
 * Created by rickard on 2016-03-18.
 */
public class CheckButton implements UIElement {
    private Rectangle rectangle;
    private World world;
    boolean isChecked = false;
    private SpriteBatch batch;
    private Sprite sprite;
    private Sprite icon;
    private boolean isMouseOver;
    private float dim = 0;
    private ButtonAction action;
    private long timestamp = 0;

    @Override
    public Rectangle getRect() {
        return rectangle;
    }

    @Override
    public void onClick(float x, float y) {
        if (System.currentTimeMillis() - timestamp > 200) {
            isChecked = !isChecked;
            action.doAction(world);
            timestamp = System.currentTimeMillis();
        }
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setAction(ButtonAction action) {
        this.action = action;
    }

    @Override
    public void setHover(boolean hovering) {
        isMouseOver = hovering;
    }

    @Override
    public void create(String name, float x, float y, World world) {
        rectangle = new Rectangle(x, y, 25, 25);
        this.world = world;
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture("button.png"));
        icon = new Sprite(new Texture("play.png"));
    }

    @Override
    public void render(OrthographicCamera camera, SpriteBatch batch) {
        this.batch.setProjectionMatrix(camera.combined);
        this.batch.begin();
        sprite.draw(this.batch);
        if (isChecked) icon.draw(this.batch);
        this.batch.end();

        if (isMouseOver) {
            dim = Math.min(dim+4* Gdx.graphics.getDeltaTime(), 0.5f);
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(1, 1, 1, dim));
            shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        } else {
            dim = Math.max(dim-4*Gdx.graphics.getDeltaTime(), 0);
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(1, 1, 1, dim));
            shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void update(float delta) {
        sprite.setPosition(rectangle.getX(), rectangle.getY());
        sprite.setSize(rectangle.getWidth(), rectangle.getHeight());
        icon.setSize(rectangle.getWidth(), rectangle.getHeight());
        icon.setPosition(rectangle.getX(), rectangle.getY());
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
        icon.getTexture().dispose();
    }
}
