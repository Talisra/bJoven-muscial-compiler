// Generated from bJoven.g4 by ANTLR 4.4
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class bJovenParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NOTE=1, DURATION=2, VOLUME=3, TIMBRE=4, OCTAVE=5, BASICNOTE=6, MODE=7, 
		DIESE=8, DOUBLE_DIESE=9, BEMOL=10, DOUBLE_BEMOL=11, BECARRE=12, END=13;
	public static final String[] tokenNames = {
		"<INVALID>", "NOTE", "DURATION", "VOLUME", "TIMBRE", "OCTAVE", "BASICNOTE", 
		"MODE", "DIESE", "DOUBLE_DIESE", "BEMOL", "DOUBLE_BEMOL", "'*'", "'.'"
	};
	public static final int
		RULE_expression = 0;
	public static final String[] ruleNames = {
		"expression"
	};

	@Override
	public String getGrammarFileName() { return "bJoven.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public bJovenParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ExpressionContext extends ParserRuleContext {
		public TerminalNode END() { return getToken(bJovenParser.END, 0); }
		public List<TerminalNode> NOTE() { return getTokens(bJovenParser.NOTE); }
		public TerminalNode NOTE(int i) {
			return getToken(bJovenParser.NOTE, i);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof bJovenListener ) ((bJovenListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof bJovenListener ) ((bJovenListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(3); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(2); match(NOTE);
				}
				}
				setState(5); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==NOTE );
			setState(7); match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\17\f\4\2\t\2\3\2"+
		"\6\2\6\n\2\r\2\16\2\7\3\2\3\2\3\2\2\2\3\2\2\2\13\2\5\3\2\2\2\4\6\7\3\2"+
		"\2\5\4\3\2\2\2\6\7\3\2\2\2\7\5\3\2\2\2\7\b\3\2\2\2\b\t\3\2\2\2\t\n\7\17"+
		"\2\2\n\3\3\2\2\2\3\7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}