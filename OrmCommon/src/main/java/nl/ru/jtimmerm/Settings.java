package nl.ru.jtimmerm;

import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Loads settings and provides helper methods to retrieve them
 * 
 * @author joost
 * 
 */
public class Settings {

	/**
	 * The one hard coded setting to rule 'em all!
	 */
	private static final String PROPERTIES_FILE = "/settings.properties";

	/**
	 * This contains the loaded properties
	 */
	private static Properties mProperties = new Properties();

	/*
	 * Because of the static constructor, this member cannot be defined at the
	 * bottom of the class
	 */
	private final static Logger log = Logger.getLogger(Settings.class);

	/**
	 * Load the settings from the properties file
	 */
	static {
		try {
			// Can't translate this, because the languages uses the settings
			// class
			log.debug("Loading properties from " + PROPERTIES_FILE);

			mProperties.load(Settings.class
					.getResourceAsStream(PROPERTIES_FILE));
		} catch (Exception e) {
			Error.fatalError(
					"Could not load settings file: " + PROPERTIES_FILE, e);
		}
	}

	/**
	 * Load a setting as a string
	 * 
	 * @param key
	 *            The key of the setting
	 * @return The string value of the setting
	 */
	public static String string(Object key) {
		return (String) mProperties.get(key);
	}

	/**
	 * Load a setting as an object
	 * 
	 * @param key
	 *            The key of the setting
	 * @return The value of the setting a an object
	 */
	public static Object object(Object key) {
		return mProperties.get(key);
	}

	/**
	 * Load a setting as an integer
	 * 
	 * @param key
	 *            The key of the setting
	 * @return The value of the setting parsed as an integer
	 */
	public static int integer(Object key) {
		return Integer.valueOf(string(key));
	}

	/**
	 * Load a setting as a long
	 * 
	 * @param key
	 *            The key of the setting
	 * @return The value of the setting parsed as a long
	 */
	public static long _long(Object key) {
		return Long.valueOf(string(key));
	}

	/**
	 * Load a setting as a boolean
	 * 
	 * @param key
	 *            The key of the setting
	 * @return The value of the setting parsed as a boolean
	 */
	public static boolean bool(Object key) {
		return Boolean.valueOf(string(key));
	}

}
