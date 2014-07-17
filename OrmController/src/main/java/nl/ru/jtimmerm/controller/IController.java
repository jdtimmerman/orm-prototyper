package nl.ru.jtimmerm.controller;

import nl.ru.jtimmerm.orm.IModelListener;
import nl.ru.jtimmerm.view.IViewListener;

/**
 * Interface representing the controller. Provived methods to start the
 * application
 * 
 * @author joost
 * 
 */
public interface IController extends IModelListener, IViewListener {

	/**
	 * Start the application by initialing settings and objects where needed,
	 * and attaching listeners
	 */
	void start();

	/**
	 * Provided some demonstration data
	 */
	void insertDemoData();

}
