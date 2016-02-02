package se.doverfelt;

import com.badlogic.gdx.*;
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
import se.doverfelt.entities.EntityBorder;
import se.doverfelt.entities.EntityPaddle;
import se.doverfelt.entities.EntityTest;

import java.util.ArrayList;

public class PongzStart extends ApplicationAdapter {
	long timestamp;
    ArrayList<Entity> entities = new ArrayList<Entity>();
    SpriteBatch batch;
    BitmapFont font;
    private OrthographicCamera camera;
    private float aspect;

	@Override
	public void create () {

        aspect = 1f * ((float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth());
        camera = new OrthographicCamera(200f, 200f*aspect);
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        camera.zoom = 1f;
        camera.update();

        timestamp = System.currentTimeMillis();
        batch = new SpriteBatch();
        font = new BitmapFont();
        addEntity(new EntityTest());
        addEntity(new EntityBall());
        addEntity(new EntityBorder(0.1f, 0, camera.viewportWidth*2 - 0.2f, 2f));
        addEntity(new EntityBorder(0.1f, camera.viewportHeight, camera.viewportWidth*2 - 0.2f, 2f));
        addEntity(new EntityPaddle(1, 1, false));
        addEntity(new EntityPaddle(camera.viewportWidth-3f, 1, true));
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
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 1, camera.viewportHeight - 1);
        batch.end();
        camera.update();
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) Gdx.app.exit();
    }
}
