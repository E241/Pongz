package se.doverfelt.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import se.doverfelt.Start;
import se.doverfelt.Utils;
import se.doverfelt.entities.ui.Button;
import se.doverfelt.entities.ui.ButtonAction;
import se.doverfelt.entities.ui.UIElement;
import se.doverfelt.entities.ui.UIManager;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by robin.boregrim on 2016-03-09.
 */

/*
Checklista:
Lägga till språk grejen
Fixa nån slags design kanske
Måste ha bättre ikoner

 */
public class WorldPause implements UIManager {

    private HashMap<String, UIElement> elements = new HashMap<String, UIElement>();
    private Start start;
    private OrthographicCamera camera;
    private float aspect;
    private Pool<Button> buttonPool = Pools.get(Button.class);
    private BitmapFont font;
    private I18NBundle locale;
    private boolean showTooltip;
    private String currentTooltip;
    private AssetManager manager;

    @Override
    public void create(Start start, AssetManager assets) {
        aspect = (float) Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
        camera = new OrthographicCamera(600, 600*aspect);
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        camera.zoom = 1f;
        this.start = start;
        this.manager = assets;

        font = new BitmapFont();

        locale = I18NBundle.createBundle(Gdx.files.internal("lang"), Locale.getDefault());

        Button temp = buttonPool.obtain();
        int width = 100;
        addElement(temp, "resume", (camera.viewportWidth / 2f) - (width / 2), (camera.viewportHeight/2f) + 0);
        //Size and Action
        temp.setDimensions(width,25);
        temp.setTooltip("Resume");
        temp.setAction(new ButtonAction() {
            @Override
            public void doAction(World world) {
                world.getStart().setWorld("game");
            }
        });
        temp.setIcon("play.png");
        temp = buttonPool.obtain();
        addElement(temp, "restart", (camera.viewportWidth / 2f) - (width / 2), (camera.viewportHeight/2) - 40);
        temp.setDimensions(width,25);
        temp.setTooltip("Restart");
        temp.setAction(new ButtonAction() {
            @Override
            public void doAction(World world) {
                ((WorldPongz) Start.getWorld("game")).reset();
                world.getStart().setWorld("game");
            }
        });
        temp = buttonPool.obtain();
        addElement(temp, "mainMenu", (camera.viewportWidth / 2f) - (width / 2), (camera.viewportHeight/2) - 80);
        temp.setDimensions(width,25);
        temp.setTooltip("Main Menu");
        temp.setAction(new ButtonAction() {
            @Override
            public void doAction(World world) {
                WorldMenu.gameCount = 0;
                ((WorldPongz) Start.getWorld("game")).reset();
                world.getStart().setWorld("menu");

            }
        });
        temp = buttonPool.obtain();
        addElement(temp, "exit", (camera.viewportWidth / 2f) - (width / 2), (camera.viewportHeight/2) - 120);
        temp.setDimensions(width,25);
        temp.setTooltip("Exit");
        temp.setIcon("backArrow.png");
        temp.setAction(new ButtonAction() {
            @Override
            public void doAction(World world) {
                world.getStart().quit();
            }
        });
    }



    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tickElements();
        renderElements(batch);
        renderTooltip();
        camera.update();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            start.setWorld("game");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            start.setWorld("menu");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.R)){
            WorldPongz w = (WorldPongz)Start.getWorld("game");
            w.reset();
            start.setWorld("game");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.Q)){
            start.quit();
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

    private void renderTooltip() {
        if (showTooltip) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (!currentTooltip.isEmpty()) Utils.renderTextInBox(x, y, font, start.getFontBatch(), currentTooltip);
        }
    }

    public void setTooltip(String tooltip) {this.currentTooltip = tooltip;}

    @Override
    public void addElement(UIElement element, String name, float x, float y) {
        try {
            element.create(name, x, y, this);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        elements.put(name, element);
    }


    @Override
    public void removeElement(String name) {

    }

    @Override
    public void shouldShowTooltip(boolean show) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public Start getStart() {
        return start;
    }

    @Override
    public AssetManager getAssetManager() {
        return manager;
    }

    @Override
    public void dispose() {
        for (UIElement e : elements.values()) {
            e.dispose();
        }
        font.dispose();
    }
}
