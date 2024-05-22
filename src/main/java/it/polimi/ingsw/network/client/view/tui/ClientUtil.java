package it.polimi.ingsw.network.client.view.tui;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;

import java.util.*;

import static it.polimi.ingsw.model.card.Symbol.*;
import static it.polimi.ingsw.network.client.view.tui.ANSIColor.*;

/**
 * This enum represents the space and position of each area in the screen
 */
enum GameScreenArea {
    PLAYGROUND(96, 40, new Position(2, 30)),
    FACE_UP_CARDS(24, 14, new Position(2, 146)),
    HAND_CARDS(2*ClientUtil.areaPadding + 3*ClientUtil.cardWidth, ClientUtil.cardHeight, new Position(44 ,62)),
    DECKS(24, 5, new Position(18, 146)),
    CHAT(64, 27, new Position(23, 126)),
    SCOREBOARD(10, 26, new Position(2,2)),
    PRIVATE_OBJECTIVE(ClientUtil.cardWidth, ClientUtil.cardHeight, new Position(7, 27)),
    COMMON_OBJECTIVE(2 + 2*ClientUtil.cardWidth, ClientUtil.cardHeight, new Position(2, 44)),
    RESOURCES(26, 15, new Position(14, 2));


    int width;
    int height;
    Position screenPosition;

    GameScreenArea(int width, int height, Position screenPosition) {
        this.width = width;
        this.height = height;
        this.screenPosition = screenPosition;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Position getScreenPosition() {
        return screenPosition;
    }
}

public class ClientUtil {
    final static int cardWidth = 9;
    final static int cardHeight = 3;
    final static int areaPadding = 2;
    final static int maxUsernameLength=15;
    final static int maxPointsLength=8;

    static String plant = "\uD83C\uDF43";
    static String butterfly = "\uD83E\uDD8B";
    static String mushroom = "\uD83C\uDF44";
    static String animal = "\uD83D\uDC3A";
    static String inkwell = "\uD83E\uDED9";
    static String manuscript = "\uD83D\uDCDC";
    static String quill = "\uD83E\uDEB6";
    static String bigSpace = "  ";
    static String thinSpace = "\u2009";
    static String doubleThinSpace = "\u2009\u2009";
    static String doubleHairSpace = "\u200A" + "\u200A";
    static String hairSpace = "\u200A";
    static String corner = "\uD83D\uDDC2️";
    static String one = "\uD835\uDFCF";
    static String two = "\uD835\uDFD0";
    static String three = "\uD835\uDFD1";
    static String four = "\uD835\uDFD2";
    static String five = "\uD835\uDFD3";
    static String six = "\uD835\uDFD4";

    static String title = """
             ▄▄·       ·▄▄▄▄  ▄▄▄ .▐▄• ▄      ▐ ▄  ▄▄▄· ▄▄▄▄▄▄• ▄▌▄▄▄   ▄▄▄· ▄▄▌  ▪  .▄▄ ·    \s
            ▐█ ▌▪▪     ██▪ ██ ▀▄.▀· █▌█▌▪    •█▌▐█▐█ ▀█ •██  █▪██▌▀▄ █·▐█ ▀█ ██•  ██ ▐█ ▀.    \s
            ██ ▄▄ ▄█▀▄ ▐█· ▐█▌▐▀▀▪▄ ·██·     ▐█▐▐▌▄█▀▀█  ▐█.▪█▌▐█▌▐▀▀▄ ▄█▀▀█ ██▪  ▐█·▄▀▀▀█▄   \s
            ▐███▌▐█▌.▐▌██. ██ ▐█▄▄▌▪▐█·█▌    ██▐█▌▐█ ▪▐▌ ▐█▌·▐█▄█▌▐█•█▌▐█ ▪▐▌▐█▌▐▌▐█▌▐█▄▪▐█   \s
            ·▀▀▀  ▀█▄▀▪▀▀▀▀▀•  ▀▀▀ •▀▀ ▀▀    ▀▀ █▪ ▀  ▀  ▀▀▀  ▀▀▀ .▀  ▀ ▀  ▀ .▀▀▀ ▀▀▀ ▀▀▀▀ \s""";

    public static void printHelpCommands(Set<TUIActions> consentedCommands) {
        for (TUIActions command : consentedCommands) {
            System.out.println(command.toString() + ": " + command.getDescription());
        }
    }

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

    // Main methods

