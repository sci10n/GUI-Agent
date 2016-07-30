package me.sciion.agent.utils.parser;

public abstract class Token {

    
    private char value;
    public Token(char value){
	this.value = value;
    }
    
    public char getValue(){
	return value;
    }
    
    public abstract TokenType getType();
    
}
