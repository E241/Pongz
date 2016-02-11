package se.doverfelt;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import se.doverfelt.effects.*;
import se.doverfelt.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Semaphore;

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
    private boolean debug = false;
    private int collisionsChecks = 0;
    private int collisions = 0;
    public static Pool<EffectDrunk> drunkPool = Pools.get(EffectDrunk.class);
    public static Pool<EffectRandomColor> randomColorPool = Pools.get(EffectRandomColor.class);
    public static Pool<EffectSizeUp> sizeUpPool = Pools.get(EffectSizeUp.class);
    public static Pool<EffectSpin> spinPool = Pools.get(EffectSpin.class);
    public static Pool<EffectZoomOut> zoomOutPool = Pools.get(EffectZoomOut.class);
    public static Pool<EffectAutoPilot> autoPilotPool = Pools.get(EffectAutoPilot.class);
    public static Pool<EntityPowerup> powerupPool = Pools.get(EntityPowerup.class);
    private static ArrayList<ParticleEffect> pEffect = new ArrayList<ParticleEffect>(), pEffectIn = new ArrayList<ParticleEffect>();
    private static final Semaphore pE = new Semaphore(1, true);

    @Override
	public void create () {
        aspect = 1f * ((float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth());
        camera = new OrthographicCamera(200f, 200f*aspect);
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        camera.zoom = 1f;
        camera.update();
        Gdx.input.setCursorCatched(true);
        white = new Texture("white.png");
        timestamp = System.currentTimeMillis();
        lastPowerup = System.currentTimeMillis();
        batch = new SpriteBatch();
        pointFnt = new BitmapFont(Gdx.files.internal("big.fnt"));
        font = new BitmapFont();
        addEntity(new EntityBall(camera), "ball");
        addEntity(new EntityBorder(0.1f, 0, camera.viewportWidth - 0.2f, 2f), "borderBottom");
        addEntity(new EntityBorder(0.1f, camera.viewportHeight-2f, camera.viewportWidth - 0.2f, 2f), "borderTop");
        addEntity(new EntityPaddle(1, 1, false, this), "paddleLeft");
        addEntity(new EntityPaddle(camera.viewportWidth-3f, 1, true, this), "paddleRight");
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.graphics.setContinuousRendering(true);

    }

    public void addEntity(Entity entity, String name) {
        entities.put(name, entity);
    }

    public void addPloppable(Ploppable entity, String name, float x, float y) {
        if (entity instanceof EntityPowerup) {
            lastPowerup = System.currentTimeMillis();
        }
        entity.create(name, x, y, this);
        entities.put(name, entity);
    }

    public void removeEntity(String name) {
        toRemove.add(name);
    }

    public void addEffect(Effect effect, String name) {
        effects.put(name, effect);
        effect.create(this, name);
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
        collisionsChecks = 0;
        collisions = 0;

		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        genPowerups();
        tickEffects(delta);
        tickEntities(delta);

        drawHUD(delta);
       if (pE.tryAcquire()) {
           if (!(pEffect.isEmpty())) {
               for (ParticleEffect p : pEffect) {
                   if (p.isComplete()) {
                       pEffect.remove(pEffect.indexOf(p));
                   }
                   p.draw(batch, delta);
               }
           }
           pE.release();
       }
       if(effectTextOn){
           if (timestamp2 == 0){ timestamp2 = System.currentTimeMillis();}
           BitmapFont pF2 = pointFnt;
           BitmapFont pF3 = pointFnt;
           //String n = effectName.substring(effectName.indexOf(' ')+1, effectName.length());
           //effectName = effectName.substring(0, effectName.indexOf(' ')+2);
           batch.begin();
           pF3.draw(batch, "Activated:", (Gdx.graphics.getWidth()/2f) - (pF3.getSpaceWidth()*("Activated:".length()/2) ), Gdx.graphics.getHeight()/2f + pF3.getLineHeight());
           pF2.draw(batch, effectName, (Gdx.graphics.getWidth()/2f) - (pF2.getSpaceWidth()*(effectName.length()/2f) ), Gdx.graphics.getHeight()/2f );
           //pF3.draw(batch, n, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);


           batch.end();
           if( 2000 < System.currentTimeMillis() - timestamp2){
               effectTextOn = false;
               timestamp2 = 0;
           }
       }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) Gdx.app.exit();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) debug = !debug;

        for (String s : toRemove) {
            if (entities.get(s) instanceof EntityPowerup) {
                powerupPool.free((EntityPowerup)entities.get(s));
            } else {
                entities.get(s).dispose();
            }
            entities.remove(s);
        }
        toRemove.clear();
        for (String toRemoveEffect : toRemoveEffects) {
            if (effects.get(toRemoveEffect) instanceof EffectDrunk) {
                drunkPool.free((EffectDrunk) effects.get(toRemoveEffect));
            } else if (effects.get(toRemoveEffect) instanceof EffectRandomColor) {
                randomColorPool.free((EffectRandomColor) effects.get(toRemoveEffect));
            } else if (effects.get(toRemoveEffect) instanceof EffectSizeUp) {
                sizeUpPool.free((EffectSizeUp) effects.get(toRemoveEffect));
            } else if (effects.get(toRemoveEffect) instanceof EffectSpin) {
                spinPool.free((EffectSpin) effects.get(toRemoveEffect));
            } else if (effects.get(toRemoveEffect) instanceof EffectZoomOut) {
                zoomOutPool.free((EffectZoomOut) effects.get(toRemoveEffect));
            } else if (effects.get(toRemoveEffect) instanceof EffectAutoPilot) {
                autoPilotPool.free((EffectAutoPilot) effects.get(toRemoveEffect));
            }
            effects.remove(toRemoveEffect);
        }
        toRemoveEffects.clear();
    }

    private void genPowerups() {
        if (System.currentTimeMillis() - lastPowerup >= 5000 && rand.nextInt() < 25){
            String n = "powerup"+System.currentTimeMillis();
            addPloppable(powerupPool.obtain(), n, Math.max((rand.nextFloat()*200)-10-3, 3), Math.max((rand.nextFloat()*200*aspect)-10-2, 2));
        }
        if (System.currentTimeMillis()-lastPowerup >= 5000) lastPowerup = System.currentTimeMillis();
    }

    private void tickEffects(int delta) {
        for (Effect effect : effects.values()) {
            effect.update(this, delta);
        }
    }

    private void drawHUD(int delta) {

        String entitiesOut = "";
        for (String s : entities.keySet()) {
            entitiesOut += s + "\n";
        }

        String effectsOut = "";
        for (String s : effects.keySet()) {
            effectsOut += s + "\n";
        }

        batch.begin();
        if (debug) {
            font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond() + "    Delta: " + delta, 1, Gdx.graphics.getHeight() - font.getLineHeight() - 1);
            font.draw(batch, "Entites: " + entities.size() + "\n" + entitiesOut, 1, Gdx.graphics.getHeight() - font.getLineHeight() * 2 - 1);
            font.draw(batch, "Effects: " + effects.size() + "\n" + effectsOut, 1, font.getLineHeight() * (effects.size() + 2));
            font.draw(batch, "CollisionChecks: " + collisionsChecks + "\nCollisions: " + collisions, 1, font.getLineHeight() * (effects.size() + 4));
            font.draw(batch, "Java Heap: " + (Gdx.app.getJavaHeap()) + "B | Native Heap: " + (Gdx.app.getNativeHeap()) + "B", 1, Gdx.graphics.getHeight()-(font.getLineHeight()*(entities.size() + 3)));
        }

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
                collisionsChecks++;
                for (Entity c : entities.values()) {
                    if (c instanceof Collidable) {
                        collisionsChecks++;
                        if (Intersector.overlaps(((Collidable)c).getRect(), ((Collidable)entity).getRect()) && entity != c) {
                            collisions++;
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
     public static void startParticle(String name, float x, float y, boolean top) {
         while (true) {
             if (pE.tryAcquire()) {
                 ParticleEffect p = new ParticleEffect();
                 p.load(Gdx.files.internal(name), Gdx.files.internal("Particles"));
                 p.setPosition(x, y);
                 if (top) {
                     p.flipY();
                 }
                 pEffect.add(p);
                 pEffect.get(pEffect.indexOf(p)).start();
                 pE.release();
                 break;
             }
         }
     }
}
