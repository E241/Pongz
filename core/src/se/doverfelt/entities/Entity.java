package se.doverfelt.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.ContactListener;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-27
 *         Filnamn: Entity.java
 */
public interface Entity {
    void render(OrthographicCamera camera);
    void update(int delta);
}
