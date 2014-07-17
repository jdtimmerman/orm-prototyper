package nl.ru.jtimmerm.orm.grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import nl.ru.jtimmerm.Lang;
import nl.ru.jtimmerm.orm.grammar.ORMGrammarParser.fact_return;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;

/**
 * Parses results from ANTLR to actual useful objects
 * 
 * @author joost
 * 
 */
public class ParseUtil {

	// ////////////////////////////////////////////////////////////////////////
	// PARSE!
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Feed the passed string to the ANTLR generated parser and return as many
	 * {@link ParseResult}s as are determined from the input
	 * 
	 * @param facts
	 *            A string containing one or more elementary fact statements,
	 *            each on a new line
	 * @return A set of {@link ParseResult}s
	 * @throws RecognitionException
	 */
	public static HashSet<ParseResult> parseSentence(String facts)
			throws RecognitionException {

		log.debug(Lang.text("parsing", facts));

		ANTLRStringStream fileStream = new ANTLRStringStream(facts);
		ORMGrammarLexer lex = new ORMGrammarLexer(fileStream);
		CommonTokenStream tokens = new CommonTokenStream(lex);
		ORMGrammarParser parser = new ORMGrammarParser(tokens);

		return parser.model().ret;
	}

	// ////////////////////////////////////////////////////////////////////////
	// FACTTYPES
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Parse the results of the fact rule to a FactType
	 * 
	 * @param r
	 * @return
	 */
	public static ParseResult parseFactType(fact_return r) {

		String sentence = getSentence((CommonTree) r.getTree());
		List<String> objects = getObjects((CommonTree) r.getTree());

		return new ParseResult(sentence, objects);

	}

	/**
	 * Returns all mentioned objects in order
	 * 
	 * @param tree
	 * @return
	 */
	public static List<String> getObjects(CommonTree tree) {

		List<String> objects = new ArrayList<String>();

		List<?> children = tree.getChildren();
		if (children != null)
			for (Object c : children) {

				CommonTree c2 = (CommonTree) c;
				switch (c2.getType()) {
				case ORMGrammarLexer.NOUN:
					objects.add(c2.getText());
					break;
				default:
					// Recurse
					objects.addAll(getObjects(c2));
					break;
				}

			}

		return objects;
	}

	private static final String WILDCARD = "%s";

	/**
	 * Creates a sentence with all verbs and put wildcards (<code>%s</code>)
	 * where objects are
	 * 
	 * @param tree
	 * @return
	 */
	private static String getSentence(CommonTree tree) {

		String sentence = tree.toString();

		// Prepend a wildcard
		if ((tree.getTokenStartIndex() < tree.token.getTokenIndex() || tree
				.getParent().getTokenStartIndex() != -1)
				&& tree.token.getTokenIndex() != 0) {
			sentence = WILDCARD + sentence;
		}
		// Append a wildcard
		if (tree.getTokenStopIndex() > tree.token.getTokenIndex()) {
			sentence += WILDCARD;
		}

		List<?> children = tree.getChildren();
		if (children != null)
			for (Object c : children) {

				CommonTree c2 = (CommonTree) c;
				switch (c2.getType()) {
				case ORMGrammarLexer.VERB:
					// Recurse
					sentence = getSentence(c2) + sentence;
					break;
				default:

					break;
				}
			}
		return sentence;
	}

	// ////////////////////////////////////////////////////////////////////////
	// LOG
	// ////////////////////////////////////////////////////////////////////////

	protected final static Logger log = Logger.getLogger(ParseUtil.class);
}
