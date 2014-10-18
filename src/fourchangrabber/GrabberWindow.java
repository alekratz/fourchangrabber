package fourchangrabber;

import com.googlecode.lanterna.gui.*;
import com.googlecode.lanterna.gui.layout.*;
import com.googlecode.lanterna.gui.component.*;
import com.googlecode.lanterna.input.*;

public class GrabberWindow extends Window {
	
	private ActionListBox listBox;
	
	public GrabberWindow() {
		super("fourchangrabber");
		
		// Add components
		Panel mainPanel = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
		Panel listPanel = new Panel(new Border.Standard(), Panel.Orientation.HORISONTAL);
		Panel helpPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
		
		// Listbox and panel
		listPanel.setAlignment(Component.Alignment.FILL);
		listPanel.setLayoutManager(new VerticalLayout());
		listBox = new ActionListBox();
		listBox.setAlignment(Component.Alignment.FILL);
		listPanel.addComponent(listBox);
		
		// Main panel
		mainPanel.setLayoutManager(new VerticalLayout());
		mainPanel.addComponent(listPanel);
		mainPanel.addComponent(helpPanel);
		mainPanel.setAlignment(Component.Alignment.FILL);
		addComponent(mainPanel);
	}
	
	@Override
	public void onKeyPressed(Key key) {
		switch(key.getKind()) {
		case Escape:
			close();
			break;
		default:
			// pass keypress event onto the window
			super.onKeyPressed(key);
			break;
		}
	}
}
