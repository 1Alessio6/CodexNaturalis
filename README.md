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

  
</p>

## Table of Contents

1. [Project specifications](#project-specifications)
   * [Implemented features](#implemented-features)
2. [How to use](#how-to-use)
   * [Run the server](#run-the-server)
   * [Run the client](#run-the-client)
3. [Documentation](#documentation)

## Project specifications

The project is a software implementation of the game 'Codex Naturalis' designed by Cranio Creations, for more information about the game visit [Bombyx][bombyx].
The implementation supports **all the rules** established by the game and allow the user to play with both the Graphical (**GUI**) and Terminal (**TUI**) interface, using the network protocols **RMI** or **Socket**.
Additionally, the following **advanced features** have been developed.

### Implemented advanced features

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

* [final UML diagram][final-uml].
* [communication protocol][communication-protocol].
* [peer-reviews][peer-reviews].
* [javadoc][javadoc]
* [user Interface documentation][user-interface-doc]

 ## Credits
 * [Gson][gson]
 * [Overleaf][overleaf]
 * [JUnit][junit]

[bombyx]:https://studiobombyx.com/en/jeu/codex-naturalis-2/ 
[final-uml]:https://github.com/RiccardoCerberi/IS24-AM34/tree/main/deliveries/final_uml
[communication-protocol]:https://github.com/RiccardoCerberi/IS24-AM34/blob/main/deliveries/communication%20protocol/final%20communication%20protocol.pdf
[peer-reviews]:https://github.com/RiccardoCerberi/IS24-AM34/tree/main/deliveries/peer-review
[javadoc]:https://github.com/RiccardoCerberi/IS24-AM34/tree/main/javadoc
[gson]:https://github.com/google/gson/
[overleaf]:https://www.overleaf.com/
[junit]:https://junit.org/junit5/
[user-interface-doc]:https://github.com/RiccardoCerberi/IS24-AM34/tree/main/deliveries/view%20documentation
