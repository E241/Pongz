package se.doverfelt.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private OrthographicCamera camera;
    private float aspect;

    @Override
    public void create(Start start, AssetManager assets) {
        this.start = start;
        this.manager = assets;
        font = new BitmapFont();
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        aspect = 1f * ((float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth());
        camera = new OrthographicCamera(400, 400*aspect);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        if (manager.update()) {
            manager.finishLoading();
            start.initWorlds();
            start.setWorld("menu");
        }
        //renderer.setProjectionMatrix(camera.combined);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.GREEN);
        renderer.rect(Gdx.graphics.getWidth()/2f - 100, Gdx.graphics.getHeight()/2f - 20, (200*manager.getProgress()), 40);
        renderer.end();
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.rect(Gdx.graphics.getWidth()/2f - 100, Gdx.graphics.getHeight()/2f - 20, 200, 40);
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
