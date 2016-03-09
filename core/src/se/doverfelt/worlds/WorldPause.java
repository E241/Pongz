package se.doverfelt.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import se.doverfelt.Start;
import se.doverfelt.entities.ui.Button;
import se.doverfelt.entities.ui.UIElement;
import se.doverfelt.entities.ui.UIManager;

import java.util.Locale;

/**
 * Created by robin.boregrim on 2016-03-09.
 */
public class WorldPause implements UIManager {

    private Start start;
    private OrthographicCamera camera;
    private float aspect;
    private Pool<Button> buttonPool = Pools.get(Button.class);
    private SpriteBatch batch;
    private BitmapFont font;
    private I18NBundle locale;
    @Override
    public void create(Start start) {
        aspect = (float) Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
        camera = new OrthographicCamera(600, 600*aspect);
        this.start = start;

        batch = new SpriteBatch();
        font = new BitmapFont();

        locale = I18NBundle.createBundle(Gdx.files.internal("lang"), Locale.getDefault());

    }

    @Override
    public void render() {

    }
    @Override
    public void setTooltip(String tooltip) {

    }

    @Override
    public void addElement(UIElement element, String name, float x, float y) {

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
