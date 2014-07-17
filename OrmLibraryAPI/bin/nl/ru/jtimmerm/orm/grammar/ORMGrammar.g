grammar ORMGrammar;

options {
    language     = Java;
    output       = AST;
    ASTLabelType = CommonTree;
}

@header {
package nl.ru.jtimmerm.orm.grammar;
import nl.ru.jtimmerm.orm.types.*;
import java.util.HashSet;
}

@lexer::header {
package nl.ru.jtimmerm.orm.grammar;
}

@members {
HashSet<ParseResult> results = new HashSet<ParseResult>();
}

// Define a 1 sentence per line model 
model returns [HashSet<ParseResult> ret]:
	(verbalization (NEWLINE | EOF) )+ { $ret=results;} 
	;

// Verbalization can only be facts at this moment
verbalization:
    f=fact { results.add(ParseUtil.parseFactType(f)); }
    ;

// Structure of a fact
fact :
	//a=(NOUN VERB^ (NOUN (VERB^ NOUN)*)?)
	a = (NOUN | VERB^ )+
	;

// Tokens
// Nouns start with capitials. Then it can be any character (to allow CamelCase for example)
NOUN : ('A'..'Z')('a'..'z'|'A'..'Z')* ; 

// Verbs are lowercase characters or spaces (to allow senteces)
VERB : ('a'..'z' | ' ' )+;

// Whitespace
WS		: ( '\t' | ' ' | '\u000C' )+ { $channel = HIDDEN; } ;
NEWLINE	: ( '\r' | '\n' )+ { };
