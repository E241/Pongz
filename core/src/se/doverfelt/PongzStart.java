package se.doverfelt;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import se.doverfelt.entities.Entity;
import se.doverfelt.entities.EntityBall;
import se.doverfelt.entities.EntityTest;

import java.util.ArrayList;
import java.util.HashMap;

public class PongzStart extends ApplicationAdapter {
	long timestamp;
    ArrayList<Entity> entities = new ArrayList<Entity>();
    SpriteBatch batch;
    BitmapFont font;
    World world;
    Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

	@Override
	public void create () {
        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        timestamp = System.currentTimeMillis();
        batch = new SpriteBatch();
        font = new BitmapFont();
        addEntity(new EntityTest());
        addEntity(new EntityBall(world));
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

	@Override
	public void render () {
        int delta = (int) (System.currentTimeMillis() - timestamp);
        timestamp = System.currentTimeMillis();
        batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (Entity entity : entities) {
            entity.update(delta);
            entity.render(camera);
        }
        debugRenderer.render(world, camera.combined);
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 1, camera.viewportHeight - 15);
        batch.end();
        world.step(Math.min(delta/1000f, 0.25f), 6, 2);
        camera.update();
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) Gdx.app.exit();
    }
}
