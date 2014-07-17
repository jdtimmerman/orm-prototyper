package nl.ru.jtimmerm;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * This class is responsible for setting and loading locales and provides helper
 * function to present the strings.
 * 
 * @author joost
 * 
 */
public class Lang {

	private static Locale mCurrentLocale;
	private static ResourceBundle mMessages =null;

	/*
	 * Because of the static constructor, this member cannot be defined at the
	 * bottom of the class
	 */
	private final static Logger log = Logger.getLogger(Lang.class);

	/**
	 * Load the default language of the system
	 */
	private static void setDefaultLanguage() {
		setLanguage(Locale.getDefault().getDisplayName());
	}
	
	private static void init() {
		if (mMessages == null)
			setDefaultLanguage();
	}

	/**
	 * Set the language based on a <code>String</code>
	 * 
	 * @param locale
	 *            A string representing a locale, for example "en", "en_US",
	 *            "en_GB"
	 */
	public static void setLanguage(String locale) {

		log.debug("Setting locale " + locale);

		String[] lo = locale.split("_");
		if (lo.length == 1) {
			setLanguage(new Locale(lo[0]));

		} else if (lo.length == 2) {
			setLanguage(new Locale(lo[0], lo[1]));
		}
	}

	/**
	 * Set the language based on the provided locale
	 * 
	 * @param locale
	 *            The locale
	 */
	public static void setLanguage(Locale locale) {
		log.debug("Setting language " + locale.getLanguage() + " and country " + locale.getCountry());

		mCurrentLocale = locale;
		mMessages = ResourceBundle.getBundle(Settings.string("resource_bundle"), mCurrentLocale);
	}

	/**
	 * Get a string from the loaded locale
	 * 
	 * @param key
	 *            The key of the resource
	 * @return A translated string
	 */
	public static String text(String key) {
		init();
		return mMessages.getString(key);
	}

	/**
	 * Get a string from the loaded locale, formatted using
	 * {@link String#format(String, Object...)}
	 * 
	 * @param key
	 *            The key of the resource including wildcards
	 * @param params
	 *            The values to be placed in place of the wildcards
	 * @return A translated string with values
	 */
	public static String text(String key, Object... params) {
		init();
		String s = mMessages.getString(key);
		return String.format(s, params);
	}

}
