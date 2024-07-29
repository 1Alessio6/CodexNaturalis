# Codex Naturalis

## Software Engineering Project YRS. 2023/2024

<p align="right">
<img align="right" width="400" height="300" src="https://github.com/RiccardoCerberi/IS24-AM34/blob/main/deliveries/git/codex_game.jpeg">

Codex Naturalis is the implementation of the homonymous game 'Codex Naturalis' designed by Cranio Creations.

**Professor:** Alessandro Margara

**Team:**
- ### Alessio Ginolfi (https://github.com/1Alessio6)<br>alessio.ginolfi@mail.polimi.it
- ### Carlo Aspren Gines Lara (https://github.com/chuckdotsvg)<br>carloaspren.gines@mail.polimi.it
- ### Natalia Daniello Lopez (https://github.com/audlo)<br>natalia.daniello@mail.polimi.it
- ### Riccardo Cerberi (https://github.com/RiccardoCerberi)<br>riccardo.cerberi@mail.polimi.it
[//]: <> (todo add person code and github nickname)
  
</p>

## Table of Contents

1. [About the game](#about-the-game)
2. [Project specifications](#project-specifications)
   * [Implemented features](#implemented-features)
3. [How to use](#how-to-use)
4. [Documentation](#documentation)

## About the game

Learn more about the game at [Bombyx][1]

## Project specifications

This project complies with **all the rules** established by the game, **TUI**, **GUI**, **RMI**, **Socket** and implements the following **advanced features** (*Coming soon*)

### Implemented features

| Feature                      | State |
|------------------------------|:-----:|
| Resilience to disconnections |  ✔️   |
| Chat                         |  ✔️   |

**Legend:**
➖ Implementing
✔️ Implemented

**Meaning:**

- Resilience to disconnections: If players are disconnected from the game due to network issues or a client crash, they can rejoin and recover all their information. During their disconnection, their turns are skipped. If only one player remains connected, the game will pause and a timer will start. When the game resumes, the timer will be canceled. If the timer runs out, the remaining player (if any) will be declared the winner.

- Chat: a chat is available to allow players to communicate with each other.

## How to use

### Overview 

The project is a distributed system based on a client-server model. The server manages the game's data, while the client handles the user's input, which is then sent to the server for processing.

### Run the server

To run the server, the following options must be provided, in order:
- Server IP 
- Port for socket communication
- Port for rmi communication

For example, the command 

    java -jar AM34-1.0-SNAPSHOT-server.jar 127.0.0.1 1234 1235

sets the server ip to 127.0.0.1 (localhost) and the socket and rmi port to 1234 and 1235, respectively.

If no arguments are provided, by default the application starts with the options indicated above.

### Run the client

To run the client, the following options must be provided, in order:
- Communication protocol, either *rmi* or *socket*
- Client IP 
- User interface, either *gui* or *tui* 

For example, the command 

    java -jar AM34-1.0-SNAPSHOT-client.jar socket 127.0.0.1 gui

specifies the socket protocol, the client ip 127.0.0.1 and the graphical user interface.

If no arguments are provided, by default the application starts with the options indicated above.

#### Settings for the TUI

* If you are a windows user and you want to use our TUI interface, please follow these steps:

1. Open your command prompt
2. Change the code page using the command: chcp 65001
3. Use a character size equal to 8.5

* If you are a Linux user, please, open your screen in full screen mode and make sure that the number of lines is greater or equal to 50 and that the number of columns is greater or equal to 190

## Documentation

The following documentation can be found:

* [initial UML diagram][6].
* final UML diagram.
* [communication protocol][8].
* [Peer-reviews][9].
* [javadoc][]
* [view documentation][14]

 ## Credits
 * [Gson][10]
 * [Overleaf][11]
 * [JUnit][12]


[1]:https://studiobombyx.com/en/jeu/codex-naturalis-2/                             "Bombyx"
[2]:https://github.com/RiccardoCerberi 
[3]:https://github.com/chuckdotsvg
[4]:https://github.com/1Alessio6
[5]:https://github.com/audlo
[6]:https://github.com/RiccardoCerberi/IS24-AM34/tree/1-uml/deliveries/UML         "initial UML diagram"
[8]:https://github.com/RiccardoCerberi/IS24-AM34/blob/main/deliveries/communication%20protocol/final%20communication%20protocol.pdf
[9]:https://github.com/RiccardoCerberi/IS24-AM34/tree/main/deliveries/peer-review  "Peer-reviews"
[10]:https://github.com/google/gson/commit/e8cdabf296cd4c2c1550ed76cf27a1a8e0c4ec59
[11]:https://www.overleaf.com/
[12]:https://junit.org/junit5/
[14]:https://github.com/RiccardoCerberi/IS24-AM34/tree/main/deliveries/view%20documentation
