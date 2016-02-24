package se.doverfelt.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import se.doverfelt.Start;
import se.doverfelt.entities.ui.Button;
import se.doverfelt.entities.ui.ButtonAction;
import se.doverfelt.entities.ui.Logo;
import se.doverfelt.entities.ui.UIElement;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-21
 *         Filnamn: WorldMenu.java
 */
public class WorldMenu implements World {

    public HashMap<String, UIElement> elements = new HashMap<String, UIElement>();
    private ArrayList<String> toRemove = new ArrayList<String>();
    private OrthographicCamera camera;
    private float aspect;
    private Pool<Button> buttonPool = Pools.get(Button.class);
    private Start start;

    @Override
    public void create(Start start) {

        aspect = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();

        camera = new OrthographicCamera(600, 600*aspect);
        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getWidth();
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        //camera.position.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
        camera.zoom = 1f;
        camera.update();
        Button temp = buttonPool.obtain();
        addElement(temp, "testBtn", camera.viewportWidth/2f/*-50*/, camera.viewportHeight/2f/*-12.5f*/);
        Logo logo = new Logo();
        addElement(logo,"logo",camera.viewportWidth/2f, camera.viewportHeight/2f);
        temp.setDimensions(100, 25);
        temp.setAction(new ButtonAction() {
            @Override
            public void doAction(World world) {
                world.getStart().setWorld("game");
            }
        });
        temp.setText("Start Game!");
        this.start = start;
    }

    public void addElement(UIElement entity, String name, float x, float y) {
        entity.create(name, x, y, this);
        elements.put(name, entity);
    }

    public void removeElement(String name) {
        toRemove.add(name);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tickElements();
        renderElements();
        camera.update();

    }

    private void tickElements() {
        for (UIElement uiElement : elements.values()) {
            if (uiElement.getRect().contains(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x, camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y)) {
                uiElement.setHover(true);
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    uiElement.onClick(Gdx.input.getX(), Gdx.input.getY());
                }
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
}
