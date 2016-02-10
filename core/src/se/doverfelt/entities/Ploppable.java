package se.doverfelt.entities;

import se.doverfelt.PongzStart;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-10
 *         Filnamn: Ploppable.java
 */
public interface Ploppable extends Entity{
    void create(String name, float x, float y, PongzStart world);
}
