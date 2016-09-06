package se.doverfelt.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.I18NBundle;
import se.doverfelt.Start;
import se.doverfelt.effects.Effect;
import se.doverfelt.worlds.World;
import se.doverfelt.worlds.WorldPongz;

import java.util.Locale;

/**
 * Created by rickard.doverfelt on 2016-05-25.
 */
public class EffectHUD implements Ploppable {
    private Effect effect;
    float x, y;
    private World world;
    private BitmapFont font;
    private I18NBundle local;
    private String name;
    private Sprite sprite;

    public EffectHUD(World world) {
        this.world = world;
    }

    @Override
    public void create(String name, float x, float y, World world) {
        this.x = x;
        this.y = y;
        this.world = world;
        font = new BitmapFont();
        local = I18NBundle.createBundle(Gdx.files.internal("lang"), new Locale(Start.getPreferences().getString("lang")));
        this.name = name;
        //sprite = new Sprite(new Texture("ball.png"));
        //sprite.setSize(4, 4);
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
        if (world.getAssetManager().isLoaded(effect.getEffectType() + ".png")) {
            this.sprite = new Sprite(world.getAssetManager().get(effect.getEffectType() + ".png", Texture.class));
        } else {
            sprite = new Sprite(new Texture("badlogic.png"));
        }
        sprite.setSize(4, 4);
    }

    @Override
    public void render(OrthographicCamera camera, SpriteBatch batch) {
        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.WHITE);
        renderer.rect(x - 0.5f, y - 0.5f, 41f, 5f);
        renderer.setColor(new Color(0, 0.1f, 0, 1));
        renderer.rect(x, y, 40, 4);
        renderer.setColor(Color.GREEN);
        renderer.rect(x, y, 40*((float)effect.currentTime()/(float)effect.totalTime()), 4);
        renderer.end();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        sprite.setPosition(x + 20 - sprite.getWidth() / 2f, y + 2 - sprite.getHeight()/2f);
        sprite.draw(batch);
        batch.end();

        /*world.getStart().getFontBatch().begin();
        font.draw(world.getStart().getFontBatch(), local.get(effect.getEffectType()), x + 210 + font.getSpaceWidth()*local.get(effect.getEffectType()).length(), y + 12.5f);
        world.getStart().getFontBatch().end();*/
    }

    @Override
    public void update(float delta) {
        if (effect.currentTime() >= effect.totalTime()) {
            ((WorldPongz)world).removeEntity(name);
            if (effect.isSided()) {
                if (effect.isLeft()) {
                    ((WorldPongz)world).removeHUD(name, "left");
                } else {
                    ((WorldPongz)world).removeHUD(name, "right");
                }
            } else {
                ((WorldPongz)world).removeHUD(name, "center");
            }
        }
    }

    @Override
    public void dispose() {

    }

    public Effect getEffect() {
        return effect;
    }

    public String getName() {
        return name;
    }
}
