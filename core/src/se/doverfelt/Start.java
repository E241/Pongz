package se.doverfelt;

import com.badlogic.gdx.ApplicationAdapter;
import se.doverfelt.worlds.WorldPongz;
import se.doverfelt.worlds.World;

import java.util.HashMap;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-21
 *         Filnamn: Start.java
 */
public class Start extends ApplicationAdapter {

    private World activeWorld;
    private HashMap<String, World> worlds = new HashMap<String, World>();

    @Override
    public void create() {
        worlds.put("game", new WorldPongz());
        setWorld("game");
    }

    @Override
    public void render() {
        activeWorld.render();
    }

    @Override
    public void pause() {
        activeWorld.pause();
    }

    @Override
    public void resume() {
        activeWorld.resume();
    }

    public void setWorld(String name) {
        activeWorld = worlds.get(name);
        activeWorld.create(this);
    }


}
