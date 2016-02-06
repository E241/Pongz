package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import se.doverfelt.PongzStart;


/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityPaddle.java
 */
public class EntityPaddle implements Collidable {

    private SpriteBatch batch;
    private Sprite img;
    private float x,y , width = 2f, height = 15, yv = 0.15f;
    private final float origHeight;
    boolean isRight;
    private Rectangle bounds;
    public static int isMovingR =0, isMovingL = 0;
    public static float ry, ly,rHeight, lHeight;
    private EntityPowerUpHUD powerUpHUD;

    public EntityPaddle(float xIn, float yIn, boolean Right, PongzStart world){
        x = xIn;
        y = yIn;
        isRight = Right;
        batch = new SpriteBatch();
        img = new Sprite(new Texture("white.png"));
        img.setPosition(x, y);
        img.setSize(width, height);
        bounds = new Rectangle(x, y, width, height);
        origHeight = height;
        powerUpHUD = new EntityPowerUpHUD(this);
        world.addEntity(powerUpHUD, "HUD_" + (Right ? "right" : "left"));
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

        if (height != origHeight && !powerUpHUD.isVisible()) {
            powerUpHUD.setVisible(true);
        }
        if (height == origHeight && powerUpHUD.isVisible()) {
            powerUpHUD.setVisible(false);
        }

        if(isRight) {
            ry = y;
            rHeight = height;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y += yv*delta;
                isMovingR = 1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                y -= yv*delta;
                isMovingR = 2;
            }else {isMovingR = 0;}
        } else {
            ly = y;
            rHeight = height;
            if (Gdx.input.isKeyPressed(Input.Keys.W)){
                y += yv*delta;
                isMovingL = 1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)){
                y -= yv*delta;
                isMovingL = 2;
            }else {isMovingL = 0;}
        }
        bounds.setHeight(height);
        img.setSize(width, height);
        bounds.setPosition(x, y);
        img.setPosition(x, y);
    }

    @Override
    public Rectangle getRect() {
        return bounds;
    }

    @Override
    public void collide(Entity other) {

    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public float getOrigHeight() {
        return origHeight;
    }

    public void moveY(float mod) {
        y += mod;
    }

    public float getWidth() {
        return width;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
