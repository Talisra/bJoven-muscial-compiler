// Generated from bJoven.g4 by ANTLR 4.4
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link bJovenParser}.
 */
public interface bJovenListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link bJovenParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull bJovenParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link bJovenParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull bJovenParser.ExpressionContext ctx);
}