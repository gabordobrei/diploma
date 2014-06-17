package net.gabordobrei.diploma;
import net.gabordobrei.diploma.logger.Logger;

public class Main {

	final static Logger log = new Logger(Main.class);

	public static void main(String args[]) {

		Logger.setLevel(Logger.DEBUG);

		/*- log.setPrintStream(new PrintStream(new FileOutputStream("log.txt", false))); */

	}

}
