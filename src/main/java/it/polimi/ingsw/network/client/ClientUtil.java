package it.polimi.ingsw.network.client;

public class ClientUtil {
    protected static void argsHelper(String error) {
        System.out.println(error);

        System.out.println("Codex Naturalis: codexnaturalis [ARGS]");
        System.out.println("Possible args:");
        System.out.println("--port [PORT]          override the default port");
        System.out.println("--rmi                  enable rmi (default is with socket)");
        System.out.println("--gui                 enable gui (default is cli)");
    }

    protected static void gameActionsHelper() {
        System.out.println("Possible moves:");
    }
}
