package se.doverfelt;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.doverfelt.effects.IEffectHandler;
import se.doverfelt.worlds.*;
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
    private static HashMap<String, World> worlds = new HashMap<String, World>();
    private static Preferences preferences;
    private IEffectHandler effectHandler;
    private SpriteBatch batch;
    private SpriteBatch fontBatch;
    private boolean shouldQuit = false;

    public SpriteBatch getFontBatch() {
        return fontBatch;
    }

    public Start(IEffectHandler effectHandler) {
        this.effectHandler = effectHandler;
    }

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

        batch = new SpriteBatch();
        fontBatch = new SpriteBatch();

        preferences.flush();
        addWorld("game", new WorldPongz(effectHandler));
        addWorld("menu", new WorldMenu());
        addWorld("options", new WorldOptions());
        addWorld("pause", new WorldPause());
        setWorld("menu");
    }

    private void initConfig() {
        preferences.putFloat("paddleRSpeed", 150f);
        preferences.putFloat("paddleLSpeed", 150f);
        preferences.putInteger("maxScore", 5);
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
        preferences.putBoolean("paddleBounds", false);
        preferences.putLong("init", System.currentTimeMillis());
        preferences.putInteger("control", 2);
        preferences.putInteger("bestOf", 3);
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
        if (activeWorld != null && !shouldQuit) activeWorld.render(batch);
        if (shouldQuit) doQuit();
    }

    @Override
    public void pause() {
        if (activeWorld != null) activeWorld.pause();
    }

    @Override
    public void resume() {
        if (activeWorld != null) activeWorld.resume();
    }

    public void setWorld(String name) {
        if (activeWorld != null) activeWorld.pause();
        activeWorld = worlds.get(name);
        worlds.get(name).resume();
    }

    public static World getWorld(String name){
        return worlds.get(name);
    }

    public void quit() {
        shouldQuit = true;
    }

    private void doQuit() {
        Gdx.app.debug("Pongz", "Exit!");
        activeWorld = null;
        for (World w : worlds.values()) {
            w.dispose();
        }
        batch.dispose();
        fontBatch.dispose();
        Gdx.app.exit();
    }
}
