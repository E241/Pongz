package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm;
import se.doverfelt.worlds.WorldPongz;


import java.util.Random;


/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityBall.java
 */
public class EntityBall implements Collidable {

    private Sprite img;
    private float x = 3, y = 3;
    public float xv = 100f, yv =100f;
    private final float WIDTH = 5, HEIGHT = 5;
    private OrthographicCamera camera;
    private Rectangle bounds;
    private Sound bounce, bom;
    private EntityPaddle lastPaddle = null;
    private boolean b = false;
    private long resetTime;
    private float maxVel = 2500f;
    private long ts1= 0, ts2 = 0;


    public EntityBall(OrthographicCamera camera) {
        img = new Sprite(new Texture("ball.png"));
        //img.setPosition(x-WIDTH/2, y-HEIGHT/2);
        img.setSize(WIDTH, HEIGHT);
        img.setPosition(x, y);
        bounds = new Rectangle(x, y, WIDTH, HEIGHT);
        bom = Gdx.audio.newSound(Gdx.files.internal("bom.wav"));
        bounce = Gdx.audio.newSound(Gdx.files.internal("bounce.wav"));
        resetTime = System.currentTimeMillis();
        this.camera = camera;
    }

    @Override
    public void render(OrthographicCamera camera, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        img.draw(batch);
        batch.end();

    }

    @Override
    public void update(float delta) {
        if (System.currentTimeMillis() - resetTime > 500) {

            if (xv >= 0) {
                x += Math.min(xv * delta, maxVel*delta);
            } else {
                x += Math.max(xv * delta, -maxVel*delta);
            }
            if (yv >= 0) {
                y += Math.min(yv * delta, maxVel*delta);
            } else {
                y += Math.max(yv * delta, -maxVel*delta);
            }

            if (x < 0) {
                //x = 0;
                // xv = -xv; //Vinst Höger
                WorldPongz.PointsR++;
                reset();
            } else if (x > camera.viewportWidth - WIDTH) {
                //x = camera.viewportWidth -WIDTH;
                //xv = -xv; //Vinst Vänster
                WorldPongz.PointsL++;
                reset();
            }
        /*if (y < 0){

            yv = -yv;
            y = 0;



        } else if (y > camera.viewportHeight-HEIGHT){
            bom.play();
            yv = -yv;
            y = camera.viewportHeight-HEIGHT;

        }*/
        }
        bounds.setPosition(x, y);
        img.setPosition(x,y);
    }

    @Override
    public void dispose() {
        bom.dispose();
        bounce.dispose();
        img.getTexture().dispose();
    }

    public void reset() {
        Random r = new Random();
        yv = r.nextFloat()*10;

        if (b){
            xv = -xv;
            b = false;
        }else b = true;
        if (r.nextBoolean()){
            yv = -yv;
        }
        float xPos;
        if (xv > 0 ){
            xPos = (float) (camera.viewportWidth * 0.2);
        }else {
            xPos = (float) (camera.viewportWidth * 0.8);
        }
        img.setPosition(xPos, (((camera.viewportHeight / 2f) - (HEIGHT / 2f)) + r.nextInt(20)) - 10);
        x = img.getX();
        y = img.getY();
        lastPaddle = null;
        resetTime = System.currentTimeMillis();
    }

    @Override
    public Rectangle getRect() {
        return bounds;
    }

    @Override
    public void collide(Entity other) {
        if (other instanceof EntityBorder) {
            float temp =camera.viewportHeight/2;
            bom.play();
            if (y < temp){
                yv = Math.abs(yv);
                if(System.currentTimeMillis()-ts1 >200) {
                    ts1 = System.currentTimeMillis();
                    //WorldPongz.startParticle("Particles/Spark.p", x, y, false);
                }
            }
            if (y > temp){
                yv = -Math.abs(yv);
                if(System.currentTimeMillis()-ts2 > 200) {
                    ts2 = System.currentTimeMillis();
                    //WorldPongz.startParticle("Particles/Spark.p", x, y + HEIGHT, true);
                }
            }         
        } else if (other instanceof EntityPaddle) {
            float temp = camera.viewportWidth / 2;
            if(x < temp){
                xv = Math.abs(xv);
                if (WorldPongz.Styrning == 1 || WorldPongz.Styrning == 3){
                    if (EntityPaddle.isMovingL == 1 || WorldPongz.Styrning == 3) {
                        yv = (yv + 20);
                    } else if (EntityPaddle.isMovingL == 2 || WorldPongz.Styrning == 3) {
                        yv = (yv - 20);
                    }
                } else if (WorldPongz.Styrning == 2 || WorldPongz.Styrning == 3){
                    float temp2 = y - EntityPaddle.ly - (EntityPaddle.lHeight/2);
                    yv += (temp2*10);
                }
            }
            if(x > temp){
                xv = -Math.abs(xv);
                if (WorldPongz.Styrning == 1 || WorldPongz.Styrning == 3){
                    if (EntityPaddle.isMovingR == 1 || WorldPongz.Styrning == 3) {
                        yv = (yv + 20);
                    } else if (EntityPaddle.isMovingR == 2 || WorldPongz.Styrning == 3) {
                        yv = (yv - 20);
                    }
                } else if (WorldPongz.Styrning == 2 || WorldPongz.Styrning == 3){
                    float temp2 = y - EntityPaddle.ry - (EntityPaddle.rHeight/2);
                    yv += (temp2*10);
                }
            }
            long id = bounce.play();
            bounce.setPan(id, ((EntityPaddle) other).isRight ? 1 : -1, 1);
            lastPaddle = (EntityPaddle) other;
        }
    }

    public EntityPaddle getLastPaddle() {
        return lastPaddle;
    }

    public float getYv(){return yv;}

    public float setYv(float yv){
        this.yv = yv;
        return this.yv;
    }

    public float getY() {
        return y;
    }
}
