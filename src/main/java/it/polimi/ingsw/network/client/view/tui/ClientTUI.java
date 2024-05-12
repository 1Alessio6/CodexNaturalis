package it.polimi.ingsw.network.client.view.tui;

import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.view.View;

import java.util.Scanner;

public class ClientTUI extends View {
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

    @Override
    public void run(VirtualView client) {
        /*
        while(true) {
            try {
                String username = receiveUsername();
                controller.connect(client, username);
                break;
            }catch(InvalidUsernameException e){
                System.err.println("Username selected is already taken. Please try again");
            } catch (RemoteException e) {
                System.err.println("Connection Error");
            } catch (FullLobbyException e){
                System.err.println("Error");
            }
        }
         */

    }

    private String receiveUsername(){
        System.out.println("Insert username");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.next();
        scanner.close();

        return username;
    }

    @Override
    public void showUpdatePlayersInLobby() {

    }

    @Override
    public void showUpdateCreator() {

    }

    @Override
    public void showUpdateAfterLobbyCrash() {

    }

    @Override
    public void showUpdateAfterConnection() {

    }

    @Override
    public void showUpdatePlayerStatus() {

    }

    @Override
    public void showInitialPlayerStatus() {

    }

    @Override
    public void showBoardSetUp() {

    }

    @Override
    public void showStarterPlacement() {

    }

    @Override
    public void showUpdateColor() {

    }

    @Override
    public void showUpdateObjectiveCard() {

    }

    @Override
    public void showUpdateAfterPlace() {

    }

    @Override
    public void showUpdateAfterDraw() {

    }

    @Override
    public void showUpdateChat() {

    }

    @Override
    public void showUpdateCurrentPlayer() {

    }

    @Override
    public void showUpdateSuspendedGame() {

    }

    @Override
    public void showWinners() {

    }
}
