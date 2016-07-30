package me.sciion.agent.utils.parser;

public class LiteralToken extends Token{

    public LiteralToken(char value) {
	super(value);
    }

    @Override
    public TokenType getType() {
	return TokenType.LITERAL;
    }

}
