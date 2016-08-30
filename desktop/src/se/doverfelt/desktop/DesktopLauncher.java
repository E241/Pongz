package se.doverfelt.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import se.doverfelt.Start;

public class DesktopLauncher {
	public static void main (String[] arg) {
	/*	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
		//config.vSyncEnabled = false;
		//config.backgroundFPS = 144;
		//config.foregroundFPS = 144;
		config.title = "Pongz";
		Graphics.DisplayMode temp = LwjglApplicationConfiguration.getDesktopDisplayMode();
		for (Graphics.DisplayMode d : LwjglApplicationConfiguration.getDisplayModes()) {
			System.out.println(d.toString());
			if ((d.refreshRate > temp.refreshRate && d.height >= temp.height) || d.height > temp.height) {
				temp = d;
			}
		}
		config.setFromDisplayMode(temp);
		System.out.println(("DisplayMode: " + temp));
		//System.out.println("Config: " +  config.toString());
		new LwjglApplication(new WorldPongz(), config);*/

		Lwjgl3ApplicationConfiguration config3 = new Lwjgl3ApplicationConfiguration();
		config3.setTitle("Pongz");
		Graphics.DisplayMode temp = Lwjgl3ApplicationConfiguration.getDisplayMode();
		/*for (Graphics.DisplayMode d : Lwjgl3ApplicationConfiguration.getDisplayModes()) {
			System.out.println(d.toString());
			if ((d.refreshRate > temp.refreshRate && d.height >= temp.height && d.width >= temp.width) || d.height > temp.height && d.width > temp.width) {
				temp = d;
			}
		}
		System.out.println(("DisplayMode: " + temp));*/
		config3.setFullscreenMode(temp);
		//config3.setWindowedMode(1280, 720);
		new Lwjgl3Application(new Start(new EffectHandler()), config3);

	}
}
