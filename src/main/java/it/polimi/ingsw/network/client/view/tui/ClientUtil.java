package it.polimi.ingsw.network.client.view.tui;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.drawplayground.DrawablePlayground;
import it.polimi.ingsw.network.client.view.drawplayground.InvalidCardDimensionException;
import it.polimi.ingsw.network.client.view.drawplayground.InvalidCardRepresentationException;
import it.polimi.ingsw.network.client.view.drawplayground.UnInitializedPlaygroundException;

import java.io.*;
import java.util.*;

import static it.polimi.ingsw.model.card.Symbol.*;
import static it.polimi.ingsw.network.client.view.tui.ANSIColor.*;

/**
 * This enum represents the space and position of each area in the screen
 */
enum GameScreenArea {
    PLAYGROUND(96, 40, new Position(30, 2)),
    FACE_UP_CARDS(24, 14, new Position(146, 2)),
    HAND_CARDS(2*ClientUtil.areaPadding + 3*ClientUtil.cardWidth, ClientUtil.cardHeight, new Position(62 ,44)),
    DECKS(24, 5, new Position(18, 149)),
    CHAT(62, 11, new Position(23, 126)),
    INPUT_AREA(62,11,new Position(36,126)),
    TITLE(80,5,new Position(2,55)),
    SCOREBOARD(10, 26, new Position(2, 2)),
    PRIVATE_OBJECTIVE(ClientUtil.cardWidth, ClientUtil.cardHeight, new Position(37, 7)),
    COMMON_OBJECTIVE(2 + 2 * ClientUtil.cardWidth, ClientUtil.cardHeight, new Position(44, 2)),
    RESOURCES(26, 15, new Position(14, 2));

    final int width;
    final int height;
    final Position screenPosition;

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
    final static int resourceBoardColumnSpace = 5;
    final static int requirementsHeight = 2;

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

    DrawablePlayground currentPlayground;

    {
        try {
            currentPlayground = new DrawablePlayground(cardWidth, cardHeight);
        } catch (InvalidCardDimensionException e) {
            throw new RuntimeException(e);
        }
    }

    static String title = """
             ▄▄·       ·▄▄▄▄  ▄▄▄ .▐▄• ▄      ▐ ▄  ▄▄▄· ▄▄▄▄▄▄• ▄▌▄▄▄   ▄▄▄· ▄▄▌  ▪  .▄▄ ·    \s
            ▐█ ▌▪▪     ██▪ ██ ▀▄.▀· █▌█▌▪    •█▌▐█▐█ ▀█ •██  █▪██▌▀▄ █·▐█ ▀█ ██•  ██ ▐█ ▀.    \s
            ██ ▄▄ ▄█▀▄ ▐█· ▐█▌▐▀▀▪▄ ·██·     ▐█▐▐▌▄█▀▀█  ▐█.▪█▌▐█▌▐▀▀▄ ▄█▀▀█ ██▪  ▐█·▄▀▀▀█▄   \s
            ▐███▌▐█▌.▐▌██. ██ ▐█▄▄▌▪▐█·█▌    ██▐█▌▐█ ▪▐▌ ▐█▌·▐█▄█▌▐█•█▌▐█ ▪▐▌▐█▌▐▌▐█▌▐█▄▪▐█   \s
            ·▀▀▀  ▀█▄▀▪▀▀▀▀▀•  ▀▀▀ •▀▀ ▀▀    ▀▀ █▪ ▀  ▀  ▀▀▀  ▀▀▀ .▀  ▀ ▀  ▀ .▀▀▀ ▀▀▀ ▀▀▀▀ \s""";

    /**
     * Prints the consented commands after the help command call.
     *
     * @param consentedCommands the commands that the player can call in a specific phase.
     */
    public static void printHelpCommands(Set<TUIActions> consentedCommands) {
        int line=GameScreenArea.INPUT_AREA.getScreenPosition().getX()+2;
        int column= GameScreenArea.INPUT_AREA.getScreenPosition().getY()+1;
        printCommandSquare();

        for (TUIActions command : consentedCommands) {
            printToLineColumn(line,
                    column,command.toString()+": "+command.getDescription()+"\n");
            line++;
        }
    }

    /**
     * Prints the <code>string</code> inside the command square.
     *
     * @param string the command to be printed.
     */
    public static void printCommand(String string){
        printCommandSquare();
        writeLine(GameScreenArea.INPUT_AREA.getScreenPosition().getX()+2,
                GameScreenArea.INPUT_AREA.getScreenPosition().getY()+1,
                GameScreenArea.INPUT_AREA.getWidth()-2,string);
    }

    /**
     * Prints the 'square' in which commands are displayed.
     */
    public static void printCommandSquare(){
        printToLineColumn(GameScreenArea.INPUT_AREA.getScreenPosition().getX(),
                GameScreenArea.INPUT_AREA.getScreenPosition().getY(),
                designSquare(GameScreenArea.INPUT_AREA.getWidth(),
                        GameScreenArea.INPUT_AREA.getHeight() - 2).toString());
    }

    /**
     * Prints the 'square' in which the chat is displayed.
     */
    public static void printChatSquare(){
        ClientUtil.printToLineColumn(GameScreenArea.CHAT.getScreenPosition().getX(),
                GameScreenArea.CHAT.getScreenPosition().getY(),
                ClientUtil.designSquare(GameScreenArea.CHAT.getWidth(),
                        GameScreenArea.CHAT.getHeight() - 2).toString());
    }

