package se.doverfelt.worlds;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by rickard on 2016-04-13.
 */
public class MouseFollower implements IScript {
    private final Viewport viewport;
    private Entity entity;

    public MouseFollower(Viewport viewport) {
        this.viewport = viewport;
    }

    @Override
    public void init(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void act(float delta) {
        TransformComponent t = ComponentRetriever.get(entity, TransformComponent.class);
        t.x = viewport.unproject(new Vector2(Gdx.input.getX(), 0)).x;
        t.y = viewport.unproject(new Vector2(0, Gdx.input.getY())).y;
    }

    @Override
    public void dispose() {

    }
}
