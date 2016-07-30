package me.sciion.agent.utils;

public class Key {

    private boolean press;
    private int code;
    
    public Key(int code, boolean press){
	this.code = code;
	this.press = press;
    }

    public boolean isPress() {
        return press;
    }

    public int getCode() {
        return code;
    }
    
    
    
}