    /**
     * Displays the available args after an args input error.
     *
     * @param error to be displayed.
     */
    public static void argsHelper(String error) {
        System.out.println(error);

        System.out.println("Codex Naturalis: codexnaturalis [ARGS]");
        System.out.println("Possible args:");
        System.out.println("--port [PORT]          override the default port");
        System.out.println("--rmi                  enable rmi (default is with socket)");
        System.out.println("--gui                 enable gui (default is cli)");
    }

    // Main methods

    /**
     * Designs a card.
     *
     * @param face of the card.
     * @return the card seen as a matrix of strings.
     */
    public static String[][] designCard(ClientFace face) {
        ANSIColor color = cardColorConversion(face.getColor());
        String[][] cardMatrix = new String[3][7];
        initializeMatrix(cardMatrix);
        Map<CornerPosition, Corner> cornerPositionCornerMap = face.getCorners();
        Condition pointsCondition = face.getPointsCondition();
        Map<Symbol, Integer> resources = face.getBackCenterResources();
        int points = face.getScore();

        appendNewResources(cornerPositionCornerMap, cardMatrix, color);
        appendMatrixLines(resources, pointsCondition, cardMatrix, color);
        if (resources.isEmpty()) {
            appendPoints(cardMatrix, pointsCondition, points);
        } else {
            appendInternalResources(resources, cardMatrix);
        }
        return cardMatrix;
    }

    public static void printOptionalCard() {
        String[][] cardMatrix = new String[3][7];
        initializeMatrix(cardMatrix);

        appendNewResources(new HashMap<>(), cardMatrix, BLUE);
        appendMatrixLines(new HashMap<>(), null, cardMatrix, BLUE);
        printCardMatrix(cardMatrix);
    }

    /**
     * Prints a <code>str</code> <B>string</B> in a specific <code>lineCoordinate</code> and <code>columnCoordinate</code>.
     *
     * @param lineCoordinate   the line in which the <code>str</code> is to be printed.
     * @param columnCoordinate the column in which the <code>str</code> is to be printed.
     * @param str              the string to print.
     */
    // this takes a string as input
    public static void printToLineColumn(int lineCoordinate, int columnCoordinate, String str) {
        String[] lines = str.split("\n");
        printToLineColumn(lineCoordinate, columnCoordinate, lines);
    }

    /**
     * Prints a <code>str</code> <B>array of strings</B> in a specific <code>lineCoordinate</code> and
     * <code>columnCoordinate</code>.
     *
     * @param lineCoordinate   the line in which the <code>str</code> is to be printed.
     * @param columnCoordinate the column in which the <code>str</code> is to be printed.
     * @param str              the array of strings to print.
     */
    // this takes an array of string as input
    public static void printToLineColumn(int lineCoordinate, int columnCoordinate, String[] str) {
        int ongoingLine = lineCoordinate;
        for (String line : str) {
            System.out.println("\033[" + ongoingLine + ";" + columnCoordinate + "H" + line);
            ongoingLine++;
        }
    }

    /**
     * Prints a <code>matrix</code> in a specific <code>lineCoordinate</code> and <code>columnCoordinate</code>.
     * It's used to convert cards, generally string matrices.
     *
     * @param lineCoordinate   the line in which the <code>matrix</code> is to be printed.
     * @param columnCoordinate the column in which the <code>matrix</code> is to be printed
     * @param matrix           of strings.
     */
    // mostly used to convert cards, generally string matrices
    public static void printToLineColumn(int lineCoordinate, int columnCoordinate, String[][] matrix) {
        String[] lines = Arrays.stream(matrix).map(str -> String.join("", str)).toArray(String[]::new);

        printToLineColumn(lineCoordinate, columnCoordinate, lines);
    }

    /**
     * Designs an objective card.
     *
     * @param objectiveCard the objective card to design.
     * @return the objective card seen as a matrix of strings.
     */
    public static String[][] designObjectiveCard(ClientObjectiveCard objectiveCard) {
        String[][] cardMatrix = new String[3][7];
        initializeMatrix(cardMatrix);
        ANSIColor color = YELLOW;
        int points;
        Map<Position, CardColor> positionCondition = objectiveCard.getPositionCondition();
        Map<Symbol, Integer> resourceCondition = objectiveCard.getResourceCondition();

        int switchCase = positionOrResourcesSwitchCase(positionCondition, resourceCondition);

        if (switchCase == 1) {//it is a card with a position condition
            points=positionCase(positionCondition)==1 ||positionCase(positionCondition)==3 ? 2:3;
            appendNewResources(new HashMap<>(), cardMatrix, color);
            appendMatrixLines(new HashMap<>(), null, cardMatrix, color);
            paintBackground(cardMatrix,positionCondition);
            appendPoints(cardMatrix, null, points);
        } else if (switchCase == 2) {
            appendNewResources(new HashMap<>(), cardMatrix, color);
            appendMatrixLines(resourceCondition, null, cardMatrix, color);
            appendInternalResources(resourceCondition, cardMatrix);
        }
        return cardMatrix;
    }

    private static void printInformation(ClientCard card) {
        Map<Symbol, Integer> requirements;
        if (card.getBack().getBackCenterResources().isEmpty()) {
            requirements = card.getFront().getRequirements();
        } else {
            requirements = card.getBack().getRequirements();
        }
        searchForRelevantInformation(requirements);
    }

    // Secondary methods

