package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityPaddle.java
 */
public class EntityPaddle implements Entity, Collidable {

    private SpriteBatch batch;
    private Sprite img;
    private float x,y , width = 2f, height = 15, yv = 0.2f;
    boolean isRight;

    public EntityPaddle(float xIn,float yIn, boolean Right){
        x = xIn;
        y = yIn;
        isRight = Right;
        batch = new SpriteBatch();
        img = new Sprite(new Texture("white.png"));
        img.setPosition(x, y);
        img.setSize(width, height);
    }
    
    @Override
    public void render(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        img.draw(batch);
        batch.end();

    }

    @Override
    public void update(int delta) {
        if(isRight) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y += yv*delta;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                y -= yv*delta;
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.W)){
                y += yv*delta;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)){
                y -= yv*delta;
            }
        }
        img.setPosition(x, y);
    }
}
