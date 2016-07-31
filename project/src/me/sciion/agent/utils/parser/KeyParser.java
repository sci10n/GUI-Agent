package me.sciion.agent.utils.parser;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Vector;

import me.sciion.agent.utils.Key;

public class KeyParser {
    
    private LinkedList<Token> tokens;
    private Vector<Key> keyCodes;
    public KeyParser() {
	tokens = new LinkedList<Token>();
	keyCodes = new Vector<Key>();
    }

    public Vector<Key> parse(String line) {
	tokens.clear();
	KeyScanner scanner = new KeyScanner(line);

	Token t;
	do {
	    t = scanner.next();
	    if (t == null)
		break;
	    tokens.addLast(t);
	} while (t != null);
	eval();
	return keyCodes;
    }

    public void eval() {
	if (tokens.isEmpty())
	    return;
	Token token;
	do {
	    token = tokens.poll();
	    try {
		evalModifier(token);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	} while (token != null);
    }

    private void evalLiteral(Token t) throws Exception {
	if (t.getType() == TokenType.LITERAL) {
	    int c = (int) Character.toUpperCase(t.getValue());
	    if (Character.isUpperCase(t.getValue())) {
		keyCodes.add(new Key(KeyEvent.VK_SHIFT, true));
		keyCodes.add(new Key(c, true));
		keyCodes.add(new Key(c, false));
		keyCodes.add(new Key(KeyEvent.VK_SHIFT, false));
	    } else {
		keyCodes.add(new Key(c, true));
		keyCodes.add(new Key(c, false));
	    }

	} else {
	    throw new Exception();
	}
    }

    private void evalModifier(Token t) throws Exception {
	if (t == null)
	    return;
	if (t.getType() == TokenType.MODIFIER) {
	    if (t.getValue() == 'M') {
		keyCodes.add(new Key(KeyEvent.VK_ALT, true));
		evalModifier(tokens.poll());
		keyCodes.add(new Key(KeyEvent.VK_ALT, false));
	    } else if (t.getValue() == 'C') {
		keyCodes.add(new Key(KeyEvent.VK_CONTROL, true));
		evalModifier(tokens.poll());
		keyCodes.add(new Key(KeyEvent.VK_CONTROL, false));
	    }else if (t.getValue() == 'W') {
		keyCodes.add(new Key(KeyEvent.VK_WINDOWS, true));
		evalModifier(tokens.poll());
		keyCodes.add(new Key(KeyEvent.VK_WINDOWS, false));
	    }else if (t.getValue() == 'S') {
		keyCodes.add(new Key(KeyEvent.VK_SHIFT, true));
		evalModifier(tokens.poll());
		keyCodes.add(new Key(KeyEvent.VK_SHIFT, false));
	    }
	} else if (t.getType() == TokenType.LITERAL) {
	    evalLiteral(t);
	} else
	    throw new Exception();
    }
    public static void main(String [] args){
	KeyParser parser = new KeyParser();
	Robot r = null ;
	try {
	    r = new Robot();
	} catch (AWTException e) {
	    e.printStackTrace();
	}

	for(Key k: parser.parse("Hello World"))
	    if(k.isPress())
		r.keyPress(k.getCode());
	    else
		r.keyRelease(k.getCode());

    }
}
