package se.doverfelt.entities;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: Collidable.java
 */
public interface Collidable extends Entity{

    void collide(Entity other);

}
