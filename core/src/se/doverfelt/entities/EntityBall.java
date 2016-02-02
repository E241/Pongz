package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import se.doverfelt.PongzStart;

import java.util.Random;


/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityBall.java
 */
public class EntityBall implements Collidable {

    private SpriteBatch batch;
    private Sprite img;
    private float x = 3, y = 3, xv = 0.1f, yv =0.1f;
    private final float WIDTH = 5, HEIGHT = 5;
    private OrthographicCamera camera;
    private Rectangle bounds;


    public EntityBall(OrthographicCamera camera) {
        batch = new SpriteBatch();
        img = new Sprite(new Texture("ball.png"));
        //img.setPosition(x-WIDTH/2, y-HEIGHT/2);
        img.setSize(WIDTH, HEIGHT);
        img.setPosition(x, y);
        bounds = new Rectangle(x, y, WIDTH, HEIGHT);
        this.camera = camera;
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
        x += xv*delta;
        y += yv*delta;

        if (x < 0){
            x = 0;
            xv = -xv; //Vinst Höger
            PongzStart.PointsR++;
            reset();
        }else if (x > camera.viewportWidth-WIDTH){
            x = camera.viewportWidth -WIDTH;
            xv = -xv; //Vinst Vänster
            PongzStart.PointsL++;
            reset();
        }
        if (y < 0){
            yv = -yv;
            y = 0;
        } else if (y > camera.viewportHeight-HEIGHT){
            yv = -yv;
            y = camera.viewportHeight-HEIGHT;
        }
        bounds.setPosition(x, y);
        img.setPosition(x,y);
    }

    private void reset() {
        Random r = new Random();
        img.setPosition(camera.viewportWidth/2f-WIDTH/2f, camera.viewportHeight/2f-HEIGHT/2f);
        x = img.getX();
        y = img.getY() + r.nextInt(20)-10;
        if (r.nextBoolean()){
            xv = -xv;
        }
        if (r.nextBoolean()){
            yv = -yv;
        }
    }

    @Override
    public Rectangle getRect() {
        return bounds;
    }

    @Override
    public void collide(Entity other) {
        if (other instanceof EntityBorder) {
            yv = -yv;
        } else if (other instanceof EntityPaddle) {
            float temp =camera.viewportWidth/2;
            if (x < temp) xv = Math.abs(xv);
            if (x > temp) xv = -Math.abs(xv);
        }
    }
}
