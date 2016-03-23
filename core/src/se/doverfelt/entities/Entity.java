package se.doverfelt.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-27
 *         Filnamn: Entity.java
 */
public interface Entity {
    void render(OrthographicCamera camera, SpriteBatch batch);
    void update(float delta);
    void dispose();
}