    public static void printCard(ClientCard card) {
        ANSIColor color = cardColorConversion(card.getFront().getColor());
        String[][] cardMatrix = new String[3][7];
        initializeMatrix(cardMatrix);
        Map<CornerPosition, Corner> cornerPositionCornerMap;
        Map<Symbol, Integer> resources;
        Condition condition;
        int points;
        if (!card.getBack().getBackCenterResources().isEmpty()) {//is back
            cornerPositionCornerMap = card.getBack().getCorners();
            points = card.getBack().getScore();
            resources = card.getBack().getBackCenterResources();
            condition = card.getBack().getPointsCondition(); //it will return an empty map

        } else {
            cornerPositionCornerMap = card.getFront().getCorners();
            points = card.getFront().getScore();
            resources = card.getFront().getBackCenterResources();
            condition = card.getFront().getPointsCondition();

        }
        appendNewResources(cornerPositionCornerMap, cardMatrix, color);
        appendMatrixLines(resources, condition, cardMatrix, color);
        if (resources.isEmpty()) {
            appendPoints(cardMatrix, condition, points);
        } else {
            appendInternalResources(resources, cardMatrix);
        }
        printCardMatrix(cardMatrix);
    }

    public static void printCard(ClientFace card) {
        ANSIColor color = cardColorConversion(card.getColor());
        String[][] cardMatrix = new String[3][7];
        initializeMatrix(cardMatrix);
        Map<CornerPosition, Corner> cornerPositionCornerMap = card.getCorners();
        Condition pointsCondition = card.getPointsCondition();
        Map<Symbol, Integer> resources = card.getBackCenterResources();
        int points = card.getScore();

        appendNewResources(cornerPositionCornerMap, cardMatrix, color);
        appendMatrixLines(resources, pointsCondition, cardMatrix, color);
        if (resources.isEmpty()) {
            appendPoints(cardMatrix, pointsCondition, points);
        } else {
            appendInternalResources(resources, cardMatrix);
        }
        printCardMatrix(cardMatrix);
    }

    public static void printOptionalCard() {
        String[][] cardMatrix = new String[3][7];
        initializeMatrix(cardMatrix);

        appendNewResources(new HashMap<>(), cardMatrix, BLUE);
        appendMatrixLines(new HashMap<>(), null, cardMatrix, BLUE);
        printCardMatrix(cardMatrix);
    }

    public static void printToLineColumn(int numberOfLine, int numberOfColumn, String str) {
        String[] lines = str.split("\n");
        int ongoingLine = numberOfLine;
        for (String line : lines) {
            System.out.println("\033[" + ongoingLine + ";" + numberOfColumn + "H" + line);
            ongoingLine++;
        }
    }

    public static void printObjectiveCard(ClientObjectiveCard objectiveCard) {
        String[][] cardMatrix = new String[3][7];
        initializeMatrix(cardMatrix);
        ANSIColor color = YELLOW;
        Map<Position, CardColor> positionCondition = objectiveCard.getPositionCondition();
        Map<Symbol, Integer> resourceCondition = objectiveCard.getResourceCondition();

        int switchCase = positionOrResourcesSwitchCase(positionCondition, resourceCondition);

        if (switchCase == 1) {//it is a card with a position condition

            appendNewResources(new HashMap<>(), cardMatrix, color);
            appendMatrixLines(new HashMap<>(), null, cardMatrix, color);
            appendPoints(cardMatrix, null, positionCase(positionCondition));
        } else if (switchCase == 2) {
            appendNewResources(new HashMap<>(), cardMatrix, color);
            appendMatrixLines(resourceCondition, null, cardMatrix, color);
            appendInternalResources(resourceCondition, cardMatrix);
        }
        printCardMatrix(cardMatrix);
    }

    private static void printInformation(ClientCard card) {
        if (card.getBack().getBackCenterResources().isEmpty()) {
            Condition pointsCondition = card.getFront().getPointsCondition();
            Map<Symbol, Integer> requirements = card.getFront().getRequirements();
            Map<Symbol, Integer> resources = card.getFront().getBackCenterResources();

            searchForRelevantInformation(pointsCondition, requirements, resources);
        } else {
            Condition pointsCondition = card.getFront().getPointsCondition();
            Map<Symbol, Integer> requirements = card.getBack().getRequirements();
            Map<Symbol, Integer> resources = card.getFront().getBackCenterResources();
            searchForRelevantInformation(pointsCondition, requirements, resources);
        }
    }

    // Secondary methods

    /**
     * Pads along x-axis a string, starting printing with same size pad at beginning and end
     */
    public static void calculateSpaces(int width, int height, String string) {
        // calculate height too
        //int string_height = 1; //by default it is one line
        //for (int i = 0; i < string.length() - 1; ++i) {
        //    if (string.substring(i, i + 1).equals("\s")) {
        //        string_height++;
        //    }
        //}

        int x_available = width - string.length();

        // printToLineColumn(x_available / 2, y_available / 2, string);
    }

