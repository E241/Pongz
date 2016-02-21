package se.doverfelt.entities.ui;

import com.badlogic.gdx.math.Rectangle;
import se.doverfelt.entities.Ploppable;

/**
 * @author Rickard Doverfelt
 *         Datum: 2016-02-21
 *         Filnamn: UIElement.java
 */
public interface UIElement extends Ploppable {

    Rectangle getRect();
    void onClick(float x, float y);
    void setHover(boolean hovering);

}
