package fourchangrabber;

import java.util.HashSet;

public class GrabberDaemon extends Thread {
	private HashSet<ImageGrabber> grabbers = new HashSet<>();
	
	public GrabberDaemon() {
		setDaemon(true);
	}
	
	/**
	 * Adds a grabber to be watched by the daemon.
	 * @param grabber the grabber
	 */
	public void addGrabber(ImageGrabber grabber) {
		
	}
	
	public void run() {
		
	}
}
