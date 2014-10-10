package fourchangrabber;

import java.util.HashSet;

public class GrabberDaemon extends Thread {
	private HashSet<ImageGrabber> grabbers = new HashSet<>();
	
	public GrabberDaemon() {
		setDaemon(true);
	}
	
	/**
	 * Adds a grabber to be watched by the daemon.
	 * @param grabber the grabber to add
	 */
	public void addGrabber(ImageGrabber grabber) {
		assert(!grabber.isAlive()); // gonna have to force this one
		grabbers.add(grabber);
		grabber.start(); // start the grabber as soon as it's added
	}
	
	public void run() {
		try {
			while(true) {
				GrabberDaemon.sleep(10);
			}
		} catch(InterruptedException ex) {
		}
	}
}
