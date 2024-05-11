package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.controller.ClientActions;

import java.io.Reader;
import java.util.Scanner;

public class ClientCli {
    private Scanner console;

    private void parseCommands() {
        // TODO: command condition should be changed
        while (console.hasNextLine()) {
            // split command with spaces and analyze the first word
            String[] nextCommand = console.nextLine().split(" ");

            switch (nextCommand[0]) {
                case "help" -> ClientUtil.gameActionsHelper();
                case "exit" -> {}
                case "place" -> {
                    //receive position
                }
                case null -> {}
                default -> {
                    System.out.println("Game action invalid");
                    ClientUtil.gameActionsHelper();
                }
            }
        }

    }

    /**
     * This method is invoked in a new thread at the beginning of a game
     * Commands can't be interrupted
     */
    private void beginCommandAcquisition() {
        new Thread(() -> {
            console = new Scanner(System.in);
            parseCommands();
        }).start();
    }
}
