package se.doverfelt;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import se.doverfelt.entities.*;

import java.util.ArrayList;

public class PongzStart extends ApplicationAdapter {
	long timestamp;
    ArrayList<Entity> entities = new ArrayList<Entity>();
    SpriteBatch batch;
    BitmapFont font;
    private OrthographicCamera camera;
    private float aspect;
    public static int PointsR= 0, PointsL = 0;

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
        addEntity(new EntityBall(camera));
        addEntity(new EntityBorder(0.1f, 0, camera.viewportWidth - 0.2f, 2f));
        addEntity(new EntityBorder(0.1f, camera.viewportHeight-2f, camera.viewportWidth - 0.2f, 2f));
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
        //batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (Entity entity : entities) {
            if (entity instanceof Collidable) {
                for (Entity c : entities) {
                    if (c instanceof Collidable) {
                        if (Intersector.overlaps(((Collidable)c).getRect(), ((Collidable)entity).getRect())) {
                            ((Collidable) entity).collide(c);
                        }
                    }
                }
            }
            entity.update(delta);
            entity.render(camera);
        }
        batch.begin();
        font.getData().setScale(1);
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 1, Gdx.graphics.getHeight() - font.getLineHeight() - 1);
        font.getData().setScale(5f);
        String points = PointsL + " | " + PointsR;
        font.draw(batch, points, Gdx.graphics.getWidth()/2f - (font.getScaleX()*5*points.length())/2f, Gdx.graphics.getHeight()-10);
        batch.end();
        camera.update();
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) Gdx.app.exit();
    }
}
