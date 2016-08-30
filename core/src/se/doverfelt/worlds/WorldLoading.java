package se.doverfelt.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import se.doverfelt.Start;

/**
 * Created by rickard.doverfelt on 2016-08-30.
 */
public class WorldLoading implements World {
    private Start start;
    private AssetManager manager;
    private BitmapFont font;
    private ShapeRenderer renderer;

    @Override
    public void create(Start start, AssetManager assets) {
        this.start = start;
        this.manager = assets;
        font = new BitmapFont();
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (manager.update()) {
            start.initWorlds();
            start.setWorld("menu");
        }
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.GREEN);
        renderer.rect(10, 10, (200*manager.getProgress()), 40);
        renderer.end();
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.rect(10, 10, 200, 40);
        renderer.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public Start getStart() {
        return this.start;
    }

    @Override
    public AssetManager getAssetManager() {
        return null;
    }

    @Override
    public void dispose() {

    }
}
