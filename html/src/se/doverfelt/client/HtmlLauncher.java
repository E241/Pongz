package se.doverfelt.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import se.doverfelt.Start;

public class HtmlLauncher extends GwtApplication {

        private ApplicationListener applicationListener;

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener getApplicationListener() {
                return applicationListener;
        }

        @Override
        public ApplicationListener createApplicationListener() {
            applicationListener = new Start(new GWTEffectHandler());
            return applicationListener;
        }
}