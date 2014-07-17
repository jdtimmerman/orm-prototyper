/**
 * 
 */
package nl.ru.jtimmerm;

import java.util.prefs.Preferences;

import nl.ru.jtimmerm.controller.Controller;
import nl.ru.jtimmerm.controller.IController;
import nl.ru.jtimmerm.orm.IModel;
import nl.ru.jtimmerm.view.BatikView;
import nl.ru.jtimmerm.view.IView;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Implements the abstract {@link Controller} to initialize with the
 * {@link BatikView}
 * 
 * @author joost
 */
public class BatikGui extends Controller {
	
	private static final Logger	log	= Logger.getLogger(BatikGui.class);
	
	/**
	 * Instantiate the BatikView
	 */
	@Override
	public IView createView(IModel model) {
		return new BatikView(model);
	}
	
	/**
	 * Run the application
	 * 
	 * @param args
	 *            Optional application arguments.
	 *            <ul>
	 *            <li>The first argument is the language (en, nl, en_US, ...)</li>
	 *            <li>The second argument enables debugging output</li>
	 *            <li>The third argument enables sample data</li>
	 *            </ul>
	 */
	public static void main(String[] args) {
		
		if (args.length > 0) {
			String arg = args[0].toLowerCase();
			
			if (arg.startsWith("-") || arg.contains("help")) {
				System.out.println("Usage:");
				System.out.println("\tjava -jar OrmPrototyper.jar [locale] [debug-level] [insertDemoData]");
				System.out.println("\t\tlocale        \tStart with the given locale (e.g. nl or en_US");
				System.out.println("\t\tdebug-level   \tLog to the console with the given level (e.g. warn, debug, info) ");
				System.out.println("\t\tinsertDemoData\tInsert an example diagram");
				return;
			}
		}
		if (args.length > 0) {
			log.info("Using locale: " + args[0]);
			Preferences.userRoot().put("lang", args[0]);
		}
		
		if (args.length > 1) {
			Level level = Level.toLevel(args[1]);
			log.info("Using log level: " + level.toString());
			Logger.getLogger(BatikGui.class.getPackage().getName()).setLevel(level);
		}
		
		// Create controller
		IController controller = new BatikGui();
		
		// And run
		controller.start();
		
		if (args.length > 2) {
			log.info("Installing demo data");
			controller.insertDemoData();
		}
	}
	
}
