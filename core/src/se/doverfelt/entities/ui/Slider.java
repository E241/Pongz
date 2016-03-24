package se.doverfelt.entities.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.worlds.World;

/**
 * Created by rickard on 2016-03-23.
 */
public class Slider implements UIElement {

    private World world;
    private Rectangle barBounds;
    private Rectangle markerBounds;
    private Rectangle outerBounds;
    private float max = 100, min = 0, value = 0;
    private Sprite bar, marker;
    private boolean markerClicked = false;
    private float lastX = 0, lastY = 0, newX = 0, newY = 0, dx = 0, dy = 0;
    private BitmapFont font;
    private long lastClick = 0;

    @Override
    public void create(String name, float x, float y, World world) {
        this.world = world;
        barBounds = new Rectangle(x, y, 100, 15);
        markerBounds = new Rectangle(x+7.5f, y-10, 15, 15);
        outerBounds = new Rectangle(x, y, 100, 25);
        bar = new Sprite(new Texture("bar.png"));
        bar.setSize(barBounds.getWidth(), barBounds.getHeight());
        marker = new Sprite(new Texture("play.png"));
        marker.setSize(markerBounds.getWidth(), markerBounds.getHeight());
        marker.rotate90(false);
        font = new BitmapFont();
    }

    @Override
    public Rectangle getRect() {
        return outerBounds;
    }

    @Override
    public void onClick(float x, float y) {
        if (markerBounds.contains(x, y)) {
            markerClicked = true;
        }
        if (newX == 0) newX = x;
        if (newY == 0) newY = y;

        lastX = newX;
        lastY = newY;

        newX = x;
        newY = y;

        dx = newX - lastX;
        dy = newY - lastY;
        lastClick = System.currentTimeMillis();
    }

    @Override
    public void setHover(boolean hovering) {

    }

    @Override
    public void render(OrthographicCamera camera, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            bar.draw(batch);
            marker.draw(batch);
        batch.end();
    }

    @Override
    public void update(float delta) {
        outerBounds.merge(barBounds);
        outerBounds.merge(markerBounds);

        if (markerClicked) {
            if (dx > 0) {
                markerBounds.setX(Math.min(markerBounds.getX() + dx, barBounds.getX()+barBounds.getWidth() - 7.5f));
            } else if(dx < 0) {
                markerBounds.setX(Math.max(markerBounds.getX() + dx, barBounds.getX()-7.5f));
            }
        }

        value = max * ((markerBounds.getX() + 7.5f - barBounds.getX())/barBounds.getWidth());

        marker.setPosition(markerBounds.getX(), markerBounds.getY());
        bar.setPosition(barBounds.getX(), barBounds.getY());
        world.getStart().getFontBatch().begin();
        font.draw(world.getStart().getFontBatch(), "Val: " + value, 50, 50);
        world.getStart().getFontBatch().end();
        markerClicked = false;
        dx = 0;
        dy = 0;
        if (System.currentTimeMillis() - lastClick > 200) {
            newX = 0;
            newY = 0;
        }
    }

    @Override
    public void dispose() {
        bar.getTexture().dispose();
        marker.getTexture().dispose();
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getValue() {
        return value;
    }

}
