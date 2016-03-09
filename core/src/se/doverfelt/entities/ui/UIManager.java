package se.doverfelt.entities.ui;

import se.doverfelt.worlds.World;

/**
 * Created by rickard on 2016-03-09.
 */
public interface UIManager extends World {
    void setTooltip(String tooltip);
    void addElement(UIElement element, String name, float x, float y);
    void removeElement(String name);
}
