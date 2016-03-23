package se.doverfelt.worlds;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.doverfelt.Start;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-21
 *         Filnamn: World.java
 */
public interface World {

    void create(Start start);
    void render(SpriteBatch batch);
    void pause();
    void resume();

    Start getStart();

    void dispose();
}
