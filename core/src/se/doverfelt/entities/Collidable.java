package se.doverfelt.entities;

import com.badlogic.gdx.math.Rectangle;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: Collidable.java
 */
public interface Collidable extends Entity{

    Rectangle getRect();

    void collide(Entity other);

}
