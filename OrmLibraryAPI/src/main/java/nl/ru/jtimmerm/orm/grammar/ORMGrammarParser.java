// $ANTLR 3.4 ORMGrammar.g 2013-07-02 11:19:58

package nl.ru.jtimmerm.orm.grammar;
import nl.ru.jtimmerm.orm.types.*;
import java.util.HashSet;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class ORMGrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NEWLINE", "NOUN", "VERB", "WS"
    };

    public static final int EOF=-1;
    public static final int NEWLINE=4;
    public static final int NOUN=5;
    public static final int VERB=6;
    public static final int WS=7;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public ORMGrammarParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public ORMGrammarParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return ORMGrammarParser.tokenNames; }
    public String getGrammarFileName() { return "ORMGrammar.g"; }


    HashSet<ParseResult> results = new HashSet<ParseResult>();


    public static class model_return extends ParserRuleReturnScope {
        public HashSet<ParseResult> ret;
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "model"
    // ORMGrammar.g:24:1: model returns [HashSet<ParseResult> ret] : ( verbalization ( NEWLINE | EOF ) )+ ;
    public final ORMGrammarParser.model_return model() throws RecognitionException {
        ORMGrammarParser.model_return retval = new ORMGrammarParser.model_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set2=null;
        ORMGrammarParser.verbalization_return verbalization1 =null;


        CommonTree set2_tree=null;

        try {
            // ORMGrammar.g:24:41: ( ( verbalization ( NEWLINE | EOF ) )+ )
            // ORMGrammar.g:25:2: ( verbalization ( NEWLINE | EOF ) )+
            {
            root_0 = (CommonTree)adaptor.nil();


            // ORMGrammar.g:25:2: ( verbalization ( NEWLINE | EOF ) )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= NOUN && LA1_0 <= VERB)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ORMGrammar.g:25:3: verbalization ( NEWLINE | EOF )
            	    {
            	    pushFollow(FOLLOW_verbalization_in_model85);
            	    verbalization1=verbalization();

            	    state._fsp--;

            	    adaptor.addChild(root_0, verbalization1.getTree());

            	    set2=(Token)input.LT(1);

            	    if ( input.LA(1)==EOF||input.LA(1)==NEWLINE ) {
            	        input.consume();
            	        adaptor.addChild(root_0, 
            	        (CommonTree)adaptor.create(set2)
            	        );
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


             retval.ret =results;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "model"


    public static class verbalization_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "verbalization"
    // ORMGrammar.g:29:1: verbalization : f= fact ;
    public final ORMGrammarParser.verbalization_return verbalization() throws RecognitionException {
        ORMGrammarParser.verbalization_return retval = new ORMGrammarParser.verbalization_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        ORMGrammarParser.fact_return f =null;



        try {
            // ORMGrammar.g:29:14: (f= fact )
            // ORMGrammar.g:30:5: f= fact
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_fact_in_verbalization115);
            f=fact();

            state._fsp--;

            adaptor.addChild(root_0, f.getTree());

             results.add(ParseUtil.parseFactType(f)); 

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "verbalization"


    public static class fact_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "fact"
    // ORMGrammar.g:34:1: fact : (a= ( NOUN | VERB ^) )+ ;
    public final ORMGrammarParser.fact_return fact() throws RecognitionException {
        ORMGrammarParser.fact_return retval = new ORMGrammarParser.fact_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token a=null;
        Token NOUN3=null;
        Token VERB4=null;

        CommonTree a_tree=null;
        CommonTree NOUN3_tree=null;
        CommonTree VERB4_tree=null;

        try {
            // ORMGrammar.g:34:6: ( (a= ( NOUN | VERB ^) )+ )
            // ORMGrammar.g:36:2: (a= ( NOUN | VERB ^) )+
            {
            root_0 = (CommonTree)adaptor.nil();


            // ORMGrammar.g:36:4: (a= ( NOUN | VERB ^) )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0 >= NOUN && LA3_0 <= VERB)) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ORMGrammar.g:36:4: a= ( NOUN | VERB ^)
            	    {
            	    // ORMGrammar.g:36:6: ( NOUN | VERB ^)
            	    int alt2=2;
            	    int LA2_0 = input.LA(1);

            	    if ( (LA2_0==NOUN) ) {
            	        alt2=1;
            	    }
            	    else if ( (LA2_0==VERB) ) {
            	        alt2=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 2, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt2) {
            	        case 1 :
            	            // ORMGrammar.g:36:7: NOUN
            	            {
            	            NOUN3=(Token)match(input,NOUN,FOLLOW_NOUN_in_fact139); 
            	            NOUN3_tree = 
            	            (CommonTree)adaptor.create(NOUN3)
            	            ;
            	            adaptor.addChild(root_0, NOUN3_tree);


            	            }
            	            break;
            	        case 2 :
            	            // ORMGrammar.g:36:14: VERB ^
            	            {
            	            VERB4=(Token)match(input,VERB,FOLLOW_VERB_in_fact143); 
            	            VERB4_tree = 
            	            (CommonTree)adaptor.create(VERB4)
            	            ;
            	            root_0 = (CommonTree)adaptor.becomeRoot(VERB4_tree, root_0);


            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "fact"

    // Delegated rules


 

    public static final BitSet FOLLOW_verbalization_in_model85 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_set_in_model87 = new BitSet(new long[]{0x0000000000000062L});
    public static final BitSet FOLLOW_fact_in_verbalization115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOUN_in_fact139 = new BitSet(new long[]{0x0000000000000062L});
    public static final BitSet FOLLOW_VERB_in_fact143 = new BitSet(new long[]{0x0000000000000062L});

}