package se.doverfelt.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.FitViewport;
//import com.sun.xml.internal.ws.api.pipe.Engine;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;
import se.doverfelt.Start;
import se.doverfelt.entities.ui.*;
import se.doverfelt.scripts.ButtonScript;
import se.doverfelt.scripts.MouseFollower;

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
    private SceneLoader sl;
    private ItemWrapper root;
    private Sprite bg;

    @Override
    public void create(final Start start) {

        aspect = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();

        camera = new OrthographicCamera(800, 600*aspect);
        //camera.viewportWidth = Gdx.graphics.getWidth();
        //camera.viewportHeight = Gdx.graphics.getWidth();
        //camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        //camera.position.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
        camera.zoom = 1f;
        camera.update();

        /*Button temp = buttonPool.obtain();
        addElement(temp, "backBtn", 10, camera.viewportHeight - 35);
        temp.setDimensions(25, 25);
        temp.setAction(new ButtonAction() {
            @Override
            public void doAction(World world) {
                world.getStart().setWorld("menu");
            }
        });
        temp.setIcon("backArrow.png");*/

        FitViewport viewport = new FitViewport(1920, 1080);
        sl = new SceneLoader();
        sl.loadScene("Options", viewport);

        root = new ItemWrapper(sl.rootEntity);
        root.getChild("mouseLight").addScript(new MouseFollower(viewport));
        root.getChild("BackBtn").addScript(new ButtonScript(viewport, this, new ButtonAction() {
            @Override
            public void doAction(World world) {
                Start.getPreferences().flush();
                world.getStart().setWorld("menu");
            }
        }));
        LabelComponent label = ComponentRetriever.get(root.getChild("ParentMode").getChild("BtnLabel").getEntity(), LabelComponent.class);
        label.setText("Parent Mode: " + (Start.getPreferences().getBoolean("paddleBounds") ? "On" : "Off"));
        root.getChild("ParentMode").addScript(new ButtonScript(viewport, this, new ButtonAction() {
            @Override
            public void doAction(World world) {
                Start.getPreferences().putBoolean("paddleBounds", !Start.getPreferences().getBoolean("paddleBounds"));
                LabelComponent label = ComponentRetriever.get(root.getChild("ParentMode").getChild("BtnLabel").getEntity(), LabelComponent.class);
                label.setText("Parent Mode: " + (Start.getPreferences().getBoolean("paddleBounds") ? "On" : "Off"));

            }
        }));
        bg = new Sprite(new Texture("bg.png"));

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
        sl.getBatch().begin();
        //bg.setSize(Gdx.graphics.getWidth()*2, Gdx.graphics.getWidth()*2);
        //bg.draw(sl.getBatch());
        sl.getBatch().end();
        sl.getEngine().update(Gdx.graphics.getDeltaTime());
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