    public static String centeredString(int availableSpaces, String string) {
        double numberOfSpaces = ((availableSpaces - string.length()) / 2.0);
        int indexOfDecimal = String.valueOf(numberOfSpaces).indexOf(".");
        String decimal = String.valueOf(0).concat(String.valueOf(numberOfSpaces).substring(indexOfDecimal));
        StringBuilder a = new StringBuilder();
        StringBuilder b=new StringBuilder();
        a.append(" ".repeat((int) Math.max(0, numberOfSpaces)));
        if (decimal.equals(String.valueOf(.5))) {
            a.append(doubleHairSpace);
        }
        b.append(a).append(string).append(a);

        return b.toString();
    }

    /**
     * Method to print in the middle of a line
     */
    public static String calculateSpaces(int width, String string) {
        calculateSpaces(width, 1, string);
        return null;
    }

    public static void printInLineColumn(int numberOfLine, int numberOfColumn, String[][] matrix) {
        int lines = matrix.length;
        int columns = matrix[0].length;
        int ongoingColumn = numberOfColumn;
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print("\033[" + numberOfLine + ";" + numberOfColumn + "H" + matrix[i][j]);
                numberOfColumn += matrix[i][j].length();
            }
            numberOfLine++;
            numberOfColumn = ongoingColumn;
            System.out.println();
        }
    }


    public static String appendLine(int numberOfLines) {
        return "═".repeat(Math.max(0, numberOfLines));
    }

    private static int resourcesSize(Map<Symbol, Integer> resources) {
        int i = 0;
        for (Map.Entry<Symbol, Integer> entry : resources.entrySet()) {
            i += entry.getValue();
        }
        return i;
    }

    private static ANSIColor cardColorConversion(CardColor color) {
        return switch (color) {
            case RED -> ANSIColor.RED;
            case BLUE -> ANSIColor.BLUE;
            case GREEN -> ANSIColor.GREEN;
            case YELLOW -> ANSIColor.YELLOW;
            case PURPLE -> ANSIColor.PURPLE;
            case null -> RESET;
        };
    }

    public static ANSIColor playerColorConversion(PlayerColor color) {
        return switch (color) {
            case RED -> ANSIColor.RED_BACKGROUND_BRIGHT;
            case BLUE -> ANSIColor.BLUE_BACKGROUND_BRIGHT;
            case GREEN -> ANSIColor.GREEN_BACKGROUND_BRIGHT;
            case YELLOW -> ANSIColor.YELLOW_BACKGROUND_BRIGHT;
            case BLACK -> ANSIColor.BLACK_BOLD_BRIGHT;
            case null -> ANSIColor.BLACK_BOLD_BRIGHT;
        };
    }

    private static void searchForRelevantInformation(Condition pointsCondition, Map<Symbol, Integer> requirements, Map<Symbol, Integer> resources) {
        if (pointsCondition != null) {//this can be improved
            System.out.println(ITALIC.getColor() + "Cond.: " + pointsCondition.name() + printCondition(pointsCondition) + RESET.getColor());
        }
        if (!requirements.isEmpty()) {
            for (Map.Entry<Symbol, Integer> entry : requirements.entrySet()) {
                System.out.println(ITALIC.getColor() + "Req.: " + entry.getKey().toString() + printResources(entry.getKey()) + " " + entry.getValue());
            }
        }
        if (!resources.isEmpty()) {
            for (Map.Entry<Symbol, Integer> entry : resources.entrySet()) {
                System.out.println(ITALIC.getColor() + "Res.: " + entry.getKey().toString() + printResources(entry.getKey()) + " " + entry.getValue());
            }
        }
    }

    private static void appendMatrixLines(Map<Symbol, Integer> resources, Condition condition, String[][] cardMatrix, ANSIColor color) {
        for (int i = 0; i < 3; i++) {
            if (i != 1) {
                for (int j = 1; j < 6; j++) {
                    cardMatrix[i][j] = color.getColor() + "═";
                }
            }
        }
        cardMatrix[1][0] = color.getColor() + "║";
        if (resourcesSize(resources) == 2) {
            cardMatrix[1][5] = color.getColor() + doubleHairSpace + "║";
        } else if (resourcesSize(resources) == 3) {
            cardMatrix[1][4] = color.getColor() + hairSpace + "║";
        } else if (resourcesSize(resources) == 1 || condition != null) {
            cardMatrix[1][6] = color.getColor() + doubleThinSpace + "║";
        } else {
            cardMatrix[1][6] = color.getColor() + "  ║";
        }
    }

    private static void appendPoints(String[][] cardMatrix, Condition pointsCondition, int points) {
        if (pointsCondition == null) {
            cardMatrix[1][4] = YELLOW.getColor() + printPoints(points) + RESET.getColor();//
        } else {
            cardMatrix[1][3] = YELLOW.getColor() + printPoints(points) + RESET.getColor();
            cardMatrix[1][4] = YELLOW.getColor() + "|" + RESET.getColor();
            cardMatrix[1][5] = printCondition(pointsCondition);
        }
    }

    private static void appendInternalResources(Map<Symbol, Integer> resources, String[][] cardMatrix) {//works only in starter and back cards
        int i = 0;
        for (Map.Entry<Symbol, Integer> entry : resources.entrySet()) {
            for (int j = 0; j < entry.getValue(); j++) {
                if (i == 0) {
                    if (resourcesSize(resources) == 1) {
                        cardMatrix[1][4] = printResources(entry.getKey());
                    } else if (resourcesSize(resources) == 2) {
                        cardMatrix[1][2] = printResources(entry.getKey());//5//2,3,4
                    } else if (resourcesSize(resources) == 3) {
                        cardMatrix[1][1] = printResources(entry.getKey());
                    }
                } else if (i == 1) {
                    if (resourcesSize(resources) == 2) {
                        cardMatrix[1][4] = printResources(entry.getKey());
                    } else {
                        cardMatrix[1][2] = printResources(entry.getKey());
                    }
                } else if (i == 2) {
                    cardMatrix[1][3] = printResources(entry.getKey());
                }
                i++;
            }
        }
    }

    public static void appendNewResources(Map<CornerPosition, Corner> cornerPositionCornerMap, String[][] card, ANSIColor color) {
        ArrayList<CornerPosition> cornerPositions = new ArrayList<>();

        for (Map.Entry<CornerPosition, Corner> entry : cornerPositionCornerMap.entrySet()) {
            cornerPositions.add(entry.getKey());

            switch (entry.getKey()) {
                case TOP_LEFT -> {
                    if (entry.getValue().isCovered()) {
                        card[0][0] = YELLOW.getColor() + " ╝";
                    } else if (entry.getValue() != null) {
                        card[0][0] = printResources(entry.getValue().getSymbol());
                    }
                }
                case TOP_RIGHT -> {
                    if (entry.getValue().isCovered()) {
                        card[0][6] = YELLOW.getColor() + "╚ ";
                    } else if (entry.getValue() != null) {
                        card[0][6] = thinSpace + printResources(entry.getValue().getSymbol());
                    }
                }
                case LOWER_LEFT -> {
                    if (entry.getValue().isCovered()) {
                        card[2][0] = YELLOW.getColor() + " ╗";
                    } else if (entry.getValue() != null) {
                        card[2][0] = printResources(entry.getValue().getSymbol());
                    }
                }
                case LOWER_RIGHT -> {
                    if (entry.getValue().isCovered()) {
                        card[2][6] = YELLOW.getColor() + "╔ ";
                    } else if (entry.getValue() != null) {
                        card[2][6] = thinSpace + printResources(entry.getValue().getSymbol());
                    }
                }
            }
        }
        if (!cornerPositions.contains(CornerPosition.TOP_LEFT)) {
            card[0][0] = color.getColor() + "╔ ";
        }
        if (!cornerPositions.contains(CornerPosition.TOP_RIGHT)) {
            card[0][6] = color.getColor() + " ╗";
        }
        if (!cornerPositions.contains(CornerPosition.LOWER_RIGHT)) {
            card[2][6] = color.getColor() + " ╝";
        }
        if (!cornerPositions.contains(CornerPosition.LOWER_LEFT)) {
            card[2][0] = color.getColor() + "╚ ";
        }

    }

    private static void initializeMatrix(String[][] cardMatrix) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                cardMatrix[i][j] = " ";
            }
        }
    }

    private static void printCardMatrix(String[][] cardMatrix) {
        int lines = cardMatrix[0].length;

        for (String[] strings : cardMatrix) {
            for (int j = 0; j < lines; j++) {
                System.out.print(strings[j]);
            }
            System.out.println();
        }
    }

    private static int positionCase(Map<Position, CardColor> positionCondition) {
        ArrayList<Position> caseOne = new ArrayList<>();
        ArrayList<Position> caseTwo = new ArrayList<>();
        ArrayList<Position> caseThree = new ArrayList<>();
        ArrayList<Position> caseFour = new ArrayList<>();
        ArrayList<Position> caseFive = new ArrayList<>();
        ArrayList<Position> caseSix = new ArrayList<>();

        Collections.addAll(caseOne, new Position(0, 0), new Position(1, 1), new Position(2, 2));
        Collections.addAll(caseTwo, new Position(0, 0), new Position(0, -2), new Position(1, -3));
        Collections.addAll(caseThree, new Position(0, 0), new Position(1, -1), new Position(2, -2));
        Collections.addAll(caseFour, new Position(0, 0), new Position(1, -1), new Position(1, -3));
        Collections.addAll(caseFive, new Position(0, 0), new Position(0, 2), new Position(1, 3));
        Collections.addAll(caseSix, new Position(0, 0), new Position(1, 1), new Position(1, 3));

        ArrayList<Position> positions = new ArrayList<>();
        for (Map.Entry<Position, CardColor> entry : positionCondition.entrySet()) {
            positions.add(entry.getKey());
        }

        if (positions.equals(caseOne)) {
            return 1;
        } else if (positions.equals(caseTwo)) {
            return 2;
        } else if (positions.equals(caseThree)) {
            return 3;
        } else if (positions.equals(caseFour)) {
            return 4;
        } else if (positions.equals(caseFive)) {
            return 5;
        } else if (positions.equals(caseSix)) {
            return 6;
        } else {
            return 0;
        }
    }

    private static String printCondition(Condition condition) {
        if (condition == Condition.NUM_QUILL) {
            return quill;
        } else if (condition == Condition.NUM_MANUSCRIPT) {
            return manuscript;
        } else if (condition == Condition.NUM_INKWELL) {
            return inkwell;
        } else if (condition == Condition.CORNERS) {
            return corner;
        } else {
            return bigSpace;
        }
    }

    private static String printResources(Symbol symbol) {
        if (symbol == PLANT) {
            return plant;
        }
        if (symbol == FUNGI) {
            return mushroom;
        }
        if (symbol == ANIMAL) {
            return animal;
        }
        if (symbol == INSECT) {
            return butterfly;
        }
        if (symbol == QUILL) {
            return quill;
        }
        if (symbol == INKWELL) {
            return inkwell;
        }
        if (symbol == MANUSCRIPT) {
            return manuscript;
        } else {
            return bigSpace;
        }
    }

    private static String printPoints(int points) {
        if (points == 0) {
            return " ";
        } else if (points == 1) {
            return one;
        } else if (points == 2) {
            return two;
        } else if (points == 3) {
            return three;
        } else if (points == 4) {
            return four;
        } else if (points == 5) {
            return five;
        } else if (points == 6) {
            return six;
        } else {
            return corner;
        }
    }

    private static int positionOrResourcesSwitchCase(Map<Position, CardColor> positionCondition, Map<Symbol, Integer> resourcesCondition) {
        if (!positionCondition.isEmpty() && resourcesCondition.isEmpty()) {
            return 1;
        } else if (positionCondition.isEmpty() && !resourcesCondition.isEmpty()) {
            return 2;
        } else {
            return 0;
        }
    }

    public static StringBuilder createScoreBoard(List<ClientPlayer> players) {
        StringBuilder str = new StringBuilder();


        str.append("╔").append(ClientUtil.appendLine(maxUsernameLength)).append("╦").append(ClientUtil.appendLine(maxPointsLength)).append("╗\n");
        str.append("║").append(centeredString(maxUsernameLength,"Players")).append("║");
        str.append(centeredString(maxPointsLength,"Points")).append("║\n");
        str.append("╠").append(ClientUtil.appendLine(maxUsernameLength)).append("╬").append(ClientUtil.appendLine(maxPointsLength)).append("╣\n");

        for (ClientPlayer i :players) {
            String username = i.getUsername();
            ANSIColor color = ClientUtil.playerColorConversion(i.getColor());
            int points = i.getPlayground().getPoints();

            str.append("║").append(color.getColor()).append(centeredString(maxUsernameLength,username)).append(RESET.getColor()).append("║");
            str.append(centeredString(maxPointsLength,Integer.toString(points))).append("║\n");
        }
        str.append("╚").append(ClientUtil.appendLine(maxUsernameLength)).append("╩").append(ClientUtil.appendLine(maxPointsLength)).append("╝\n");
        return str;
    }

    public static void main(String[] args) {
        printToLineColumn(25, 95, title);
    }
}
