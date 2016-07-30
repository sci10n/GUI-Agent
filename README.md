# GUI-Agent
"Scripting user activity is a hard thing to do" - me

## About
This project aims at creating a system for scripting user activity inside computer systems using computer vision and peripheral devices. The technologies used are the following

- OpenCV
- Jython
- Java
- GraphLib

Tons of inspiration was taken from the Sikuli project and until I get on good terms with VirtualBox SDK this project can be seen as a inferior version of Sikuli. THe goal is to enable automization of guest systems in VirtualBox

## Install
All you have to do are the following esoteric tasks taken right out of M. C. Escher’s wildest nightmares:
- Install java
- Download OpenCV
- Make sure the OpenCV library is present in your library path, on Windows this is your PATH varaible
- Git clone this repo
- Done

## Use

The primary way this project is meant to be used involves creating jython scripts that interacts with the function calls in the API.java file. Some example scripts are present in the project and can be executed using the following command
```
java -jar lib\jython.jar scripts\<filename>.py
```
More features are expected to be added as the project proceeds. The primary focus right now is to make the software somewhat useable.

Keep in mind that all files referenced in the scripts are relative to the current working dir

###Keyboard interactions
This has proven to be the hardest nut to crack (apart from the entire "computer vision" thing). I searched for good tools but couldn't find anything I liked. I decided to use what already was present in the java environment and this resulted in the java Robot class. 

I've built a text parser to spare the user from scripting the robot by itself. The API can now take a string following the EMACS style of expressing key-combinations. That is, for example `ctr+c` and `ctr+v` being represented as the string:
```
C-cC-v
```
and more complex stuff like typing "Hello World" either being done by:
```
Hello World
```
but also using shift modifier
```
S-hello S-world
```
this way has the added advantage of enabling more than the key codes that have dedicated keyboard-keys. For example the "!" character hiding under "1"
```
S-1
```
The full list of modifiers are the following:
```
S- Shift
W- Windows
C- Control 
M- Alt
```

When `API.type()` is called the call finishes with a `ENTER`. 

As with everything the keyboard parser is a work in progress and does not support scoping more than one key to a modifier. 
