// Generated from bJoven.g4 by ANTLR 4.4
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class bJovenLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NOTE=1, DURATION=2, VOLUME=3, TIMBRE=4, OCTAVE=5, BASICNOTE=6, MODE=7, 
		DIESE=8, DOUBLE_DIESE=9, BEMOL=10, DOUBLE_BEMOL=11, BECARRE=12, END=13;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'"
	};
	public static final String[] ruleNames = {
		"NOTE", "DURATION", "VOLUME", "TIMBRE", "OCTAVE", "BASICNOTE", "MODE", 
		"DIESE", "DOUBLE_DIESE", "BEMOL", "DOUBLE_BEMOL", "BECARRE", "END"
	};


	public bJovenLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "bJoven.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\17q\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\6\2\37\n\2\r\2\16\2 \3\2\6\2$\n\2\r"+
		"\2\16\2%\3\2\5\2)\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3B\n\3\3\4\3\4\3\5\6\5"+
		"G\n\5\r\5\16\5H\3\5\6\5L\n\5\r\5\16\5M\3\5\5\5Q\n\5\3\6\3\6\3\7\3\7\3"+
		"\b\3\b\3\b\3\b\3\b\5\b\\\n\b\3\t\3\t\3\n\3\n\3\n\3\n\5\nd\n\n\3\13\3\13"+
		"\3\f\3\f\3\f\3\f\5\fl\n\f\3\r\3\r\3\16\3\16\2\2\17\3\3\5\4\7\5\t\6\13"+
		"\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\3\2\6\3\2\62\63\4\2CIZZ\4"+
		"\2%%--\4\2//dd\u0080\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2"+
		"\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3"+
		"\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\3\36\3\2\2\2\5A\3\2\2\2"+
		"\7C\3\2\2\2\tF\3\2\2\2\13R\3\2\2\2\rT\3\2\2\2\17[\3\2\2\2\21]\3\2\2\2"+
		"\23c\3\2\2\2\25e\3\2\2\2\27k\3\2\2\2\31m\3\2\2\2\33o\3\2\2\2\35\37\5\5"+
		"\3\2\36\35\3\2\2\2\37 \3\2\2\2 \36\3\2\2\2 !\3\2\2\2!#\3\2\2\2\"$\5\t"+
		"\5\2#\"\3\2\2\2$%\3\2\2\2%#\3\2\2\2%&\3\2\2\2&(\3\2\2\2\')\5\7\4\2(\'"+
		"\3\2\2\2()\3\2\2\2)\4\3\2\2\2*+\7y\2\2+,\7j\2\2,-\7q\2\2-.\7n\2\2.B\7"+
		"g\2\2/\60\7j\2\2\60\61\7c\2\2\61\62\7n\2\2\62B\7h\2\2\63\64\7s\2\2\64"+
		"\65\7w\2\2\65\66\7c\2\2\66\67\7t\2\2\67B\7v\2\289\7g\2\29:\7k\2\2:;\7"+
		"i\2\2;<\7j\2\2<B\7v\2\2=>\7u\2\2>?\7k\2\2?@\7z\2\2@B\7j\2\2A*\3\2\2\2"+
		"A/\3\2\2\2A\63\3\2\2\2A8\3\2\2\2A=\3\2\2\2B\6\3\2\2\2CD\t\2\2\2D\b\3\2"+
		"\2\2EG\5\13\6\2FE\3\2\2\2GH\3\2\2\2HF\3\2\2\2HI\3\2\2\2IK\3\2\2\2JL\5"+
		"\r\7\2KJ\3\2\2\2LM\3\2\2\2MK\3\2\2\2MN\3\2\2\2NP\3\2\2\2OQ\5\17\b\2PO"+
		"\3\2\2\2PQ\3\2\2\2Q\n\3\2\2\2RS\t\2\2\2S\f\3\2\2\2TU\t\3\2\2U\16\3\2\2"+
		"\2V\\\5\21\t\2W\\\5\23\n\2X\\\5\25\13\2Y\\\5\27\f\2Z\\\5\31\r\2[V\3\2"+
		"\2\2[W\3\2\2\2[X\3\2\2\2[Y\3\2\2\2[Z\3\2\2\2\\\20\3\2\2\2]^\t\4\2\2^\22"+
		"\3\2\2\2_`\7-\2\2`d\7-\2\2ab\7%\2\2bd\7%\2\2c_\3\2\2\2ca\3\2\2\2d\24\3"+
		"\2\2\2ef\t\5\2\2f\26\3\2\2\2gh\7/\2\2hl\7/\2\2ij\7d\2\2jl\7d\2\2kg\3\2"+
		"\2\2ki\3\2\2\2l\30\3\2\2\2mn\7,\2\2n\32\3\2\2\2op\7\60\2\2p\34\3\2\2\2"+
		"\r\2 %(AHMP[ck\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}