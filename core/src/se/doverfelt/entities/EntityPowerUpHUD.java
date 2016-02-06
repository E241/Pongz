package se.doverfelt.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-06
 *         Filnamn: EntityPowerUpHUD.java
 */
public class EntityPowerUpHUD implements Entity {

    private EntityPaddle parent;
    private Sprite img;
    private SpriteBatch batch;
    private boolean visible = false;

    public EntityPowerUpHUD(EntityPaddle parent) {
        this.parent = parent;
        img = new Sprite(new Texture("arrow.png"));
        batch = new SpriteBatch();
        img.setSize(parent.getWidth(), parent.getWidth());
        img.setColor(Color.GREEN);
    }

    @Override
    public void render(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        if (visible) {
            batch.begin();
            img.draw(batch);
            batch.end();
        }
    }

    @Override
    public void update(int delta) {
        img.setPosition(parent.getX(), parent.getY() + parent.getHeight());
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
