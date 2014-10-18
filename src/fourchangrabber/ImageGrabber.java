package fourchangrabber;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.HashMap;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class ImageGrabber extends Thread {
	private String directory;
	private String baseImageUrl;
	private String board;
	private int threadNum;
	private int sleepTime = 10000;
	private HashMap<String, String> images = null;
	private static HashSet<String> boards = null;
	private static String baseDirectory = null ;
	
	/**
	 * Updates the list of boards. This only really needs to be called once, unless
	 * 4chan manages to add/remove a board while this is running. Not likely, but
	 * still probable.
	 */
	private static void updateBoardList() {
		boards = new HashSet<>();
		try{
			URL url = new URL("http://a.4cdn.org/boards.json");
			InputStream is = url.openStream();
			JsonParser parser = Json.createParser(is);
			while(parser.hasNext()) {
				Event ev = parser.next();
				if(ev == Event.KEY_NAME) {
					switch(parser.getString()) {
					case "board":
						parser.next();
						boards.add(parser.getString());
						break;
					}
				}
			}
		} catch(MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a list of all of the images that need to be downloaded for this thread.
	 */
	private void getThreadInfo() {
		String boardUrl = "http://a.4cdn.org/" + board + "/thread/" + threadNum + ".json";
		images = new HashMap<>();
		try{
			URL url = new URL(boardUrl);
			InputStream is = url.openStream();
			JsonParser parser = Json.createParser(is);
			String ext = "", tim = "", md5 = "";
			if(!parser.hasNext()) {
				return; // TODO : set 404 status
			}
			while(parser.hasNext()) {
				Event ev = parser.next();
				if(ev == Event.KEY_NAME) {
					switch(parser.getString()) {
					case "ext":
						parser.next();
						ext = parser.getString();
						break;
					case "tim":
						parser.next();
						tim = parser.getString();
						break;
					case "md5":
						parser.next();
						md5 = parser.getString();
						// it's worth noting that "tim" always comes after "ext" and once
						// "tim" is reached, it's time to construct to file name
						// and after "md5" is reached it's time to add it to the image list
						String imageName = tim + ext;
						images.put(imageName, md5);
						break;
					}
				}
			}
		} catch(MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Downloads all images
	 * @param filename the filename to save this as
	 * @param imageUrl the URL to the image to download
	 * @param md5 the md5 hash to compare this to
	 * @throws IOException if reading the remote URL failed, or if saving the file failed
	 * @throws MismatchedHashException if the received file's hash did not match the given MD5
	 */
	private void downloadImage(String filename, URL imageUrl, String md5) throws IOException, MismatchedHashException {
		InputStream in = imageUrl.openStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1!=(n=in.read(buf))) {
		   out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		// make sure that the response matches the hash
		byte[] digest = null;
		try {
			digest = MessageDigest.getInstance("md5").digest(response);
		} catch (NoSuchAlgorithmException e) { }
		
		String digestStr = Base64.getEncoder().encodeToString(digest);
		// the digests are not equal! oh no checksum failed
		if(!digestStr.equals(md5)) {
			throw new MismatchedHashException(digestStr, md5);
		}
		FileOutputStream fileOut = new FileOutputStream(new File(directory, filename));
		fileOut.write(response);
		fileOut.close();
	}
	
	public void run() {
		if(boards == null) {
			updateBoardList();
		}
		
		if(!boards.contains(board)) {
			System.out.println(board + " doesn't exist, use just board name without slashes");
			return;
		}
		
		// grab the images that need to be downloaded
		getThreadInfo();
		
		File dir = new File(directory);
		if(!dir.exists()) {
			dir.mkdir();
		}
		
		//System.out.println("Downloading " + images.size() + " images");
		while(true) {
			for(String key : images.keySet()) {
				File imageFile = new File(directory, key);
				if(imageFile.exists())
					continue;
				System.out.print("getting " + key + " " + images.get(key) + "...");
				try {
					downloadImage(key, new URL(baseImageUrl + key), images.get(key));
					System.out.println("done.");
				} catch(IOException ex) {
					System.out.println("error.");
					System.out.println("Error message: " + ex.getMessage());
				} catch(MismatchedHashException ex) {
					System.out.println("error.");
					System.out.println("Error message: " + ex.getMessage());
				}
			}
			
			try {
				ImageGrabber.sleep(getSleepTime()); // we made the type, might as well use it
			} catch(InterruptedException ex) {
				break;
			}
		}
	}
	
	/**
	 * Instantiates a new ImageGrabber.
	 * @param board the 4chan board to grab images from
	 * @param threadNum the thread corresponding to the to download images from
	 */
	public ImageGrabber(String board, int threadNum) {
		this.board = board;
		this.threadNum = threadNum;
		directory = (new File(baseDirectory, board + "_" + threadNum)).getAbsolutePath();
		baseImageUrl = "http://i.4cdn.org/" + board + "/";
	}
	
	public String getBoard() {
		return board;
	}

	public int getThreadNum() {
		return threadNum;
	}
	
	public int getSleepTime() {
		return sleepTime;
	}
	
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
}
