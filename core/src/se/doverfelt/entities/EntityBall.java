package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;


/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityBall.java
 */
public class EntityBall implements Entity, Collidable {

    private SpriteBatch batch;
    private Sprite img;
    private float x = 3, y = 3, xv = 1000, yv =1000;
    private final float WIDTH = 5, HEIGHT = 5;


    public EntityBall() {
        batch = new SpriteBatch();
        img = new Sprite(new Texture("ball.png"));
        //img.setPosition(x-WIDTH/2, y-HEIGHT/2);
        img.setSize(WIDTH, HEIGHT);
        img.setPosition(x,y);
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
        //img.setPosition(body.getPosition().x-WIDTH/2, body.getPosition().y-HEIGHT/2);
        //img.setRotation(MathUtils.radiansToDegrees * body.getAngle());
        //body.setLinearDamping(0f);

        x += xv;
        y += yv;

        if (x < 0){

        }else if (x > 1275){

        }
        if (y < 0){

        } else if (y > 715){
            
        }

    }
}
