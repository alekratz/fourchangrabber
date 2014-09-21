package fourchangrabber;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui.*;

public class GrabberWindow extends Window {
	public GrabberWindow() {
		super("fourchangrabber");
	}
	
	public static void main(String[] args) {
		GUIScreen gui = TerminalFacade.createGUIScreen();
		GrabberWindow window = new GrabberWindow();
		gui.getScreen().startScreen();
		gui.showWindow(window);
		gui.getScreen().stopScreen();
	}
}
