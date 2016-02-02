package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import se.doverfelt.PongzStart;


/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityBall.java
 */
public class EntityBall implements Entity, Collidable {

    private SpriteBatch batch;
    private Sprite img;
    private float x = 3, y = 3, xv = 1f, yv =1f;
    private final float WIDTH = 5, HEIGHT = 5;
    private OrthographicCamera camera;


    public EntityBall(OrthographicCamera camera) {
        batch = new SpriteBatch();
        img = new Sprite(new Texture("ball.png"));
        //img.setPosition(x-WIDTH/2, y-HEIGHT/2);
        img.setSize(WIDTH, HEIGHT);
        img.setPosition(x, y);
        this.camera = camera;
    }

    @Override
    public void render(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        img.draw(batch);
        batch.end();
        this.camera = camera;
    }

    @Override
    public void update(int delta) {
        //img.setPosition(body.getPosition().x-WIDTH/2, body.getPosition().y-HEIGHT/2);
        //img.setRotation(MathUtils.radiansToDegrees * body.getAngle());
        //body.setLinearDamping(0f);

        x += xv;
        y += yv;

        if (x < 0){
            x = 0;
            xv = -xv; //Vinst Höger
            PongzStart.PointsR++;
        }else if (x > camera.viewportWidth){
            x = camera.viewportWidth;
            xv = -xv; //Vinst Vänster
            PongzStart.PointsL++;
        }
        if (y < 0){
            yv = -yv;
            y = 0;
        } else if (y > camera.viewportHeight){
            yv = -yv;
            y = camera.viewportHeight;
        }
        img.setPosition(x,y);
    }
}
