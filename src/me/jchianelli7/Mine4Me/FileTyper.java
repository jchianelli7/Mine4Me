package me.jchianelli7.Mine4Me;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.glass.events.KeyEvent;

public class FileTyper {

	private FileTyperThread thread;

	public FileTyper() {
	}

	public void run(String fileName) {
		thread = new FileTyperThread("FileTyper", fileName);
		thread.start();
	}
	
	public void stop() {
		thread.interrupt();
	}
	
	public boolean isRunning() {
		if(thread == null) {
			return false;
		}
		return thread.isAlive();
	}

}

class FileTyperThread extends Thread {
	
	private final String fileName;
	
	public FileTyperThread(String name, String fileName) {
		super(name);
		this.fileName = fileName;
	}

	public void run() {
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResource("files/" + fileName).openStream()));
			
			try {
				
				Robot bot = new Robot();

				String line;
				line = in.readLine();
				
				Thread.sleep(3000);

				while (line != null) {
					if(Thread.currentThread().isInterrupted()) {
						break;
					}
					
					System.out.println("Typing: " + line);

					for (int i = 0; i < line.length(); i++) {
						char c = Character.toUpperCase(line.charAt(i));
						
						bot.keyPress((int) KeyEvent.class.getField("VK_" + c).getInt(null));
						bot.keyRelease((int) KeyEvent.class.getField("VK_" + c).getInt(null));
						Thread.sleep((int) Miner.instance.betweenChars.getValue());
					}
					
					bot.keyPress(KeyEvent.VK_ENTER);
					Thread.sleep((int) Miner.instance.betweenLines.getValue());
					line = in.readLine();
				}

			} catch (AWTException | IOException | InterruptedException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			} finally {
				in.close();
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
