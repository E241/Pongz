package se.doverfelt.entities.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.worlds.World;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-21
 *         Filnamn: Button.java
 */
public class Button implements UIElement {

    private float width = 20, height = 20, x, y;
    private Rectangle rectangle;
    private boolean isMouseOver = false;
    private Sound sound;
    private Sprite sprite;
    private UIManager world;
    private ButtonAction action;
    private BitmapFont font;
    private Sprite icon;
    private String tooltip = "";
    private float dim = 0;

    @Override
    public void create(String name, float x, float y, World world) {
        rectangle = new Rectangle(x, y, width, height);
        this.x = x;
        this.y = y;
        sound = Gdx.audio.newSound(Gdx.files.internal("bounce.wav"));
        sprite = new Sprite(new Texture("button.png"));
        if (world instanceof UIManager) {
            this.world = (UIManager) world;
        } else {
            Gdx.app.error("FATAL", "Can't add Button to non UIManager world");
            System.exit(-1);
        }
        this.font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        icon = new Sprite(new Texture("ball.png"));
    }
    @Override
    public void render(OrthographicCamera camera, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);
        icon.draw(batch);
        batch.end();

        if (isMouseOver) {
            dim = Math.min(dim+4*Gdx.graphics.getDeltaTime(), 0.5f);
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(1, 1, 1, dim));
            shapeRenderer.rect(x, y, width, height);
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
            shapeRenderer.rect(x, y, width, height);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

//        fontBatch.begin();
//        font.draw(fontBatch, icon, camera.project(new Vector3(x, 0, 0)).x + camera.project(new Vector3(width, 0, 0)).y/2f - font.getSpaceWidth()* icon.length()/2f, camera.project(new Vector3(0, y, 0)).y + camera.project(new Vector3(0, height, 0)).y/2f - font.getLineHeight()/2f);
//        fontBatch.end();
    }

    public void setDimensions(float width, float height) {
        this.width = width;
        this.height = height;
        rectangle.set(x, y, width, height);
    }

    public void setAction(ButtonAction action) {
        this.action = action;
    }

    public void setIcon(String s) {
        this.icon.getTexture().dispose();
        this.icon.setTexture(new Texture(s));
    }

    @Override
    public Rectangle getRect() {
        return rectangle;
    }

    @Override
    public void onClick(float x, float y) {
        sound.play();
        action.doAction(world);
    }

    @Override
    public void setHover(boolean hovering) {
        isMouseOver = hovering;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public void update(float delta) {
        if (isMouseOver) {
            world.setTooltip(tooltip);
        }
        sprite.setPosition(x, y);
        sprite.setSize(width, height);
        icon.setSize(height-(height*0.2f), height-(height*0.2f));
        icon.setPosition(x+width/2f-icon.getWidth()/2f, y+height/2f-icon.getHeight()/2f);
    }

    @Override
    public void dispose() {

    }


}
