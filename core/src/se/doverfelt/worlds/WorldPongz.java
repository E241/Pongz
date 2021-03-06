package se.doverfelt.worlds;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import se.doverfelt.Start;
import se.doverfelt.effects.*;
import se.doverfelt.entities.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class WorldPongz implements World {
    private static Pool<ParticleEffect> particlePool = Pools.get(ParticleEffect.class);
    private long timestamp, timestamp1 = -5000;
    public static HashMap<String, Entity> entities = new HashMap<String, Entity>();
    private HashMap<String, Effect> effects = new HashMap<String, Effect>();
    private BitmapFont font;
    private BitmapFont pointFnt;
    public OrthographicCamera camera;
    private float aspect;
    public static int PointsR= 0, PointsL = 0;
    private Texture white;
    private float r = 0, g = 0, b = 0;
    private ArrayList<String> toRemove = new ArrayList<String>();
    private Random rand = new Random();
    private ArrayList<String> toRemoveEffects = new ArrayList<String>();
    private Texture bg;
    private long lastPowerup, timestamp2 = 0;
    public static int Styrning = 2;
    private static boolean effectTextOn;
    private static String effectName;
    private boolean debug = false;
    private int collisionsChecks = 0;
    private int collisions = 0;
    private static ArrayList<ParticleEffect> pEffect = new ArrayList<ParticleEffect>();
    private ArrayList<ParticleEffect> pEffectRemove = new ArrayList<ParticleEffect>();
    public static IEffectHandler effectHandler;
    private static Pool<EntityPowerup> powerupPool = Pools.get(EntityPowerup.class);
    private static I18NBundle local;
    public static boolean isFlashbanged = false;
    private boolean running = true, justStarted= true, menu = false, wt = true;
    private float dim = 0;
    private PerformanceCounter tickCounter;
    private PerformanceCounter renderCounter;
    private Start start;
    private long winTimestamp = -1;
    private Music bgMusic;
    private String currentSong = "Pongz.wav";
    private Music.OnCompletionListener musicListener;
    private static int gameCount = 0, wl = 0, wr = 0;
    private ArrayList<EffectHUD> toAdd = new ArrayList<EffectHUD>();
    private HashMap<String, EffectHUD> eHUDLeft = new HashMap<String, EffectHUD>();
    private HashMap<String, EffectHUD> eHUDRight = new HashMap<String, EffectHUD>();
    private HashMap<String, EffectHUD> eHUDCenter = new HashMap<String, EffectHUD>();
    private AssetManager assets;

    public WorldPongz(IEffectHandler effectHandler) {
        WorldPongz.effectHandler = effectHandler;
    }

    @Override
	public void create(Start start, final AssetManager assets) {
        this.assets = assets;
        wt = true;
        System.out.println(WorldMenu.gameCount);
        WorldMenu.gameCount ++;
        System.out.println(WorldMenu.gameCount);
        this.start = start;
        aspect = 1f * ((float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth());
        camera = new OrthographicCamera(200f, 200f*aspect);
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        camera.zoom = 1f;
        camera.update();
        bg = assets.get("bg.png", Texture.class);
        bg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        white = assets.get("white.png", Texture.class);
        timestamp = System.currentTimeMillis();
        lastPowerup = System.currentTimeMillis();
        pointFnt = assets.get("big.fnt", BitmapFont.class);
        font = new BitmapFont();
        addEntity(new EntityBall(camera, this), "ball");
        ((EntityBall) entities.get("ball")).reset();
        addEntity(new EntityBorder(0.1f, 0, camera.viewportWidth - 0.2f, 2f, this), "borderBottom");
        addEntity(new EntityBorder(0.1f, camera.viewportHeight-2f, camera.viewportWidth - 0.2f, 2f, this), "borderTop");
        addEntity(new EntityPaddle(1, 1, false, this), "paddleLeft");
        addEntity(new EntityPaddle(camera.viewportWidth-3f, 1, true, this), "paddleRight");
        if (Start.getPreferences().getBoolean("autoPilot")) effectHandler.registerEffect(EffectAutoPilot.class);
        if (Start.getPreferences().getBoolean("drunk")) effectHandler.registerEffect(EffectDrunk.class);
        if (Start.getPreferences().getBoolean("randomColor")) effectHandler.registerEffect(EffectRandomColor.class);
        if (Start.getPreferences().getBoolean("sizeUp")) effectHandler.registerEffect(EffectSizeUp.class);
        if (Start.getPreferences().getBoolean("spin")) effectHandler.registerEffect(EffectSpin.class);
        if (Start.getPreferences().getBoolean("zoom")) effectHandler.registerEffect(EffectZoomOut.class);
        if (Start.getPreferences().getBoolean("flashbang")) effectHandler.registerEffect(EffectFlashbang.class);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        local = I18NBundle.createBundle(Gdx.files.internal("lang"), new Locale(Start.getPreferences().getString("lang")));
        //local = I18NBundle.createBundle(Gdx.files.internal("lang"), new Locale("es", "ES"));
        tickCounter = new PerformanceCounter("Tick");
        renderCounter = new PerformanceCounter("Render");

        musicListener = new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                if (currentSong == "Pongz.wav") {
                    currentSong = "Pongz2.wav";
                } else {
                    currentSong = "Pongz.wav";
                }
                bgMusic.dispose();
                bgMusic = assets.get(currentSong, Music.class);
                bgMusic.setOnCompletionListener(this);
                bgMusic.setVolume(0.5f);
                bgMusic.play();
            }
        };

        bgMusic = assets.get(currentSong, Music.class);
        //bgMusic.setLooping(true);
        bgMusic.setVolume(0.5f);
        bgMusic.setOnCompletionListener(musicListener);
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

    public void removeHUD(String name, String side) {
        if (side == "left") {
            eHUDLeft.remove(name);
        } else if (side == "right") {
            eHUDRight.remove(name);
        } else if (side == "center") {
            eHUDCenter.remove(name);
        }
    }

    public void removeEntity(String name) {
        toRemove.add(name);
    }

    public void addEffect(Effect effect, String name) {
        if (effect.isTimed()) {
            EffectHUD hud = new EffectHUD(this);
            hud.setEffect(effect);
            toAdd.add(hud);
        }
        effects.put(name, effect);
        effect.create(this, name);
    }

    public void removeEffect(String name) {
        toRemoveEffects.add(name);
    }

    public static void eName(String name){
        effectTextOn = true;
        effectName = local.get(name);

    }
	@Override
	public void render(SpriteBatch batch) {
        tickCounter.start();
        float delta = Gdx.graphics.getDeltaTime();
        timestamp = System.currentTimeMillis();
        collisionsChecks = 0;
        collisions = 0;
        tickCounter.stop();

        renderCounter.start();
		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        renderCounter.stop();
        /*batch.begin();
        TextureRegion region = new TextureRegion(bg, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);
        drawable.draw(batch, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();*/

       if (justStarted) {
           drawEntities(batch);
           drawHUD(delta);
           drawCountdown();
           //if(WorldMenu.gameCount >= start.getPreferences().getInteger("bestOf")){
           start.getFontBatch().begin();
           String s = "Game " + WorldMenu.gameCount + " of " + Start.getPreferences().getInteger("bestOf");
           pointFnt.draw(start.getFontBatch(), s, (Gdx.graphics.getWidth() / 2f) - (pointFnt.getSpaceWidth() * s.length() / 2f), (Gdx.graphics.getHeight() / 2f) + (Gdx.graphics.getHeight() / 8f) + pointFnt.getLineHeight());
           start.getFontBatch().end();
           //System.out.println("Space width" + pointFnt.getSpaceWidth());
           float s2 = ((Gdx.graphics.getWidth() / 2f) - (pointFnt.getSpaceWidth() * s.length()));
           //System.out.printf("%s%n", s2);
           //}
       } else if (running && !(PointsR >= Start.getPreferences().getInteger("maxScore") || PointsL >= Start.getPreferences().getInteger("maxScore"))) {
            genPowerups();
            tickEffects(delta);
            tickEntities(delta);
            drawEntities(batch);
            drawHUD(delta);
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                running = false;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
                start.setWorld("pause");
            }
        } else if (menu){
            start.setWorld("pause");
        } else if (PointsR >= Start.getPreferences().getInteger("maxScore") || PointsL >= Start.getPreferences().getInteger("maxScore")) {
            drawHUD(delta);
            drawEntities(batch);
            drawVictory(PointsL >= Start.getPreferences().getInteger("maxScore"), delta);
        } else if (!running && (!(PointsR >= Start.getPreferences().getInteger("maxScore") || PointsL >= Start.getPreferences().getInteger("maxScore")))) {
            drawEntities(batch);
            drawHUD(delta);
            drawPause(delta, batch);
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                running = true;
                dim = 0;
            }
        }

        if (!(pEffect.isEmpty())) {
            for (ParticleEffect p : pEffect) {
                tickCounter.start();
                if (p.isComplete()) {
                    pEffectRemove.add(p);
                }
                p.scaleEffect(camera.viewportHeight/Gdx.graphics.getHeight());
                p.update(delta);
                tickCounter.stop();
                renderCounter.start();
                batch.setProjectionMatrix(camera.combined);
                batch.begin();
                p.draw(batch, delta);
                batch.end();
                renderCounter.stop();
            }
        }
       if(effectTextOn){
           if (timestamp2 == 0){ timestamp2 = System.currentTimeMillis();}
           //String n = effectName.substring(effectName.indexOf(' ')+1, effectName.length());
           //effectName = effectName.substring(0, effectName.indexOf(' ')+2);
           /*renderCounter.start();
           start.getFontBatch().begin();
           pointFnt.draw(start.getFontBatch(), local.get("powerupMessage"), (Gdx.graphics.getWidth()/2f) - (pointFnt.getSpaceWidth()*(local.get("powerupMessage").length()/2) ), Gdx.graphics.getHeight()/2f + pointFnt.getLineHeight());
           pointFnt.draw(start.getFontBatch(), effectName, (Gdx.graphics.getWidth()/2f) - (pointFnt.getSpaceWidth()*(effectName.length()/2f) ), Gdx.graphics.getHeight()/2f );
           //pointFnt.draw(batch, local.format("powerupMessage", effectName), (Gdx.graphics.getWidth()/2f) - (pointFnt.getSpaceWidth()*(local.get("powerupMessage").length()/2)), Gdx.graphics.getHeight()/2f + pointFnt.getLineHeight());
           //pF3.draw(batch, n, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);


           start.getFontBatch().end();
           renderCounter.stop();*/
           /*tickCounter.start();
           if( 2000 < System.currentTimeMillis() - timestamp2){
               effectTextOn = false;
               timestamp2 = 0;
           }
           tickCounter.stop();*/
       }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) start.quit();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) debug = !debug;
        tickCounter.start();
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
            effectHandler.free(effects.get(toRemoveEffect).getClass(), effects.get(toRemoveEffect));
            effects.remove(toRemoveEffect);
        }
        toRemoveEffects.clear();

        for (ParticleEffect p : pEffectRemove) {
            particlePool.free(p);
            pEffect.remove(p);
        }
        pEffectRemove.clear();

        for (EffectHUD e : toAdd) {
            String s = "effectHUD" + System.currentTimeMillis();
            if (e.getEffect().isSided()) {
                if (!e.getEffect().isLeft()) {
                    eHUDRight.put(s, e);
                    addPloppable(e, s, camera.viewportWidth - 46, 5 + 10 * (eHUDRight.size()-1));
                } else {
                    eHUDLeft.put(s, e);
                    addPloppable(e, s, 5, 5 + 10 * (eHUDLeft.size() - 1));
                }
            } else {

                eHUDCenter.put(s, e);
                addPloppable(e, s, camera.viewportWidth/2f - 20.5f, 5 + 10 * (eHUDCenter.size() - 1));
            }
        }
        toAdd.clear();
        tickCounter.stop();
        tickCounter.tick(delta);
        renderCounter.tick(delta);
    }

    private void drawVictory(boolean isLeft, float delta) {
        if (winTimestamp == -1) winTimestamp = System.currentTimeMillis();
        if (System.currentTimeMillis() - winTimestamp < 5000) {
            dim = Math.min(dim + 2 * delta, 0.75f);
        } else {
            dim = Math.min(dim + 2 * delta, 1f);
        }
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, dim));
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        if (System.currentTimeMillis() - winTimestamp < 5000 && dim == 0.75f) {

                /*String s;
                if(Start.getPreferences().getInteger("bestOf") <= 1) {
                    s ="Winner: " + (isLeft ? "left" : "right") + "!";
                }else{
                    //s = "Winner of game " + WorldMenu.gameCount + " is " + (isLeft ? "left" : "right") + "!";
                    s = "";
                }
            start.getFontBatch().begin();
            font.draw(start.getFontBatch(), s, Gdx.graphics.getWidth() / 2f - (font.getSpaceWidth() * (s).length()) / 2f, 400);
            start.getFontBatch().end();*/
            if(isLeft && wt) {
                wl++;
            }else if (wt) {
                wr++;
            }
            wt = false;
            String s;
            start.getFontBatch().begin();
            if(WorldMenu.gameCount >= Start.getPreferences().getInteger("bestOf")) {
                s = "The Winner is " + (wl>wr ? "left": "right");

                pointFnt.draw(start.getFontBatch(), s, Gdx.graphics.getWidth() / 2f - (pointFnt.getSpaceWidth() * (s).length()) / 2f, Gdx.graphics.getHeight() / 2f);

            }
            s = wl + " : " + wr;
            pointFnt.draw(start.getFontBatch(), s, Gdx.graphics.getWidth() / 2f - (pointFnt.getSpaceWidth() * (s).length()) / 2f, (Gdx.graphics.getHeight() / 2f) + (pointFnt.getCapHeight() * 1.5f));
            start.getFontBatch().end();
        }
        if (System.currentTimeMillis() - winTimestamp > 6000) doVictory(isLeft);
    }



    private void doVictory(boolean isLeft) {
        winTimestamp = -1;
        timestamp1 = -5000;
        r = 0;
        g = 0;
        b = 0;
        PointsL = 0;
        PointsR = 0;
        dim = 0;
        Gdx.input.setCursorCatched(false);
        // Ta den under och gör en funktion i början av create "Game X out of X"
        if(WorldMenu.gameCount >= Start.getPreferences().getInteger("bestOf")){
            start.setWorld("menu");
            WorldMenu.gameCount = 0;
        }else {
            this.reset();
            justStarted = true;
            create(start, assets);
        }
    }



    void reset(){
        timestamp1 = -5000;
        r = 0;
        g = 0;
        b = 0;
        PointsL = 0;
        PointsR = 0;
        dim = 0;
        EntityBall ball =(EntityBall)entities.get("ball");
        ball.reset();
        for (String toRemoveEffect : effects.keySet()) {
            toRemoveEffects.add(toRemoveEffect);
        }
        for (String n : entities.keySet()) {
            Entity e = entities.get(n);
            if (e instanceof EffectHUD) {
                toRemove.add(n);
            }
        }
    }

    private void drawEntities(SpriteBatch batch) {
        renderCounter.start();
        for (Entity entity : entities.values()) {
            entity.render(camera, batch);
        }
        renderCounter.stop();
    }

    @Override
    public void pause() {
        running = false;
        Gdx.input.vibrate(500);
        Gdx.input.setCursorCatched(false);
        bgMusic.pause();
    }

    @Override
    public void resume() {
        Gdx.input.setCursorCatched(true);
        justStarted = timestamp1 < 0;
        running = timestamp1 > 0;
        bgMusic.play();
    }

    @Override
    public Start getStart() {
        return start;
    }

    @Override
    public AssetManager getAssetManager() {
        return assets;
    }

    @Override
    public void dispose() {
        for (Entity e : entities.values()) {
            e.dispose();
        }
        bg.dispose();
        font.dispose();
        pointFnt.dispose();
        white.dispose();
        bgMusic.dispose();
    }

    private void drawPause(float delta, SpriteBatch batch) {
        renderCounter.start();
        dim = Math.min(dim+2*delta, 0.75f);
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, dim));
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        if (dim == 0.75f) {
            start.getFontBatch().begin();
            pointFnt.draw(start.getFontBatch(), local.get("pause"), Gdx.graphics.getWidth() / 2f - (local.get("pause").length() * pointFnt.getSpaceWidth()) / 2f, Gdx.graphics.getHeight() / 2f - pointFnt.getLineHeight() / 2f);
            start.getFontBatch().end();
        }
        renderCounter.stop();
    }
    private void drawCountdown(){
        long temp = System.currentTimeMillis() - timestamp1;
        if(temp > 4000){
            timestamp1 = System.currentTimeMillis();
        }



        temp = System.currentTimeMillis() - timestamp1;
        String s = "3";

        if (temp > 3000){
            s = "0";
            justStarted = false;
            ((EntityBall)entities.get("ball")).unPause();
            running = true;
        } else if (temp > 2000){
            s = "1";
        } else if (temp > 1000) {
            s = "2";
        } else if (temp >0){
            ((EntityBall)entities.get("ball")).pause();
        }
        start.getFontBatch().begin();
        pointFnt.draw(start.getFontBatch(), s, Gdx.graphics.getWidth() / 2f - pointFnt.getSpaceWidth() / 2f, Gdx.graphics.getHeight() / 2f /*- pointFnt.getLineHeight() / 2f*/);
        start.getFontBatch().end();
    }
    private void genPowerups() {
        tickCounter.start();
        if (System.currentTimeMillis() - lastPowerup >= 5000 && rand.nextInt() < 25){
            String n = "powerup"+System.currentTimeMillis();
            addPloppable(powerupPool.obtain(), n, Math.max((rand.nextFloat()*200)-10-3, 3), Math.max((rand.nextFloat()*200*aspect)-10-2, 2));
        }
        if (System.currentTimeMillis()-lastPowerup >= 5000) lastPowerup = System.currentTimeMillis();
        tickCounter.stop();
    }

    private void tickEffects(float delta) {
        tickCounter.start();
        for (Effect effect : effects.values()) {
            effect.update(this, delta);
        }
        tickCounter.stop();
    }

    private void drawHUD(float delta) {
        SpriteBatch batch = start.getFontBatch();
        renderCounter.start();
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
            font.draw(batch, "Java Heap: " + (Gdx.app.getJavaHeap()/1024/1024) + "MB | Native Heap: " + (Gdx.app.getNativeHeap()/1024/1024) + "MB", 1, Gdx.graphics.getHeight()-(font.getLineHeight()*(entities.size() + 3)));
            MessageFormat.format("{0,number,#.##%}", tickCounter.current);
            font.draw(batch, "Tick: " + MessageFormat.format("{0,number,#.####%}", tickCounter.current), 1, Gdx.graphics.getHeight() - (font.getLineHeight()*(entities.size() + 4)));
            font.draw(batch, "Render: " + MessageFormat.format("{0,number,#.####%}", renderCounter.current), 1, Gdx.graphics.getHeight() - (font.getLineHeight()*(entities.size() + 5)));
            font.draw(batch, "Sum: " + (renderCounter.load.latest + tickCounter.load.latest), 1, Gdx.graphics.getHeight() - (font.getLineHeight()*(entities.size() + 6)));
        }

        String pl = "" + PointsL, pr = "" + PointsR;
        pointFnt.draw(batch, pl, Gdx.graphics.getWidth()/2f - 4 - (pl.length()*pointFnt.getSpaceWidth()), Gdx.graphics.getHeight()-25);
        pointFnt.draw(batch, pr, Gdx.graphics.getWidth()/2f + 4, Gdx.graphics.getHeight()-25);
        batch.draw(white, Gdx.graphics.getWidth()/2f-2, 0, 4f, Gdx.graphics.getHeight());
        batch.end();
        camera.update();
        renderCounter.stop();
    }

    private void tickEntities(float delta) {
        tickCounter.start();
        for (Entity entity : entities.values()) {
            if (entity instanceof Collidable) {
                collisionsChecks++;
                for (Entity c : entities.values()) {
                    if (c instanceof Collidable) {
                        collisionsChecks++;
                        if (Intersector.overlaps(((Collidable) c).getRect(), ((Collidable) entity).getRect()) && entity != c) {
                            collisions++;
                            ((Collidable) entity).collide(c);
                        }
                    }
                }
            }
            entity.update(delta);
        }
        tickCounter.stop();
    }

    public void setColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public float getR(){return this.r;}
    public float getG(){return this.g;}
    public float getB(){return this.b;}

     public static void startParticle(String name, float x, float y, boolean top) {
         ParticleEffect p = particlePool.obtain();
         p.load(Gdx.files.internal(name), Gdx.files.internal("Particles"));
         p.setPosition(x, y);
         if (top) {
             p.flipY();
         }
         pEffect.add(p);
         p.start();
     }

    public HashMap<String, Effect> getEffects() {
        return effects;
    }
}
