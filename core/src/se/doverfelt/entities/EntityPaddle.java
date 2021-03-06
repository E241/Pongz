package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.Start;
import se.doverfelt.worlds.WorldPongz;


/**
 * @author Rickard Doverfelt
 *         Datum: 2016-01-28
 *         Filnamn: EntityPaddle.java
 */
public class EntityPaddle implements Collidable {

    private Sprite img;
    private float x,y , width = 2f, height = 15, yv = 150f;
    private final float origHeight;
    boolean isRight;
    private Rectangle bounds;
    static int isMovingR =0, isMovingL = 0;
    static float ry, ly,rHeight, lHeight;
    private EntityPowerUpHUD powerUpHUD;
    private OrthographicCamera camera;
    private boolean canGoDown = true;
    private boolean canGoUp = true;

    public EntityPaddle(float xIn, float yIn, boolean Right, WorldPongz world){
        x = xIn;
        y = yIn;
        isRight = Right;
        yv = isRight ? Start.getPreferences().getFloat("paddleRSpeed") : Start.getPreferences().getFloat("paddleLSpeed");
        img = new Sprite(world.getAssetManager().<Texture>get("white.png"));
        img.setPosition(x, y);
        img.setSize(width, height);
        bounds = new Rectangle(x, y, width, height);
        origHeight = height;
        powerUpHUD = new EntityPowerUpHUD(this);
        world.addEntity(powerUpHUD, "HUD_" + (Right ? "right" : "left"));
    }
    
    @Override
    public void render(OrthographicCamera camera, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        img.draw(batch);
        batch.end();
        this.camera = camera;
    }

    @Override
    public void update(float delta) {

        if (height != origHeight && !powerUpHUD.isVisible()) {
            powerUpHUD.setVisible(true);
        }
        if (height == origHeight && powerUpHUD.isVisible()) {
            powerUpHUD.setVisible(false);
        }

        if(isRight) {
            ry = y;
            rHeight = height;
            if (Gdx.input.isKeyPressed(Input.Keys.UP) && canGoUp) {
                y += yv*delta;
                isMovingR = 1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && canGoDown) {
                y -= yv*delta;
                isMovingR = 2;
            }else {isMovingR = 0;}
        } else {
            ly = y;
            rHeight = height;
            if (Gdx.input.isKeyPressed(Input.Keys.W) && canGoUp){
                y += yv*delta;
                isMovingL = 1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S) && canGoDown){
                y -= yv*delta;
                isMovingL = 2;
            }else {isMovingL = 0;}
        }
        bounds.setHeight(height);
        img.setSize(width, height);
        bounds.setPosition(x, y);
        img.setPosition(x, y);
        canGoUp = true;
        canGoDown = true;
    }

    @Override
    public void dispose() {
        img.getTexture().dispose();
    }

    @Override
    public Rectangle getRect() {
        return bounds;
    }

    @Override
    public void collide(Entity other) {
        if (Start.getPreferences().getBoolean("paddleBounds")) {
            if (other instanceof EntityBorder) {
                if (y < camera.viewportHeight /2f) {
                    canGoDown = false;
                } else if (y > camera.viewportHeight/2f) {
                    canGoUp = false;
                }
            }
        }
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

    public void setY(float y) {
        this.y = y;
    }

    public boolean isLeft() {
        return !isRight;
    }
}
