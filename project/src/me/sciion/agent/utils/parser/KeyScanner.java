package me.sciion.agent.utils.parser;

import java.util.LinkedList;
import java.util.Stack;

public class KeyScanner {

    
    private Stack<Token> tokens;
    private LinkedList<Character> input;
    public KeyScanner(String line){
	tokens = new Stack<Token>();
	input = new LinkedList<Character>();
	for(int i = 0; i < line.length(); i++){
	    input.add(line.charAt(i));
	}
    }
    
    private char scan(){
	if(input.isEmpty()){
	    return '\0';
	}
	char c = input.pollFirst();
	return c;
    }
    
    private void putBack(char c){
	input.addFirst(c);
    }
    
    public Token next(){
	
	int state = 0;
	    char c ='\0';
	while(true){
	
	    switch(state){
	    	case 0: 
		    c = scan();
		    if(c == '\0')
			return null;
		    else if(c == 'M')
	    		state = 1;
	    	    else if(c == 'W')
	    		state = 1;
	    	    else if(c == 'S')
	    		state = 1;
	    	    else if(c == 'C')
	    		state = 1;
	    	    else
	    		return new LiteralToken(c);
		break;
	    	case 1: 
	    	    char c2 = scan();
	    	    if(c2 == '-')
	    		return new ModifierToken(c);
	    	    else{
	    		putBack(c2);
	    		state = 0;
	    		return new LiteralToken(c);
	    	    }
		default:
		    System.out.println("Error in scanner. Current char: " + c);
		    return null;
	    }
	}
    }
}
