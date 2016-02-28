package se.doverfelt.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-27
 *         Filnamn: Entity.java
 */
public interface Entity {
    void render(OrthographicCamera camera);
    void update(float delta);
    void dispose();
}