    /**
     * Pads the <code>string</code> in the center of the <code>availableSpaces</code>.
     *
     * @param availableSpaces total available space
     * @param string          the string to be centered.
     * @return the centered word.
     */
    public static String centeredString(int availableSpaces, String string) {
        int numberOfSpaces = ((availableSpaces - string.length()) / 2);
        StringBuilder a = new StringBuilder();
        StringBuilder b = new StringBuilder();
        a.append(" ".repeat(Math.max(0, numberOfSpaces)));
        b.append(a).append(string).append(a);
        if ((availableSpaces - string.length()) % 2 != 0) {
            b.append(thinSpace);
        }
        return b.toString();
    }

    /**
     * Clears the area where exceptions are displayed.
     */
    public static void clearExceptionSpace(){
        writeLine(GameScreenArea.INPUT_AREA.getScreenPosition().getX()+11,
                GameScreenArea.INPUT_AREA.getScreenPosition().getY()+1,
                GameScreenArea.INPUT_AREA.getWidth() -2,
                " ".repeat(120));
    }

    /**
     * Designs a 'square' with the <code>width</code> and the <code>height</code> provided.
     *
     * @param width  the width of the 'square' to be designed.
     * @param height the height of the 'square' to be designed.
     * @return the 'square' with the given specifications.
     */
    public static StringBuilder designSquare(int width, int height) {
        StringBuilder str = new StringBuilder();

        str.append("╔").append(appendHorizontalLine(width)).append("╗\n");
        str.append(appendVerticalLines(height, width));
        str.append("╚").append(appendHorizontalLine(width)).append("╝\n");

        return str;
    }

    /**
     * Adds <code>numberOfLines</code> horizontal lines to the returned string.
     *
     * @param numberOfLines the number of horizontal lines to be added.
     * @return a string with <code>numberOfLines</code> horizontal lines.
     */
    public static String appendHorizontalLine(int numberOfLines) {
        return "═".repeat(Math.max(0, numberOfLines));
    }

