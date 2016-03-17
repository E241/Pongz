package se.doverfelt.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import se.doverfelt.Start;
import se.doverfelt.entities.ui.Button;
import se.doverfelt.entities.ui.UIElement;
import se.doverfelt.entities.ui.UIManager;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by robin.boregrim on 2016-03-09.
 */
public class WorldPause implements UIManager {

    private HashMap<String, UIElement> elements = new HashMap<String, UIElement>();
    private Start start;
    private OrthographicCamera camera;
    private float aspect;
    private Pool<Button> buttonPool = Pools.get(Button.class);
    private SpriteBatch batch;
    private BitmapFont font;
    private I18NBundle locale;
    private boolean showTooltip;
    private String currentTooltip;

    @Override
    public void create(Start start) {
        aspect = (float) Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
        camera = new OrthographicCamera(600, 600*aspect);
        this.start = start;

        batch = new SpriteBatch();
        font = new BitmapFont();

        locale = I18NBundle.createBundle(Gdx.files.internal("lang"), Locale.getDefault());

        Button temp = buttonPool.obtain();
        addElement(temp, "resume", camera.viewportWidth / 2, camera.viewportHeight - 20);
        temp = buttonPool.obtain();
        addElement(temp, "restart", camera.viewportWidth / 2, camera.viewportHeight );
        temp = buttonPool.obtain();
        addElement(temp, "mainMenu", camera.viewportWidth / 2, camera.viewportHeight + 20);
        temp = buttonPool.obtain();
        addElement(temp, "exit", camera.viewportWidth / 2, camera.viewportHeight + 40);

    }



    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tickElements();
        renderElements();
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
            Gdx.app.exit();
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
    private void renderElements() {
        for (UIElement uiElement : elements.values()) {
            uiElement.render(camera);
        }
    }

    private void renderTooltip() {
        if (showTooltip) {
            batch.begin();
            font.draw(batch, currentTooltip, 1, 1 + font.getLineHeight());
            batch.end();
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
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public Start getStart() {
        return null;
    }
}
