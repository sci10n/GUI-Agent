package me.sciion.agent.api;

import org.python.util.PythonInterpreter;

public class ScriptEngine {

    
    public ScriptEngine(){
	PythonInterpreter interpriter = new PythonInterpreter();
	interpriter.execfile("scripts/HelloWorld.py");
    }
    
    public static void main(String [] args){
	ScriptEngine e = new ScriptEngine();
    }
}
