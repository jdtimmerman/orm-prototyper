package nl.ru.jtimmerm.view;

import java.awt.Component;

/**
 * Implemented by all classes that provide a view element (that contain a
 * component)
 * 
 * @author joost
 * 
 */
public interface IComponentContainer {
	/**
	 * @return The component representing this class
	 */
	Component getComponent();
}
