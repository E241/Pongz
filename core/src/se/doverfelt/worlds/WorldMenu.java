package se.doverfelt.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    public HashMap<String, UIElement> elements = new HashMap<String, UIElement>();
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

    @Override
    public void create(final Start start) {

        aspect = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();

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
                System.exit(0);
            }
        });
        temp.setTooltip(locale.get("menu.quit"));
        this.start = start;
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
            start.getFontBatch().begin();
            font.draw(start.getFontBatch(), currentTooltip, 1, 1 + font.getLineHeight());
            start.getFontBatch().end();
        }
    }

    private void tickElements() {
        showTooltip = false;
        for (UIElement uiElement : elements.values()) {
            if (uiElement.getRect().contains(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x, camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y)) {
                uiElement.setHover(true);
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
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
    }

    @Override
    public Start getStart() {
        return start;
    }

    @Override
    public void dispose() {
        for (UIElement e : elements.values()) {
            e.dispose();
        }
        font.dispose();
    }
}
