package se.doverfelt.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import se.doverfelt.Start;
import se.doverfelt.entities.ui.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-21
 *         Filnamn: WorldMenu.java
 */
public class WorldOptions implements UIManager {

    public HashMap<String, UIElement> elements = new HashMap<String, UIElement>();
    private ArrayList<String> toRemove = new ArrayList<String>();
    private OrthographicCamera camera;
    private float aspect;
    private Pool<Button> buttonPool = Pools.get(Button.class);
    private Start start;
    private boolean showTooltip = false;

    @Override
    public void create(Start start) {

        aspect = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();

        camera = new OrthographicCamera(600, 600*aspect);
        //camera.viewportWidth = Gdx.graphics.getWidth();
        //camera.viewportHeight = Gdx.graphics.getWidth();
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        //camera.position.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
        camera.zoom = 1f;
        camera.update();
        Button temp = buttonPool.obtain();
        addElement(temp, "backBtn", 10, camera.viewportHeight - 35);
        Logo logo = new Logo();
        addElement(logo,"logo",camera.viewportWidth/2f, camera.viewportHeight/2f+50);
        temp.setDimensions(25, 25);
        temp.setAction(new ButtonAction() {
            @Override
            public void doAction(World world) {
                world.getStart().setWorld("menu");
            }
        });
        temp.setIcon("backArrow.png");
        /*CheckButton checkButton = new CheckButton();
        addElement(checkButton, "cbutton", camera.viewportWidth/2f, camera.viewportHeight/2f);
        checkButton.setAction(new ButtonAction() {
            @Override
            public void doAction(World world) {
                setTooltip(String.valueOf(System.currentTimeMillis()));
            }
        });*/
        final Slider slider = new Slider();
        addElement(slider, "slider", 200, 200);
        slider.setChangeListener(new ChangeListener() {
            @Override
            public void onChange() {
                Gdx.app.debug("Change", String.valueOf(slider.getValue()));
            }
        });
        this.start = start;
    }

    @Override
    public void setTooltip(String tooltip) {

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
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tickElements();
        renderElements(batch);
        camera.update();

    }

    private void tickElements() {
        showTooltip = false;
        Vector3 pos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        for (UIElement uiElement : elements.values()) {
            if (uiElement.getRect().contains(pos.x, pos.y)) {
                uiElement.setHover(true);
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    uiElement.onClick(pos.x, pos.y);
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
    }
}
