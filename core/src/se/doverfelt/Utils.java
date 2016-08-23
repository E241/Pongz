package se.doverfelt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by rickard.doverfelt on 2016-08-23.
 */
public class Utils {

    private static GlyphLayout layout = new GlyphLayout();
    private static ShapeRenderer renderer = new ShapeRenderer();

    public static void renderTextInBox(float x, float y, BitmapFont font, SpriteBatch batch, String currentTooltip, Color bg, Color fg) {
        layout.setText(font, currentTooltip);

        //Render box
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(bg);
        renderer.rect(x, y,  layout.width + 20, 20 + layout.height);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(fg);
        renderer.rect(x, y,  layout.width + 20, 20 + layout.height);
        renderer.end();

        //Render text
        batch.begin();
        font.draw(batch, currentTooltip, x + 10 , y + 20);
        batch.end();
    }

    public static void renderTextInBox(float x, float y, BitmapFont font, SpriteBatch batch, String currentTooltip) {
        renderTextInBox(x, y, font, batch, currentTooltip, Color.BLACK, Color.WHITE);
    }



}
