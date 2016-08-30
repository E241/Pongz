package se.doverfelt.entities.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;
import se.doverfelt.Start;
import se.doverfelt.worlds.World;

import java.util.Locale;

/**
 * Created by rickard.doverfelt on 2016-08-30.
 */
public class ToggleButton implements ButtonAction {

    private final ItemWrapper root;
    private final I18NBundle locale;
    private final String title;
    private final String option;
    private final String child;

    public ToggleButton(ItemWrapper root, String child, String option, String title) {
        this.root = root;
        this.locale = I18NBundle.createBundle(Gdx.files.internal("lang"), Locale.forLanguageTag(Start.getPreferences().getString("lang")));
        this.title = title;
        this.option = option;
        this.child = child;
    }

    @Override
    public void doAction(World world) {
        Start.getPreferences().putBoolean(option, !Start.getPreferences().getBoolean(option));
        LabelComponent label = ComponentRetriever.get(root.getChild(child).getChild("BtnLabel").getEntity(), LabelComponent.class);
        label.setText(locale.get(title) + ": " + (Start.getPreferences().getBoolean(option) ? "On" : "Off"));
    }
}
