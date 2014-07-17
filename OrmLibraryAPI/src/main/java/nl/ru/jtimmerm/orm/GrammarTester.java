package nl.ru.jtimmerm.orm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import nl.ru.jtimmerm.orm.grammar.ORMGrammarLexer;
import nl.ru.jtimmerm.orm.grammar.ORMGrammarParser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.stringtemplate.StringTemplate;

public class GrammarTester {

	
	/**
	 * Read a sentence from the console
	 * 
	 * @return The typed sentence
	 * @throws IOException
	 */
	private static String readSentence() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter a sentence:");
		return br.readLine();
	}

	public void testSentence() {
		try {
			System.out.println("-------------------");
			String sentence = readSentence();
			CommonTree result = parse(sentence);
			String output = treeToDot(result);
			System.out.println("Tree:\n" + output);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static CommonTree parse(String sentence) throws RecognitionException {
		ANTLRStringStream fileStream = new ANTLRStringStream(sentence);
		ORMGrammarLexer lex = new ORMGrammarLexer(fileStream);
		CommonTokenStream tokens = new CommonTokenStream(lex);
		ORMGrammarParser parser = new ORMGrammarParser(tokens);
		
		return (CommonTree) parser.verbalization().getTree();
	}
	
	private static String treeToDot(CommonTree tree) {
		DOTTreeGenerator gen = new DOTTreeGenerator();
        StringTemplate st = gen.toDOT(tree);
        return st.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GrammarTester gt = new GrammarTester();
		while (true) {
			gt.testSentence();
		}
	}

}
