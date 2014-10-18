package fourchangrabber;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;
import org.apache.commons.cli.*;

public class GrabberClient {
	public static void main(String[] args) {
		
		GnuParser parser = new GnuParser();
		Options options = new Options();
		options.addOption(new Option("c", "force-console", false, "Forces the fourchangrabber client to run in the terminal"));
		CommandLine results = null;
		try {
			results = parser.parse(options, args);
		} catch(ParseException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		
		// options
		boolean forceConsole = results.hasOption('c');
		
		
		GUIScreen gui = TerminalFacade.createGUIScreen();
		gui.getScreen().startScreen();
		GrabberWindow window = new GrabberWindow();
		gui.showWindow(window);
		gui.getScreen().stopScreen();
	}
}