    /**
     * Adds <code>numberOfLines</code> pairs of vertical lines separated by a space that is <code>width</code> wide to
     * the returned string.
     *
     * @param numberOfLines the number of vertical line pairs to be added.
     * @param width         the space between a single pair of lines.
     * @return a string with <code>numberOfLines</code> pairs of vertical lines separated by a <code>width</code> space.
     */
    private static String appendVerticalLines(int numberOfLines, int width) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < numberOfLines; i++) {
            str.append("║").repeat(" ", width).append("║\n");
        }
        return str.toString();
    }

    /**
     * Calculates the total number of resources inside <code>resources</code>.
     *
     * @param resources the map from which the number of resources is to be determined.
     * @return the total number of resources in <code>resources</code>.
     */
    private static int resourcesSize(Map<Symbol, Integer> resources) {
        int i = 0;
        for (Map.Entry<Symbol, Integer> entry : resources.entrySet()) {
            i += entry.getValue();
        }
        return i;
    }

    /**
     * Converts a <code>color</code> from CardColor to ANSIColor.
     *
     * @param color the color to convert.
     * @return the ANSIColor color corresponding to the CardColor <code>color</code>.
     */
    private static ANSIColor cardColorConversion(CardColor color) {
        return switch (color) {
            case RED -> RED;
            case BLUE -> BLUE;
            case GREEN -> GREEN;
            case YELLOW -> YELLOW;
            case PURPLE -> PURPLE;
            case null -> RESET;
        };
    }

    /**
     * Converts a <code>color</code> from PlayerColor to ANSIColor.
     *
     * @param color the color to convert.
     * @return the ANSIColor color corresponding to the PlayerColor <code>color</code>.
     */
    public static ANSIColor playerColorConversion(PlayerColor color) {
        return switch (color) {
            case RED -> RED_BACKGROUND_BRIGHT;
            case BLUE -> BLUE_BACKGROUND_BRIGHT;
            case GREEN -> GREEN_BACKGROUND_BRIGHT;
            case YELLOW -> YELLOW_BACKGROUND_BRIGHT;
            case BLACK -> BLACK_BOLD_BRIGHT;
            case null -> BLACK_BOLD_BRIGHT;
        };
    }

    /**
     * Converts a <code>color</code> from CardColor to ANSIColor, seen as a background color.
     *
     * @param color the color to convert.
     * @return the ANSIColor background color corresponding to the CardColor <code>color</code>.
     */
    public static ANSIColor cardColorToBGConversion(CardColor color) {
        return switch (color) {
            case RED -> RED_BACKGROUND_BRIGHT;
            case BLUE -> BLUE_BACKGROUND_BRIGHT;
            case GREEN -> GREEN_BACKGROUND_BRIGHT;
            case PURPLE -> PURPLE_BACKGROUND_HIGH_INTENSITY;
            case YELLOW -> null;
        };
    }

    private static void searchForRelevantInformation(Map<Symbol, Integer> requirements) {
        if (!requirements.isEmpty()) {
            for (Map.Entry<Symbol, Integer> entry : requirements.entrySet()) {
                System.out.println(ITALIC.getColor() + "Req.: " + entry.getKey().toString() + printResources(entry.getKey()) + " " + entry.getValue());
            }
        }
    }

    /**
     * Appends the outer edges to the <code>cardMatrix</code>. It adds the bottom, top and side edges.
     *
     * @param resources  a map containing the resources of the card and their quantities.
     * @param condition  the condition of the card.
     * @param cardMatrix the card seen as an array of strings.
     * @param color      the color of the card.
     */
    private static void appendMatrixLines(Map<Symbol, Integer> resources, Condition condition, String[][] cardMatrix, ANSIColor color) {
        for (int i = 0; i < 3; i++) {
            if (i != 1) {
                for (int j = 1; j < 6; j++) {
                    cardMatrix[i][j] = color.getColor() + "═" + RESET.getColor();
                }
            }
        }
        cardMatrix[1][0] = color.getColor() + "║" + RESET.getColor();
        if (resourcesSize(resources) == 2) {
            cardMatrix[1][4] = color.getColor() + doubleHairSpace + "║" + RESET.getColor();
        } else if (resourcesSize(resources) == 3) {
            cardMatrix[1][4] = color.getColor() + hairSpace + "║" + RESET.getColor();
        } else if (resourcesSize(resources) == 1 || condition != null) {
            cardMatrix[1][6] = color.getColor() + " ║" + RESET.getColor();
        } else {
            cardMatrix[1][6] = color.getColor() + "  ║" + RESET.getColor();
        }
    }

    /**
     * Appends possible <code>points</code> to the <code>cardMatrix</code>.
     *
     * @param cardMatrix      the card seen as an array of strings.
     * @param pointsCondition the condition to be fulfilled in order to earn the <code>points</code>.
     * @param points          card points.
     */
    private static void appendPoints(String[][] cardMatrix, Condition pointsCondition, int points) {
        if (pointsCondition == null) {
            cardMatrix[1][4] = YELLOW.getColor() + printPoints(points) + RESET.getColor();//
        } else {
            cardMatrix[1][3] = YELLOW.getColor() + printPoints(points) + RESET.getColor();
            cardMatrix[1][4] = YELLOW.getColor() + "|" + RESET.getColor();
            cardMatrix[1][5] = printCondition(pointsCondition);
        }
    }

    /**
     * Appends the <code>resources</code> inside the <code>cardMatrix</code>.
     *
     * @param resources  a map containing the resources of the card and their quantities.
     * @param cardMatrix the card seen as an array of strings.
     */
    private static void appendInternalResources(Map<Symbol, Integer> resources, String[][] cardMatrix) {//works only in starter and back cards
        int i = 0;
        for (Map.Entry<Symbol, Integer> entry : resources.entrySet()) {
            for (int j = 0; j < entry.getValue(); j++) {
                if (i == 0) {
                    if (resourcesSize(resources) == 1) {
                        cardMatrix[1][3] = printResources(entry.getKey());
                    } else if (resourcesSize(resources) == 2) {
                        cardMatrix[1][2] = printResources(entry.getKey());//5//2,3,4
                    } else if (resourcesSize(resources) == 3) {
                        cardMatrix[1][1] = printResources(entry.getKey());
                    }
                } else if (i == 1) {
                    if (resourcesSize(resources) == 2) {
                        cardMatrix[1][3] = printResources(entry.getKey());
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

    /**
     * Appends the vertices of the card to the <code>card</code>.
     *
     * @param cornerPositionCornerMap the map containing the vertices of the card.
     * @param card                    the card seen as an array of strings.
     * @param color                   the color of the card.
     */
    public static void appendNewResources(Map<CornerPosition, Corner> cornerPositionCornerMap, String[][] card, ANSIColor color) {
        ArrayList<CornerPosition> cornerPositions = new ArrayList<>();

        for (Map.Entry<CornerPosition, Corner> entry : cornerPositionCornerMap.entrySet()) {
            cornerPositions.add(entry.getKey());

            switch (entry.getKey()) {
                case TOP_LEFT -> {
                    if (entry.getValue().isCovered()) {
                        card[0][0] = YELLOW.getColor() + " ╝" + RESET.getColor();
                    } else if (entry.getValue() != null) {
                        card[0][0] = printResources(entry.getValue().getSymbol());
                    }
                }
                case TOP_RIGHT -> {
                    if (entry.getValue().isCovered()) {
                        card[0][6] = YELLOW.getColor() + "╚ " + RESET.getColor();
                    } else if (entry.getValue() != null) {
                        card[0][6] = printResources(entry.getValue().getSymbol());
                    }
                }
                case LOWER_LEFT -> {
                    if (entry.getValue().isCovered()) {
                        card[2][0] = YELLOW.getColor() + " ╗" + RESET.getColor();
                    } else if (entry.getValue() != null) {
                        card[2][0] = printResources(entry.getValue().getSymbol());
                    }
                }
                case LOWER_RIGHT -> {
                    if (entry.getValue().isCovered()) {
                        card[2][6] = YELLOW.getColor() + "╔ " + RESET.getColor();
                    } else if (entry.getValue() != null) {
                        card[2][6] = printResources(entry.getValue().getSymbol());
                    }
                }
            }
        }
        if (!cornerPositions.contains(CornerPosition.TOP_LEFT)) {
            card[0][0] = color.getColor() + "╔ " + RESET.getColor();
        }
        if (!cornerPositions.contains(CornerPosition.TOP_RIGHT)) {
            card[0][6] = color.getColor() + " ╗" + RESET.getColor();
        }
        if (!cornerPositions.contains(CornerPosition.LOWER_RIGHT)) {
            card[2][6] = color.getColor() + " ╝" + RESET.getColor();
        }
        if (!cornerPositions.contains(CornerPosition.LOWER_LEFT)) {
            card[2][0] = color.getColor() + "╚ " + RESET.getColor();
        }

    }

    /**
     * Initialize the inside of the matrix with empty spaces.
     *
     * @param cardMatrix the card seen as an array of strings.
     */
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

    /**
     * Determines the case in which the objective position card is.
     *
     * @param positionCondition a map containing the positions and the colors of the cards.
     * @return one, two, three, four, five or six, depending on the case the objective position card is in.
     */
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

    /**
     * Paints the pattern of the objective position card in the <code>card</code> depending on the case in which it is.
     *
     * @param card                 the card to be painted.
     * @param positionCardColorMap a map containing the positions and the colors of the cards.
     */
    private static void paintBackground(String[][] card, Map<Position, CardColor> positionCardColorMap) {
        ArrayList<CardColor> colors = new ArrayList<>();
        int posCase = positionCase(positionCardColorMap);
        for (Map.Entry<Position, CardColor> entry : positionCardColorMap.entrySet()) {
            colors.add(entry.getValue());
        }
        switch (posCase) {
            case 1:
                card[2][1] = cardColorToBGConversion(colors.get(0)).getColor() + " " + RESET.getColor();
                card[1][3] = cardColorToBGConversion(colors.get(1)).getColor() + " " + RESET.getColor();
                card[0][3] = cardColorToBGConversion(colors.get(2)).getColor() + " " + RESET.getColor();
                break;
            case 2:
                card[0][2]=cardColorToBGConversion(colors.get(0)).getColor()+" "+RESET.getColor();
                card[1][3]=cardColorToBGConversion(colors.get(1)).getColor()+" "+RESET.getColor();
                card[2][3]=cardColorToBGConversion(colors.get(2)).getColor()+" "+RESET.getColor();
                break;
            case 3:
                card[0][1]=cardColorToBGConversion(colors.get(0)).getColor()+" "+RESET.getColor();
                card[1][3]=cardColorToBGConversion(colors.get(1)).getColor()+" "+RESET.getColor();
                card[2][3]=cardColorToBGConversion(colors.get(2)).getColor()+" "+RESET.getColor();
                break;
            case 4:
                card[0][1]=cardColorToBGConversion(colors.get(0)).getColor()+" "+RESET.getColor();
                card[1][3]=cardColorToBGConversion(colors.get(1)).getColor()+" "+RESET.getColor();
                card[2][2]=cardColorToBGConversion(colors.get(2)).getColor()+" "+RESET.getColor();
                break;
            case 5:
                card[2][2]=cardColorToBGConversion(colors.get(0)).getColor()+" "+RESET.getColor();
                card[1][3]=cardColorToBGConversion(colors.get(1)).getColor()+" "+RESET.getColor();
                card[0][3]=cardColorToBGConversion(colors.get(2)).getColor()+" "+RESET.getColor();
                break;
            case 6:
                card[2][1]=cardColorToBGConversion(colors.get(0)).getColor()+" "+RESET.getColor();
                card[1][3]=cardColorToBGConversion(colors.get(1)).getColor()+" "+RESET.getColor();
                card[0][2]=cardColorToBGConversion(colors.get(2)).getColor()+" "+RESET.getColor();
                break;
        }
    }

    /**
     * Returns the corresponding symbol to the specified <code>condition</code>.
     *
     * @param condition the condition of the card.
     * @return the symbol of the <code>condition</code> seen as string.
     */
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

    /**
     * Returns the corresponding string symbol to the specified <code>symbol</code>.
     *
     * @param symbol the symbol to be converted.
     * @return the string symbol of the <code>symbol</code>.
     */
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

    /**
     * Returns the corresponding string number to the specified <code>points</code>.
     *
     * @param points the points to be converted.
     * @return the string number of the <code>points</code>.
     */
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

    /**
     * Determines the case in which the objective card is.
     *
     * @param positionCondition  a map containing the positions and the colors of the cards.
     * @param resourcesCondition a map containing the resources of the card and their quantities.
     * @return 1 if the objective card is an objective position card, 2 of the card is an objective resource card, 0
     * otherwise.
     */
    private static int positionOrResourcesSwitchCase(Map<Position, CardColor> positionCondition, Map<Symbol, Integer> resourcesCondition) {
        if (!positionCondition.isEmpty() && resourcesCondition.isEmpty()) {
            return 1;
        } else if (positionCondition.isEmpty() && !resourcesCondition.isEmpty()) {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * Creates the scoreboard.
     *
     * @param players the players.
     * @return the scoreboard with the usernames of the players,their points and their colors.
     */
    public static String createScoreBoard(List<ClientPlayer> players) {
        StringBuilder str = new StringBuilder();

        str.append("╔").append(ClientUtil.appendHorizontalLine(maxUsernameLength)).append("╦").append(ClientUtil.appendHorizontalLine(maxPointsLength)).append("╗\n");
        str.append("║").append(centeredString(maxUsernameLength, "Players")).append("║");
        str.append(centeredString(maxPointsLength, "Points")).append("║\n");
        str.append("╠").append(ClientUtil.appendHorizontalLine(maxUsernameLength)).append("╬").append(ClientUtil.appendHorizontalLine(maxPointsLength)).append("╣\n");

        for (ClientPlayer i : players) {
            String username = i.getUsername();
            ANSIColor color = ClientUtil.playerColorConversion(i.getColor());
            String resultName = color.getColor() + centeredString(maxUsernameLength, username) + RESET.getColor();
            // "delete" name if player isn't connected
            if (!i.isConnected()) {
                resultName = STRIKETHROUGH.getColor() + resultName;
            }
            int points = i.getPlayground().getPoints();

            str.append("║").append(resultName).append("║");
            str.append(centeredString(maxPointsLength, Integer.toString(points))).append("║\n");
        }
        str.append("╚").append(ClientUtil.appendHorizontalLine(maxUsernameLength)).append("╩").append(ClientUtil.appendHorizontalLine(maxPointsLength)).append("╝\n");

        return str.toString();
    }

    public static void printPlayerHand(List<ClientCard> hand, Side side) {
        int x = GameScreenArea.HAND_CARDS.getScreenPosition().getX();
        int y = GameScreenArea.HAND_CARDS.getScreenPosition().getY();

        List<ClientFace> faces = hand.stream().map(c -> c.getFace(side)).toList();

        for (int i = 0; i < 3; i++) {
            ClientFace face = i < faces.size() ? faces.get(i) : null;
            printCardOutsidePlayground(x, y, face);
            // move cursor after padding
            x += cardWidth + areaPadding;
        }
    }

    /**
     * Need this method because outside playground player needs to know card's requirements
     *
     * @param x    of card print zone
     * @param y    of card print zone
     * @param face (optional) of the card: will decide if print card
     */
    private static void printCardOutsidePlayground(int x, int y, ClientFace face) {
        String[][] toPrint = Optional.ofNullable(face).map(ClientUtil::designCard)
                .orElse(createEmptyArea(cardHeight, cardWidth));

        Map<Symbol, Integer> map = Optional.ofNullable(face).map(ClientFace::getRequirements).orElse(new HashMap<>());

        printToLineColumn(y, x, toPrint);
        printToLineColumn(y + cardHeight, x, createCardRequirementsString(map));
    }

    /**
     * Prints the table of the player's current resources on the screen.
     *
     * @param filler the resources of the player.
     */
    public static void printResourcesArea(Map<Symbol, Integer> filler) {
        printToLineColumn(GameScreenArea.RESOURCES.screenPosition.getX(), GameScreenArea.RESOURCES.screenPosition.getY(), createResourcesTable(filler));
    }

    /**
     * @param resources of the player
     * @return resourceTable as string array
     */
    private static String[] createResourcesTable (Map<Symbol, Integer> resources) {
        int rows = resources.size();
        List<String> setupTable = createEmptyTable(rows);

        String separator = "║";
        Iterator<Map.Entry<Symbol, Integer>> mapIterator = resources.entrySet().iterator();

        Map.Entry<Symbol, Integer> entry = mapIterator.next();
        // print until reach the end of the table
        int i;
        for (i = 1; mapIterator.hasNext(); i += 2, entry = mapIterator.next()) {
            Symbol key = entry.getKey();
            Integer value = entry.getValue();

            String tableLine = separator +
                    centeredString(resourceBoardColumnSpace, printResources(key)) +
                    separator +
                    centeredString(resourceBoardColumnSpace, String.valueOf(value)) +
                    separator;

            setupTable.add(i, tableLine);
        }

        // todo: fix for last element
        Symbol key = entry.getKey();
        Integer value = entry.getValue();

        String tableLine = separator +
                centeredString(resourceBoardColumnSpace, printResources(key)) +
                separator +
                centeredString(resourceBoardColumnSpace, String.valueOf(value)) +
                separator;

        setupTable.add(i, tableLine);

        return setupTable.toArray(new String[0]);
    }

    /**
     * Creates an empty table.
     *
     * @param rows the number of rows in the table.
     * @return an empty table seen as a list of strings.
     */
    private static List<String> createEmptyTable(int rows) {
        String inBetween = "═".repeat(resourceBoardColumnSpace); // space - icon - space
        String beginning;
        String end;
        String separator;

        List<String> mytable = new LinkedList<>();

        int tableIdx = rows + 1; // create table without entries

        for(int i = 1; i <= tableIdx; i++) {
            StringBuilder tableLine = new StringBuilder();

            if (i == 1){
                beginning = "╔";
                separator = "╦";
                end = "╗";
            } else if (i == tableIdx){
                beginning = "╚";
                separator = "╩";
                end = "╝";
            } else {
                beginning = "╠";
                end = "╣";
                separator = "╬";
            }

            tableLine.append(beginning).append(inBetween).append(separator).append(inBetween).append(end);
            mytable.add(tableLine.toString());
        }

        return mytable;
    }

    /**
     * Prints the scoreboard on the screen.
     *
     * @param players the players.
     */
    public static void printScoreboard(List<ClientPlayer> players) {
        printToLineColumn(GameScreenArea.SCOREBOARD.screenPosition.getX(),
                GameScreenArea.SCOREBOARD.screenPosition.getY(),
                createScoreBoard(players));
    }

    /**
     * Clears the screen completely.
     */
    public static void clearScreen() {
        System.out.println("\u001b[2J");
    }

    /**
     * Moves the cursor to the specified <code>line</code> and <code>column</code>.
     *
     * @param line   the line on which the cursor is to be positioned.
     * @param column the column on which the cursor is to be positioned.
     */
    public static void moveCursor(int line, int column) {
        System.out.print("\033[" + line + ";" + column + "H");
    }

    /**
     * Puts the cursor in the input area.
     */
    public static void putCursorToInputArea() {
        // todo: put correct values
        moveCursor(37, 127);
    }

    private static String[][] buildPlayground(ClientPlayground clientPlayground) throws InvalidCardRepresentationException, UnInitializedPlaygroundException, InvalidCardDimensionException {
        DrawablePlayground dp = new DrawablePlayground(7, cardHeight);
        dp.allocateMatrix(clientPlayground);
        // todo: print available in different way
        Set<Position> availablePositions = new HashSet<>();
        Set<Position> occupiedPositions = new HashSet<>();

        for (Position position : clientPlayground.getAllPositions()) {
            if (clientPlayground.getTile(position).sameAvailability(Availability.OCCUPIED))
                occupiedPositions.add(position);
            else if (clientPlayground.getTile(position).sameAvailability(Availability.EMPTY)) {
                availablePositions.add(position);
            }
        }

        // print first available positions (so they don't override corners)
        for (Position pos : availablePositions) {
            dp.drawCard(clientPlayground, pos, drawAvailablePosition(pos));
        }
        // then print occupied positions
        for (Position pos : occupiedPositions) {
            dp.drawCard(clientPlayground, pos, designCard(clientPlayground.getTile(pos).getFace()));
        }

        return dp.getPlaygroundRepresentation();
    }

    public static void printPlayground(ClientPlayground clientPlayground) {
        try {
            String[][]playgroundToPrint = buildPlayground(clientPlayground);
            int playgroundHeight = playgroundToPrint.length;
            int playgroundWidth = playgroundToPrint[0].length;

            // center playground
            int printX = GameScreenArea.PLAYGROUND.screenPosition.getX() + ((GameScreenArea.PLAYGROUND.getWidth() - playgroundWidth) / 2);
            int printY = GameScreenArea.PLAYGROUND.screenPosition.getY() + ((GameScreenArea.PLAYGROUND.getHeight() - playgroundHeight) / 2);

            printToLineColumn(printY,
                    printX,
                    playgroundToPrint);
        } catch (InvalidCardRepresentationException | UnInitializedPlaygroundException | InvalidCardDimensionException e) {
            throw new RuntimeException(e);
        }
    }

    // todo: please update card representation
    public static String[][] drawAvailablePosition(Position pos) {
        String[][] placeHolder = createEmptyArea(cardHeight, cardWidth - 2);

        //upper part
        for (int i = 2; i < cardWidth - 2 - 1; i++) {
            placeHolder[0][i] = "═";
        }

        // middle part
        placeHolder[1][0] = "║";

        placeHolder[1][2] = String.valueOf(pos.getX());
        placeHolder[1][3] = ",";
        placeHolder[1][4] = String.valueOf(pos.getY());

        placeHolder[1][cardWidth - 2 - 1] = "║";

        // fill middle if shorter
        int cardFixedStuffSize = 3; // border of the card + comma
        int positionSize = placeHolder[1][2].length() + placeHolder[1][4].length();
        int availableSpaces = cardWidth - cardFixedStuffSize - positionSize;
        placeHolder[1][1] = " ".repeat((availableSpaces)/2 + (availableSpaces % 2)); // add one space more if needed
        placeHolder[1][5] = " ".repeat((availableSpaces)/2);

        // lower part
        for (int i = cardHeight - 1; i < cardWidth - 2 - 1; i++) {
            placeHolder[cardHeight - 1][i] = "═";
        }

        return placeHolder;
    }

    /**
     * Prints the face up cards on the screen.
     *
     * @param faceUpCards the four face up cards.
     */
    public static void printFaceUpCards(List<ClientCard> faceUpCards) {
        List<ClientFace> faces = faceUpCards.stream().map(c -> c.getFace(Side.FRONT)).toList();

        int relativeY = areaPadding;
        int relativeX = areaPadding;
        for (int i = 0; i < 4; i++) {
            // if new card will go over the faceUpCard area
            if (relativeX + cardWidth > GameScreenArea.FACE_UP_CARDS.width){
                relativeY += cardHeight + areaPadding;
                relativeX = areaPadding;
            }

            ClientFace face = i < faces.size() ? faces.get(i) : null;
            printCardOutsidePlayground(GameScreenArea.FACE_UP_CARDS.screenPosition.getX() + relativeX,
                    GameScreenArea.FACE_UP_CARDS.screenPosition.getY() + relativeY,
                    face);

            relativeX += cardWidth + areaPadding;
        }
    }

    /**
     * Method used to create empty area (to not have null strings to print)
     * @param height of the area
     * @param width of the area
     * @return the empty area
     */
    public static String[][] createEmptyArea(int height, int width) {
        String[][] area = new String[height][width];
        for (String[] row : area) {
            Arrays.fill(row, " ");
        }

        return area;
    }

    /**
     * Modifies the visualization of the message.
     *
     * @param sender    the sender of the message.
     * @param recipient the recipient of the message.
     * @param content   the content of the message.
     * @return the modified message.
     */
    public static String messageModifier(String sender, String recipient, String content) {
        return sender + " -> " + recipient + ": " + content;
    }

    /**
     * Searches for the last messages whose dimension in lines is maximum 9.
     *
     * @param messages the messages in the game.
     * @return the last messages with maximum 9 lines.
     */
    public static List<String> searchForLastNineLineMessages(List<Message> messages) {
        int i = messages.size() - 1, j = 0;
        List<String> messagesToPrint = new ArrayList<>();
        int linesOccupiedByLastMessage;
        String lastSender;
        String lastMessage;
        String lastRecipient;

        while (i >= 0) {
            lastMessage = messages.get(i).getContent();
            lastSender = messages.get(i).getSender();
            lastRecipient = messages.get(i).getRecipient();
            linesOccupiedByLastMessage = (int) Math.ceil((double) (lastMessage.length() + lastSender.length() + lastRecipient.length() + 6) / GameScreenArea.CHAT.getWidth() - 2);
            if (j + linesOccupiedByLastMessage > 9) {
                break;
            }
            messagesToPrint.add(messageModifier(lastSender, lastRecipient, lastMessage));
            j += linesOccupiedByLastMessage;
            i--;
        }
        return messagesToPrint;
    }

    /**
     * Prints the chat on the screen.
     * @param messages the messages in the game.
     */
    public static void printChat(List<Message> messages) {
        List<String> messagesToPrint = searchForLastNineLineMessages(messages);
        int occupiedLines = 0;

        ClientUtil.printChatSquare();
        for (String message : messagesToPrint) {
            int linesOccupiedByLastMessage = (int) Math.ceil((double) (message.length()) / 60);
            occupiedLines += linesOccupiedByLastMessage;
            if (occupiedLines <= 9) {
                writeLine(GameScreenArea.CHAT.getScreenPosition().getX() + 10 - occupiedLines,
                        GameScreenArea.CHAT.getScreenPosition().getY() + 1,
                        GameScreenArea.CHAT.getWidth() - 2,
                        message);
            }
        }
    }

    /**
     * Writes a <code>string</code> in an <code>availableSpace</code> in a specific <code>line</code> and
     * <code>column</code>.
     *
     * @param line           the line on which the <code>string</code> is written.
     * @param column         the column on which the <code>string</code> is written.
     * @param availableSpace total space available for writing.
     * @param string         the string to be written.
     */
    public static void writeLine(int line, int column, int availableSpace, String string) {
        int cnt = 0;
        int onGoingLine = line;

        for (int i = 0; i < string.length(); i++) {
            if (cnt == availableSpace) {
                onGoingLine++;
                cnt = 0;
            }
            System.out.print("\033[" + onGoingLine + ";" + (column + cnt) + "H" + string.charAt(i));
            cnt++;
        }
    }
    public static void eraseLine(int line, int initialColumn, int finalColumn, int numberOfLinesToDelete) {
        if (finalColumn < initialColumn) {
            System.err.println("Final column is less than initial column");
        } else {
            for (int i = 0; i < numberOfLinesToDelete; i++) {
                int currentLine = line + i;
                System.out.print("\033[" + currentLine + ";" + initialColumn + "H");
                for (int j = initialColumn; j < finalColumn + 1; j++) {
                    System.out.print(" ");
                }
            }
            System.out.print("\033[" + (line + numberOfLinesToDelete) + ";0H");
        }
    }

    /**
     * Prints the rulebook on the screen.
     */
    public static void printRulebook(int numberOfPage){
        clearScreen();
        printToLineColumn(GameScreenArea.TITLE.getScreenPosition().getX(),
                GameScreenArea.TITLE.getScreenPosition().getY(),
                ClientUtil.title);
        System.out.println("\n");
        try{
            InputStream rulebookStream = numberOfPage==1?ClientUtil.class.getClassLoader().getResourceAsStream("tui/CODEX_NATURALIS_RULEBOOK_1.txt"):
                    ClientUtil.class.getClassLoader().getResourceAsStream("tui/CODEX_NATURALIS_RULEBOOK_2.txt");
            BufferedReader bufferedReader= null;
            if (rulebookStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(rulebookStream));
            }
            String string;
            if (bufferedReader != null) {
                while((string=bufferedReader.readLine())!=null){
                    System.out.println(string);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prints the golden and resource decks on the screen.
     *
     * @param resourceDeckTop the back of the first resource card found in the resource deck.
     * @param goldenDeckTop   the back of the first golden card found in the golden deck.
     */
    public static void printDecks(ClientFace resourceDeckTop, ClientFace goldenDeckTop) {
        int x = GameScreenArea.DECKS.getScreenPosition().getX();
        int y = GameScreenArea.DECKS.getScreenPosition().getY();

        List<Optional<ClientFace>> decks = new ArrayList<>();
        decks.add(Optional.ofNullable(resourceDeckTop));
        decks.add(Optional.ofNullable(goldenDeckTop));

        for (Optional<ClientFace> d : decks) {
            // convert face to its representation or empty space depending on deck's top value
            String[][] toPrint = d.map(ClientUtil::designCard)
                    .orElse(ClientUtil.createEmptyArea(cardHeight, cardWidth));

            ClientUtil.printToLineColumn(x, y, toPrint);
            // print to adjacent deck area
            y += cardWidth + 2;
        }
    }

    /**
     * Prints the objective cards on the screen.
     *
     * @param objectives the objective cards to print.
     * @param area       the screen area.
     */
    public static void printObjectiveCards(List<ClientObjectiveCard> objectives, GameScreenArea area) {
        int x = area.getScreenPosition().getX();
        int y = area.getScreenPosition().getY();

        for(int i = 0; i < 2; ++i, y += cardWidth + areaPadding) {
            String[][] toPrint = i + 1 <= objectives.size() ?
                    designObjectiveCard(objectives.get(i)) :
                    createEmptyArea(cardHeight, cardWidth);

            ClientUtil.printToLineColumn(x, y, toPrint);
        }
    }

    /**
     * Creates an array with the requirements of the card.
     *
     * @param requirements the requirements to be fulfilled in order to place the card.
     * @return the requirements of the card.
     */
    private static String[] createCardRequirementsString(Map<Symbol, Integer> requirements) {
        String[] requirementsArea = new String[requirementsHeight];
        Arrays.fill(requirementsArea, " ".repeat(cardWidth));

        int i = 0;
        for (Map.Entry<Symbol, Integer> entry : requirements.entrySet()) {
            Symbol key = entry.getKey();
            Integer value = entry.getValue();
            requirementsArea[i] = key.getIcon() + "x" + value;
            ++i;
        }

        return requirementsArea;
    }
}
