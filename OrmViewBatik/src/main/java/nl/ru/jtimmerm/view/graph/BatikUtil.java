package nl.ru.jtimmerm.view.graph;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nl.ru.jtimmerm.orm.types.IFactType;
import nl.ru.jtimmerm.orm.types.IObjectType;
import nl.ru.jtimmerm.orm.types.IType;
import nl.ru.jtimmerm.view.NotHandledException;
import nl.ru.jtimmerm.view.graph.shapes.AbstractBatikShapeElement;
import nl.ru.jtimmerm.view.graph.shapes.FactTypeSVGShape;
import nl.ru.jtimmerm.view.graph.shapes.ISVGShape;
import nl.ru.jtimmerm.view.graph.shapes.IShapeElement;
import nl.ru.jtimmerm.view.graph.shapes.SVGLine;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 * Contains utility methods for creating a SVG Document for the batik
 * implementation
 * 
 * @author joost
 * 
 */
public class BatikUtil {

	// ////////////////////////////////////////////////////////////////////////
	// DOCUMENT DOM
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Create a document based on the given mapper including all selection hints
	 * 
	 * @param mapper
	 *            The shape mapper
	 * @return The generated SVG Document
	 */
	public static SVGDocument createDocument(
			IShapeMapper<? extends IShapeElement<? extends IType>> mapper) {
		return createDocument(mapper, true);
	}

	/**
	 * @return A SVG document with only a SVG-root element
	 */
	public static SVGDocument createEmptyDocument() {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();

		SVGDocument d = (SVGDocument) impl.createDocument(
				SVGDOMImplementation.SVG_NAMESPACE_URI,
				SVGConstants.SVG_SVG_TAG, null);
		d.getRootElement().setAttribute(
				SVGConstants.SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE,
				"xMidYMid slice");

		return d;
	}

	/**
	 * Builds a document based on the contents of the mapper. Selection hints
	 * are drawn according to the appropiate flag
	 * 
	 * @param mapper
	 *            The shape mapper
	 * @param drawSelection
	 *            Whether selection hints (borders, ports) should be drawn
	 * @return The generated SVG document
	 */
	public static SVGDocument createDocument(
			IShapeMapper<? extends IShapeElement<? extends IType>> mapper,
			boolean drawSelection) {

		SVGDocument document = createEmptyDocument();
		Element rootElement = AbstractBatikShapeElement.createGroup(document,
				"root", "everything");

		// Store facts, we'll use them for lines
		HashSet<IShapeElement<IFactType>> facts = new HashSet<IShapeElement<IFactType>>();

		// Add shapes
		for (IShapeElement<? extends IType> shape : mapper.getShapes()) {

			addFactType(shape, facts);

			rootElement.appendChild(((ISVGShape<? extends IType>) shape)
					.getElement(document, drawSelection));
		}

		// Add lines
		for (Element e : createLines(document, mapper, facts))
			rootElement.appendChild(e);

		Element root = document.getRootElement();
		root.appendChild(rootElement);

		calculateSize(document);

		return document;
	}

	/**
	 * Test if the shape represents a fact type, if so add it to the collection.
	 * This method localizes the unchecked warning.
	 * 
	 * @param shape
	 *            The shape to be tested
	 * @param facts
	 *            The collection to which the shape is to be added
	 */
	@SuppressWarnings("unchecked")
	private static void addFactType(IShapeElement<? extends IType> shape,
			HashSet<IShapeElement<IFactType>> facts) {
		if (shape.getType() instanceof IFactType)
			facts.add((IShapeElement<IFactType>) shape);
	}

	// ////////////////////////////////////////////////////////////////////////
	// PARTS DOM
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Create lines between the given fact types and all relating objects.
	 * Include totality constraints where applied
	 * 
	 * @param doc
	 *            The documents to which the elements will be added
	 * @param mapper
	 *            The shape mapper
	 * @param facts
	 *            The fact types in the mapper
	 * @return A set of elements (lines and totality shapes)
	 */
	private static HashSet<Element> createLines(SVGDocument doc,
			IShapeMapper<? extends IShapeElement<? extends IType>> mapper,
			HashSet<IShapeElement<IFactType>> facts) {

		HashSet<Element> elements = new HashSet<Element>();

		// Start with the fact types
		for (IShapeElement<IFactType> factshape : facts) {

			ISVGShape<IFactType> source = (ISVGShape<IFactType>) factshape;
			List<IObjectType> objs = source.getType().getObjectTypes();

			// Loop all relations of the fact type
			for (IObjectType o : objs) {

				try {
					ISVGShape<? extends IType> dest = (ISVGShape<? extends IType>) mapper
							.getShapeFor(o);

					if (dest == null)
						continue;

					// Create a line
					SVGLine line = new SVGLine(source, dest);
					elements.add(line.getElement(doc));

					// Does a totality exist?
					if (source.getType().hasTotality(o)) {
						Element constraintShape = FactTypeSVGShape
								.createTotality(doc, line.getStart());
						elements.add(constraintShape);
					}

				} catch (NotHandledException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		return elements;

	}

	// ////////////////////////////////////////////////////////////////////////
	// DOCUMENT BOUNDS
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Used for determining the dimensions of the document
	 */
	private static BridgeContext ctx = new BridgeContext(new UserAgentAdapter());

	/**
	 * Used for determining the dimensions of the document
	 */
	private static GVTBuilder builder = new GVTBuilder();

	/**
	 * Calculate the width and height of the contents of the document and add
	 * these values to the root elemenent of the document
	 * 
	 * @param doc
	 *            The document to which its dimensions are to be added
	 */
	private static void calculateSize(SVGDocument doc) {

		if (doc.getRootElement() != null) {
			GraphicsNode gvtRoot = builder.build(ctx, doc);

			Rectangle2D rect = gvtRoot.getSensitiveBounds();

			if (rect == null)
				return;

			doc.getRootElement().setAttributeNS(null,
					SVGConstants.SVG_WIDTH_ATTRIBUTE, rect.getMaxX() + 2 + "");
			doc.getRootElement().setAttributeNS(null,
					SVGConstants.SVG_HEIGHT_ATTRIBUTE, rect.getMaxY() + 2 + "");
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// OUTPUT
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Transforms the document to text, useful for writing a file
	 * 
	 * @param doc
	 *            The document to be transformed
	 * @throws TransformerFactoryConfigurationError
	 * @throws IOException
	 * @throws TransformerException
	 * @return A string containing the XML output of the passed SVG Document
	 */
	public static String transformToString(Document doc)
			throws TransformerFactoryConfigurationError, IOException,
			TransformerException {

		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "2");

		// initialize StreamResult with File object to save to file
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		DOMSource source = new DOMSource(doc);

		transformer.transform(source, result);

		return writer.toString();

	}
}
