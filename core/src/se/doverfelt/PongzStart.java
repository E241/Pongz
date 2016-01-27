package se.doverfelt;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.doverfelt.entities.Entity;
import se.doverfelt.entities.EntityTest;

import java.util.ArrayList;
import java.util.HashMap;

public class PongzStart extends ApplicationAdapter {
	long timestamp;
    ArrayList<Entity> entities = new ArrayList<Entity>();

	@Override
	public void create () {
        timestamp = System.currentTimeMillis();
        addEntity(new EntityTest());
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

	@Override
	public void render () {
        int delta = (int) (System.currentTimeMillis() - timestamp);
        timestamp = System.currentTimeMillis();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (Entity entity : entities) {
            entity.update(delta);
            entity.render();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) Gdx.app.exit();
    }
}
