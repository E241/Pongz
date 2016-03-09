package se.doverfelt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import se.doverfelt.worlds.WorldMenu;
import se.doverfelt.worlds.WorldOptions;
import se.doverfelt.worlds.WorldPongz;
import se.doverfelt.worlds.World;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-21
 *         Filnamn: Start.java
 */
public class Start extends ApplicationAdapter {

    private World activeWorld;
    private HashMap<String, World> worlds = new HashMap<String, World>();
    private static Preferences preferences;

    @Override
    public void create() {
        Preferences temp = Gdx.app.getPreferences("pongz");
        if (!temp.contains("currentConfig")) {
            temp.putString("currentConfig", "pongzDefault");
            temp.flush();
        }

        preferences = Gdx.app.getPreferences(temp.getString("currentConfig"));

        if (!preferences.contains("init")) {
            initConfig();
        }

        preferences.flush();
        addWorld("game", new WorldPongz());
        addWorld("menu", new WorldMenu());
        addWorld("options", new WorldOptions());
        setWorld("menu");
    }

    private void initConfig() {
        preferences.putFloat("paddleRSpeed", 150f);
        preferences.putFloat("paddleLSpeed", 150f);
        preferences.putInteger("maxScore", 30);
        preferences.putBoolean("drunk", true);
        preferences.putBoolean("spin", true);
        preferences.putBoolean("flashbang", true);
        preferences.putBoolean("randomColor", true);
        preferences.putBoolean("autoPilot", true);
        preferences.putBoolean("sizeUp", true);
        preferences.putBoolean("zoom", true);
        preferences.putInteger("drunkChance", 10);
        preferences.putInteger("spinChance", 10);
        preferences.putInteger("flashbangChance", 15);
        preferences.putInteger("randomColorChance", 45);
        preferences.putInteger("autoPilotChance", 5);
        preferences.putInteger("sizeUpChance", 45);
        preferences.putInteger("zoomChance", 10);
        preferences.putString("lang", Locale.getDefault().toString());
        preferences.putLong("init", System.currentTimeMillis());
        preferences.flush();
    }

    private void addWorld(String name, World world) {
        world.create(this);
        worlds.put(name, world);
    }

    public static Preferences getPreferences() {
        return preferences;
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
