package se.doverfelt.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import se.doverfelt.PongzStart;

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
		new LwjglApplication(new PongzStart(), config);*/

		Lwjgl3ApplicationConfiguration config3 = new Lwjgl3ApplicationConfiguration();
		config3.setTitle("Pongz");
		Graphics.DisplayMode temp = Lwjgl3ApplicationConfiguration.getDisplayMode();
		for (Graphics.DisplayMode d : Lwjgl3ApplicationConfiguration.getDisplayModes()) {
			System.out.println(d.toString());
			if ((d.refreshRate > temp.refreshRate && d.height >= temp.height) || d.height > temp.height) {
				temp = d;
			}
		}
		System.out.println(("DisplayMode: " + temp));
		config3.setFullscreenMode(temp);
		new Lwjgl3Application(new PongzStart(), config3);

	}
}
