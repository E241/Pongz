package se.doverfelt.worlds;

import se.doverfelt.Start;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-21
 *         Filnamn: World.java
 */
public interface World {

    void create(Start start);
    void render();
    void pause();
    void resume();

    Start getStart();
}
