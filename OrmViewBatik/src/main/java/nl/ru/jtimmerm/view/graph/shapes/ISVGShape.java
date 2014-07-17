package nl.ru.jtimmerm.view.graph.shapes;

import nl.ru.jtimmerm.Settings;
import nl.ru.jtimmerm.orm.types.IType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Provides a contract for SVG based graphs by allowing to return XML-dom
 * elements
 * 
 * @author joost
 * 
 * @param <T>
 */
public interface ISVGShape<T extends IType> extends IShapeElement<T> {

	/**
	 * Create a SVG Element, by default draw selections
	 * 
	 * @param doc
	 *            The document for which the element should be available
	 * @return The created element
	 */
	Element getElement(Document doc);

	/**
	 * Create a SVG Element, whether selections are drawn is indicated by
	 * <code>drawSelection</code>
	 * 
	 * @param doc
	 *            The document for which the element should be available
	 * @param drawSelection
	 *            Whether or not to include selection hints
	 * @return The created element
	 */
	Element getElement(Document doc, boolean drawSelection);

	/**
	 * Font size for text
	 */
	static int FONT_SIZE = Settings.integer("shape_font_size");

	/**
	 * Width of the lines in pixels
	 */
	static int LINE_WIDTH = Settings.integer("shape_line_width");

}
