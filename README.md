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
- Make sure the OpenCV library is present in your library path
- Git clone this repo
- Done

## Use

The primary way this project is meant to be used involves creating jython scripts that interacts with the function calls in the API.java file. Some example scripts are present in the project and can be executed using the following command
```
java -jar lib\jython.jar scripts\<filename>.py
```
More features are expected to be added as the project proceeds. The primary focus right now is to make the software somewhat useable.
