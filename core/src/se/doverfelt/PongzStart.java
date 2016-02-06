package se.doverfelt;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import se.doverfelt.effects.Effect;
import se.doverfelt.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PongzStart extends ApplicationAdapter {
	long timestamp;
    public static HashMap<String, Entity> entities = new HashMap<String, Entity>();
    private HashMap<String, Effect> effects = new HashMap<String, Effect>();
    SpriteBatch batch;
    BitmapFont font;
    BitmapFont pointFnt;
    public OrthographicCamera camera;
    public float aspect;
    public static int PointsR= 0, PointsL = 0;
    private Texture white;
    private float r = 0, g = 0, b = 0;
    private ArrayList<String> toRemove = new ArrayList<String>();
    private Random rand = new Random();
    private ArrayList<String> toRemoveEffects = new ArrayList<String>();
    private long lastPowerup, timestamp2 = 0;
    public static int Styrning = 2;
    private static boolean effectTextOn;
    private static String effectName;

    @Override
	public void create () {
        aspect = 1f * ((float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth());
        camera = new OrthographicCamera(200f, 200f*aspect);
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        camera.zoom = 1f;
        camera.update();

        white = new Texture("white.png");
        timestamp = System.currentTimeMillis();
        lastPowerup = System.currentTimeMillis();
        batch = new SpriteBatch();
        pointFnt = new BitmapFont(Gdx.files.internal("big.fnt"));
        font = new BitmapFont();
        addEntity(new EntityBall(camera), "ball");
        addEntity(new EntityBorder(0.1f, 0, camera.viewportWidth - 0.2f, 2f), "borderBottom");
        addEntity(new EntityBorder(0.1f, camera.viewportHeight-2f, camera.viewportWidth - 0.2f, 2f), "borderTop");
        addEntity(new EntityPaddle(1, 1, false), "paddleLeft");
        addEntity(new EntityPaddle(camera.viewportWidth-3f, 1, true), "paddleRight");
        addEntity(new EntityPowerup(this, 100, 100, "pow"), "pow");
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    public void addEntity(Entity entity, String name) {
        if (entity instanceof EntityPowerup) {
            lastPowerup = System.currentTimeMillis();
        }
        entities.put(name, entity);
    }

    public void removeEntity(String name) {
        toRemove.add(name);
    }

    public void addEffect(Effect effect, String name) {
        effects.put(name, effect);
        effect.create(this);
    }

    public void removeEffect(String name) {
        toRemoveEffects.add(name);
    }

    public static void eName(String name){
        effectTextOn = true;
        effectName = name;

    }
	@Override
	public void render () {
        int delta = (int) (System.currentTimeMillis() - timestamp);
        timestamp = System.currentTimeMillis();

		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        genPowerups();
        tickEffects(delta);
        tickEntities(delta);

        drawHUD();
       if(effectTextOn){
           if (timestamp2 == 0){ timestamp2 = System.currentTimeMillis();}
           BitmapFont pF2 = pointFnt;
           BitmapFont pF3 = pointFnt;
           //String n = effectName.substring(effectName.indexOf(' ')+1, effectName.length());
           //effectName = effectName.substring(0, effectName.indexOf(' ')+2);
           batch.begin();
           pF3.draw(batch, "Activated:", (Gdx.graphics.getWidth()/2f) - (pF3.getSpaceWidth()*("Activated:".length()/2) ), Gdx.graphics.getHeight()/2f + pF3.getCapHeight());
           pF2.draw(batch, effectName, (Gdx.graphics.getWidth()/2f) - (pF2.getSpaceWidth()*(effectName.length()/2) ), Gdx.graphics.getHeight()/2f);
           //pF3.draw(batch, n, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
           batch.end();
           if( 2000 < System.currentTimeMillis() - timestamp2){
               effectTextOn = false;
               timestamp2 = 0;
           }
       }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) Gdx.app.exit();
        for (String s : toRemove) {
            entities.remove(s);
        }
        toRemove.clear();
        for (String toRemoveEffect : toRemoveEffects) {
            effects.remove(toRemoveEffect);
        }
        toRemoveEffects.clear();
    }

    private void genPowerups() {
        if (System.currentTimeMillis() - lastPowerup >= 5000 && rand.nextInt() < 25){
            String n = "powerup"+System.currentTimeMillis();
            addEntity(new EntityPowerup(this, rand.nextFloat()*200, rand.nextFloat()*200*aspect, n), n);
        }
        if (System.currentTimeMillis()-lastPowerup >= 5000) lastPowerup = System.currentTimeMillis();
    }

    private void tickEffects(int delta) {
        for (Effect effect : effects.values()) {
            effect.update(this, delta);
        }
    }

    private void drawHUD() {
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 1, Gdx.graphics.getHeight() - font.getLineHeight() - 1);
        String pl = "" + PointsL, pr = "" + PointsR;
        pointFnt.draw(batch, pl, Gdx.graphics.getWidth()/2f - 4 - (pl.length()*pointFnt.getSpaceWidth()), Gdx.graphics.getHeight()-25);
        pointFnt.draw(batch, pr, Gdx.graphics.getWidth()/2f + 4, Gdx.graphics.getHeight()-25);
        batch.draw(white, Gdx.graphics.getWidth()/2f-2, 0, 4f, Gdx.graphics.getHeight());
        batch.end();
        camera.update();
    }

    private void tickEntities(int delta) {
        for (Entity entity : entities.values()) {
            if (entity instanceof Collidable) {
                for (Entity c : entities.values()) {
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
    }

    public void setColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

}
