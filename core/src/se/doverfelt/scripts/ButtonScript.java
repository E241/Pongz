package se.doverfelt.scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import se.doverfelt.entities.ui.ButtonAction;
import se.doverfelt.entities.ui.UIManager;
import se.doverfelt.worlds.World;

/**
 * Created by rickard on 2016-04-14.
 */
public class ButtonScript implements IScript {
    private final Viewport viewport;
    private Entity entity;
    private ButtonAction action;
    private World world;
    private boolean wasDown = false;
    private float fade = 0;
    private String tooltip = "";

    public ButtonScript(Viewport viewport, World world, ButtonAction action, String tooltip) {
        this.viewport = viewport;
        this.world = world;
        this.action = action;
        this.tooltip = tooltip;
    }

    @Override
    public void init(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void act(float delta) {
        Vector2 pos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        TransformComponent t = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent d = ComponentRetriever.get(entity, DimensionsComponent.class);
        Vector2 dim = viewport.unproject(new Vector2(d.width, d.height));
        if (pos.x > t.x && pos.x < t.x + d.width && pos.y > t.y && pos.y < t.y + d.height) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !wasDown ) {
                action.doAction(world);
            }
            ((UIManager) world).setTooltip(tooltip);
            ((UIManager) world).shouldShowTooltip(true);
        } else {
            ((UIManager) world).shouldShowTooltip(false);
        }
        wasDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
    }

    @Override
    public void dispose() {

    }
}
