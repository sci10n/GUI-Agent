package me.sciion.agent.utils.parser;

public class ModifierToken extends Token{

    public ModifierToken(char value) {
	super(value);
    }

    @Override
    public TokenType getType() {
	return TokenType.MODIFIER;
    }

}
