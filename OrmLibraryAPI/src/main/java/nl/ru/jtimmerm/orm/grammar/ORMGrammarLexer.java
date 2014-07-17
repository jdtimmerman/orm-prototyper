// $ANTLR 3.4 ORMGrammar.g 2013-07-02 11:19:58

package nl.ru.jtimmerm.orm.grammar;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class ORMGrammarLexer extends Lexer {
    public static final int EOF=-1;
    public static final int NEWLINE=4;
    public static final int NOUN=5;
    public static final int VERB=6;
    public static final int WS=7;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public ORMGrammarLexer() {} 
    public ORMGrammarLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public ORMGrammarLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "ORMGrammar.g"; }

    // $ANTLR start "NOUN"
    public final void mNOUN() throws RecognitionException {
        try {
            int _type = NOUN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ORMGrammar.g:41:6: ( ( 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' )* )
            // ORMGrammar.g:41:8: ( 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // ORMGrammar.g:41:18: ( 'a' .. 'z' | 'A' .. 'Z' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= 'A' && LA1_0 <= 'Z')||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ORMGrammar.g:
            	    {
            	    if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NOUN"

    // $ANTLR start "VERB"
    public final void mVERB() throws RecognitionException {
        try {
            int _type = VERB;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ORMGrammar.g:44:6: ( ( 'a' .. 'z' | ' ' )+ )
            // ORMGrammar.g:44:8: ( 'a' .. 'z' | ' ' )+
            {
            // ORMGrammar.g:44:8: ( 'a' .. 'z' | ' ' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==' '||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // ORMGrammar.g:
            	    {
            	    if ( input.LA(1)==' '||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VERB"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ORMGrammar.g:47:5: ( ( '\\t' | ' ' | '\\u000C' )+ )
            // ORMGrammar.g:47:7: ( '\\t' | ' ' | '\\u000C' )+
            {
            // ORMGrammar.g:47:7: ( '\\t' | ' ' | '\\u000C' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='\t'||LA3_0=='\f'||LA3_0==' ') ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ORMGrammar.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)=='\f'||input.LA(1)==' ' ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
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


             _channel = HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ORMGrammar.g:48:9: ( ( '\\r' | '\\n' )+ )
            // ORMGrammar.g:48:11: ( '\\r' | '\\n' )+
            {
            // ORMGrammar.g:48:11: ( '\\r' | '\\n' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0=='\n'||LA4_0=='\r') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // ORMGrammar.g:
            	    {
            	    if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


             

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NEWLINE"

    public void mTokens() throws RecognitionException {
        // ORMGrammar.g:1:8: ( NOUN | VERB | WS | NEWLINE )
        int alt5=4;
        alt5 = dfa5.predict(input);
        switch (alt5) {
            case 1 :
                // ORMGrammar.g:1:10: NOUN
                {
                mNOUN(); 


                }
                break;
            case 2 :
                // ORMGrammar.g:1:15: VERB
                {
                mVERB(); 


                }
                break;
            case 3 :
                // ORMGrammar.g:1:20: WS
                {
                mWS(); 


                }
                break;
            case 4 :
                // ORMGrammar.g:1:23: NEWLINE
                {
                mNEWLINE(); 


                }
                break;

        }

    }


    protected DFA5 dfa5 = new DFA5(this);
    static final String DFA5_eotS =
        "\2\uffff\1\3\3\uffff";
    static final String DFA5_eofS =
        "\6\uffff";
    static final String DFA5_minS =
        "\1\11\1\uffff\1\11\3\uffff";
    static final String DFA5_maxS =
        "\1\172\1\uffff\1\40\3\uffff";
    static final String DFA5_acceptS =
        "\1\uffff\1\1\1\uffff\1\2\1\3\1\4";
    static final String DFA5_specialS =
        "\6\uffff}>";
    static final String[] DFA5_transitionS = {
            "\1\4\1\5\1\uffff\1\4\1\5\22\uffff\1\2\40\uffff\32\1\6\uffff"+
            "\32\3",
            "",
            "\1\4\2\uffff\1\4\23\uffff\1\2",
            "",
            "",
            ""
    };

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( NOUN | VERB | WS | NEWLINE );";
        }
    }
 

}