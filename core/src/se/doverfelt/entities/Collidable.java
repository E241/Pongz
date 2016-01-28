package se.doverfelt.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactListener;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: Collidable.java
 */
public interface Collidable extends ContactListener {

    Body getBody();

}
