package se.doverfelt;

import com.badlogic.gdx.ApplicationAdapter;
import se.doverfelt.worlds.*;

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
        addWorld("game", new WorldPongz());
        addWorld("menu", new WorldMenu());
        addWorld("options", new WorldOptions());
        addWorld("pause", new WorldPause());
        setWorld("menu");
    }

    private void addWorld(String name, World world) {
        world.create(this);
        worlds.put(name, world);
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
        resume();
    }


}
