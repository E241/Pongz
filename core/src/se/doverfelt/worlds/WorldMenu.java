package se.doverfelt.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import se.doverfelt.Start;
import se.doverfelt.Utils;
import se.doverfelt.entities.ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-21
 *         Filnamn: WorldMenu.java
 */
public class WorldMenu implements UIManager {

    private HashMap<String, UIElement> elements = new HashMap<String, UIElement>();
    private ArrayList<String> toRemove = new ArrayList<String>();
    private OrthographicCamera camera;
    private float aspect;
    private Pool<Button> buttonPool = Pools.get(Button.class);
    private Start start;
    private BitmapFont font;
    private String currentTooltip = "";
    private I18NBundle locale;
    private boolean showTooltip = false;
    private float dim;
    private boolean fadeIn = false;
    static int gameCount = 0, wl = 0, wr = 0;
    private long timestamp;
    private AssetManager manager;

    @Override
    public void create(final Start start, AssetManager assets) {

        aspect = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
        this.manager = assets;

        camera = new OrthographicCamera(600, 600*aspect);
        //camera.viewportWidth = Gdx.graphics.getWidth();
        //camera.viewportHeight = Gdx.graphics.getWidth();
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        //camera.position.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
        camera.zoom = 1f;
        camera.update();

        font = new BitmapFont();

        locale = I18NBundle.createBundle(Gdx.files.internal("lang"), Locale.getDefault());

        Button temp = buttonPool.obtain();
        addElement(temp, "startBtn", camera.viewportWidth/2f-50, camera.viewportHeight/2f-50-12.5f);
        Logo logo = new Logo();
        addElement(logo,"logo",camera.viewportWidth/2f, camera.viewportHeight/2f+50);
        temp.setDimensions(100, 25);
        temp.setAction(new ButtonAction() {
            @Override
            public void doAction(World world) {
                world.getStart().setWorld("game");
            }
        });
        temp.setIcon("play.png");
        temp.setTooltip(locale.get("menu.startGame"));
        temp = buttonPool.obtain();
        addElement(temp, "optionsBtn", camera.viewportWidth/2f-50, camera.viewportHeight/2f-80-12.5f);
        temp.setDimensions(70, 25);
        temp.setAction(new ButtonAction() {
            @Override
            public void doAction(World world) {
                start.setWorld("options");
            }
        });
        temp.setTooltip(locale.get("menu.options"));
        temp = buttonPool.obtain();
        addElement(temp, "quitBtn", camera.viewportWidth/2f+25, camera.viewportHeight/2f-80-12.5f);
        temp.setDimensions(25, 25);
        temp.setIcon("backArrow.png");
        temp.setAction(new ButtonAction() {
            @Override
            public void doAction(World world) {
                world.getStart().quit();
            }
        });
        temp.setTooltip(locale.get("menu.quit"));
        this.start = start;
        timestamp = System.currentTimeMillis();
    }


    public void addElement(UIElement entity, String name, float x, float y) {
        try {
            entity.create(name, x, y, this);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        elements.put(name, entity);
    }

    public void removeElement(String name) {
        toRemove.add(name);
    }

    @Override
    public void shouldShowTooltip(boolean show) {

    }

    public void setTooltip(String currentTooltip) {
        this.currentTooltip = currentTooltip;
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tickElements();
        renderElements(batch);
        renderTooltip();
        if (fadeIn) {
            dim = Math.max(dim - 2*Gdx.graphics.getDeltaTime(), 0);
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0, 0, 0, dim));
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            if (dim <= 0) {
                fadeIn = false;
            }
        }
        camera.update();

    }

    private void renderTooltip() {
        if (showTooltip) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            Utils.renderTextInBox(x, y, font, start.getFontBatch(), currentTooltip);
        }
    }

    private void tickElements() {
        showTooltip = false;
        for (UIElement uiElement : elements.values()) {
            if (uiElement.getRect().contains(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x, camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y)) {
                uiElement.setHover(true);
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && System.currentTimeMillis() - timestamp >= 200) {
                    uiElement.onClick(Gdx.input.getX(), Gdx.input.getY());
                }
                showTooltip = true;
            } else {
                uiElement.setHover(false);
            }
            uiElement.update(Gdx.graphics.getDeltaTime());
        }
    }

    private void renderElements(SpriteBatch batch) {
        for (UIElement uiElement : elements.values()) {
            uiElement.render(camera, batch);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        dim = 1;
        fadeIn = true;
        timestamp = System.currentTimeMillis();
    }

    @Override
    public Start getStart() {
        return start;
    }

    @Override
    public AssetManager getAssetManager() {
        return this.manager;
    }

    @Override
    public void dispose() {
        for (UIElement e : elements.values()) {
            e.dispose();
        }
        font.dispose();
    }
}
