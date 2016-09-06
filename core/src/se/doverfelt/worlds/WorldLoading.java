package se.doverfelt.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
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
    private float WIDTH, HEIGHT;
    private Interpolation.Exp pow;
    private Sprite logo;

    @Override
    public void create(Start start, AssetManager assets) {
        this.start = start;
        this.manager = assets;
        font = new BitmapFont();
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        aspect = 1f * ((float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth());
        camera = new OrthographicCamera(400, 400*aspect);
        WIDTH = Gdx.graphics.getWidth()/2f;
        HEIGHT = Gdx.graphics.getHeight()/5f;
        pow = new Interpolation.Exp((float)Math.E, 2);
        //assets.finishLoadingAsset("logo.png");
        /*logo = new Sprite(new Texture("logo.png"));
        logo.setPosition(logo.getWidth()/2f, 20);*/
    }

    @Override
    public void render(SpriteBatch batch) {

        //batch.setProjectionMatrix(camera.combined);
        if (manager.update()) {
            manager.finishLoading();
            start.initWorlds();
            start.setWorld("menu");
            return;
        }

        /*batch.begin();
        logo.draw(batch);
        batch.end();*/

        float progress = Interpolation.linear.apply(0, 1, manager.getProgress());
        //renderer.setProjectionMatrix(camera.combined);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.setColor(new Color(1 - progress, progress, 0, 1));
        renderer.rect(Gdx.graphics.getWidth()/2f - WIDTH/2f, Gdx.graphics.getHeight()/2f - HEIGHT/2f, WIDTH*pow.apply(manager.getProgress()), HEIGHT);
        renderer.end();
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.rect(Gdx.graphics.getWidth()/2f - WIDTH/2f, Gdx.graphics.getHeight()/2f - HEIGHT/2f, WIDTH, HEIGHT);
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
