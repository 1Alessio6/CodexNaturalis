package it.polimi.ingsw.network.client.view.tui;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.board.ClientTile;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.tui.drawplayground.*;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.card.Symbol.*;
import static it.polimi.ingsw.network.client.view.tui.ANSIColor.*;
import static it.polimi.ingsw.network.client.view.tui.ClientUtil.cardHeight;
import static it.polimi.ingsw.network.client.view.tui.ClientUtil.cardWidth;

/**
 * This enum represents the space and position of each area in the screen
 */
enum GameScreenArea {
    // Matrix (12-1) * (20-1) cards (considering last not overlapping)
    PLAYGROUND(97, 41, new Position(28, 2)),
    FACE_UP_CARDS(24, 14, new Position(146, 2)),
    HAND_CARDS(2* ClientUtil.areaPadding + 3* cardWidth, cardHeight, new Position(62 ,44)),
    DECKS(24, 5, new Position(18, 149)),
    CHAT(62, 11, new Position(23, 126)),
    INPUT_AREA(62,12,new Position(35,126)),
    TITLE(80,5,new Position(2,55)),
    SCOREBOARD(10, 26, new Position(2, 2)),
    PRIVATE_OBJECTIVE(ClientUtil.objectiveCardWidth, ClientUtil.objectiveCardHeight, new Position(35, 2)),
    COMMON_OBJECTIVE(2 + 2 * ClientUtil.objectiveCardWidth, ClientUtil.objectiveCardHeight, new Position(42, 2)),
    RESOURCES(26, 15, new Position(14, 2));

    private final int width;
    private final int height;
    private final Position screenPosition;

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

/**
 * ClientUtil is the class that facilitates TUIs printing
 */
public class ClientUtil {
    public final static int cardWidth = 9;
    public final static int cardHeight = 3;
    public final static int objectiveCardWidth = 11;
    public final static int objectiveCardHeight = 5;
    final static int areaPadding = 2;
    final static int maxUsernameLength=15;
    final static int maxPointsLength=8;
    final static int resourceBoardColumnSpace = 5;
    final static int requirementsHeight = 2;
    final static int[] maxPrintablePlaygroundSize = maxPlaygroundScreenPositions();

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
    static String corner = "\uD83D\uDCCB";
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
     * Prints exception <code>string</code> bellow of the input area.
     *
     * @param string the message to be printed.
     */
    public static void printExceptions(String string) {
        ClientUtil.clearExceptionSpace();

        ClientUtil.writeLine(GameScreenArea.INPUT_AREA.getScreenPosition().getX() +
                        GameScreenArea.INPUT_AREA.getHeight(),
                GameScreenArea.INPUT_AREA.getScreenPosition().getY()+1,
                GameScreenArea.INPUT_AREA.getWidth() - 2,
                string);
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

        // reposition cursor to input area, in order to write in the correct place
        ClientUtil.putCursorToInputArea();
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
        System.out.println("Insert in order the following args:");
        System.out.println("<communication_protocol>: rmi or socket");
        System.out.println("<client_ip>");
        System.out.println("<application_type>: gui or tui");
        System.out.println("Example:\trmi 127.0.0.1 gui");
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
            appendPoints(cardMatrix, pointsCondition, points, 0);
        } else {
            appendInternalResources(resources, cardMatrix, false);
        }
        return cardMatrix;
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
        String[][] cardMatrix = new String[5][9];
        initializeMatrix(cardMatrix);
        ANSIColor color = YELLOW;
        int points=objectiveCard.getScore();
        Map<Position, CardColor> positionCondition = objectiveCard.getPositionCondition();
        Map<Symbol, Integer> resourceCondition = objectiveCard.getResourceCondition();

        int switchCase = positionOrResourcesSwitchCase(positionCondition, resourceCondition);

        if (switchCase == 1) {//it is a card with a position condition
            appendNewResources(new HashMap<>(), cardMatrix, color);
            appendObjectiveMatrixLines(new HashMap<>(), cardMatrix, color);
            paintBackground(cardMatrix, positionCondition);
            appendPoints(cardMatrix, null, points, switchCase);
        } else if (switchCase == 2) {
            appendNewResources(new HashMap<>(), cardMatrix, color);
            appendObjectiveMatrixLines(resourceCondition, cardMatrix, color);
            appendInternalResources(resourceCondition, cardMatrix, true);
            appendPoints(cardMatrix,null,points,switchCase);
        }
        return cardMatrix;
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
        writeLine(GameScreenArea.INPUT_AREA.getScreenPosition().getX()+
                        GameScreenArea.INPUT_AREA.getHeight(),
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
                    cardMatrix[i][j] = color + "═" + RESET;
                }
            }
        }
        cardMatrix[1][0] = color + "║" + RESET;
        if (resourcesSize(resources) == 2) {
            cardMatrix[1][4] = color + doubleHairSpace + "║" + RESET;
        } else if (resourcesSize(resources) == 3) {
            cardMatrix[1][4] = color + hairSpace + "║" + RESET;
        } else if (resourcesSize(resources) == 1 || condition != null) {
            cardMatrix[1][6] = color + " ║" + RESET;
        } else {
            cardMatrix[1][6] = color + "  ║" + RESET;
        }
    }

    /**
     * Appends the outer edges to the <B>objective</B> <code>cardMatrix</code>. It adds the bottom, top and side edges.
     *
     * @param resources  a map containing the resources of the card and their quantities.
     * @param cardMatrix the <B>objective</B> card seen as an array of strings.
     * @param color      the color of the <B>objective</B> card.
     */
    private static void appendObjectiveMatrixLines(Map<Symbol, Integer> resources, String[][] cardMatrix, ANSIColor color) {
        int resourceLine = (cardMatrix.length / 2)+1;
        for (int i = 0; i < cardMatrix.length; i++) {
            if (i == 0 || i == cardMatrix.length - 1) {
                for (int j = 1; j < cardMatrix[0].length - 1; j++) {
                    cardMatrix[i][j] = color + "═" + RESET;
                }
            }
        }
        for (int i = 1; i < cardMatrix.length - 1; i++) {
            cardMatrix[i][0] = color + "║" + RESET;
            if ((resourcesSize(resources) == 1 || resourcesSize(resources) == 2) && i == resourceLine) {
                cardMatrix[i][cardMatrix[0].length - 1] = color + "║" + RESET;
            } else if (resourcesSize(resources) == 3 && i == resourceLine) {
                cardMatrix[i][cardMatrix[0].length - 2] = color + "║" + RESET;
            } else {
                cardMatrix[i][cardMatrix[0].length - 1] = color + "  ║" + RESET;
            }
        }
    }

    /**
     * Appends possible <code>points</code> to the <code>cardMatrix</code>.
     *
     * @param cardMatrix      the card seen as an array of strings.
     * @param pointsCondition the condition to be fulfilled in order to earn the <code>points</code>.
     * @param points          card points.
     */
    private static void appendPoints(String[][] cardMatrix, Condition pointsCondition, int points,int switchCase) {
        if(pointsCondition==null && switchCase==1){
            cardMatrix[2][6] = YELLOW + printPoints(points) + RESET;
        } else if (pointsCondition == null && switchCase == 2) {
            cardMatrix[1][5] = YELLOW + printPoints(points) + RESET;
        }
        else if (pointsCondition == null) {
            cardMatrix[1][4] = YELLOW + printPoints(points) + RESET;
        } else {
            cardMatrix[1][3] = YELLOW + printPoints(points) + RESET;
            cardMatrix[1][4] = YELLOW + "|" + RESET;
            cardMatrix[1][5] = printCondition(pointsCondition);
        }
    }

    /**
     * Appends the <code>resources</code> inside the <code>cardMatrix</code>.
     *
     * @param resources  a map containing the resources of the card and their quantities.
     * @param cardMatrix the card seen as an array of strings.
     */
    private static void appendInternalResources(Map<Symbol, Integer> resources, String[][] cardMatrix,boolean isObjective) {//works only in starter and back cards
        int i = 0;
        int objPad = 0, objMidLinePad = 0;
        int midLine=cardMatrix.length/2;
        if(isObjective){
            objPad=1;
            objMidLinePad = 1;
        }
        for (Map.Entry<Symbol, Integer> entry : resources.entrySet()) {
            for (int j = 0; j < entry.getValue(); j++) {
                if (i == 0) {
                    if (resourcesSize(resources) == 1) {
                        cardMatrix[midLine+objMidLinePad][3+objPad] = printResources(entry.getKey());
                    } else if (resourcesSize(resources) == 2) {
                        cardMatrix[midLine+objMidLinePad][2+objPad] = printResources(entry.getKey());//5//2,3,4
                    } else if (resourcesSize(resources) == 3) {
                        cardMatrix[midLine+objMidLinePad][1+objPad] = printResources(entry.getKey());
                    }
                } else if (i == 1) {
                    if (resourcesSize(resources) == 2) {
                        cardMatrix[midLine+objMidLinePad][3+objPad] = printResources(entry.getKey());
                    } else {
                        cardMatrix[midLine+objMidLinePad][2+objPad] = printResources(entry.getKey());
                    }
                } else if (i == 2) {
                    cardMatrix[midLine+objMidLinePad][3+objPad] = printResources(entry.getKey());
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

        int upperRight = card[0].length - 1;
        int lowerLeft = card.length - 1;


        for (Map.Entry<CornerPosition, Corner> entry : cornerPositionCornerMap.entrySet()) {
            cornerPositions.add(entry.getKey());

            switch (entry.getKey()) {
                case TOP_LEFT -> {
                    if (entry.getValue().isCovered()) {
                        card[0][0] = YELLOW + "═╝" + RESET;
                    } else if (entry.getValue() != null) {
                        card[0][0] = printResources(entry.getValue().getSymbol());
                    }
                }
                case TOP_RIGHT -> {
                    if (entry.getValue().isCovered()) {
                        card[0][upperRight] = YELLOW + "╚═" + RESET;
                    } else if (entry.getValue() != null) {
                        card[0][upperRight] = printResources(entry.getValue().getSymbol());
                    }
                }
                case LOWER_LEFT -> {
                    if (entry.getValue().isCovered()) {
                        card[lowerLeft][0] = YELLOW + "═╗" + RESET;
                    } else if (entry.getValue() != null) {
                        card[lowerLeft][0] = printResources(entry.getValue().getSymbol());
                    }
                }
                case LOWER_RIGHT -> {
                    if (entry.getValue().isCovered()) {
                        card[lowerLeft][upperRight] = YELLOW + "╔═" + RESET;
                    } else if (entry.getValue() != null) {
                        card[lowerLeft][upperRight] = printResources(entry.getValue().getSymbol());
                    }
                }
            }
        }
        if (!cornerPositions.contains(CornerPosition.TOP_LEFT)) {
            card[0][0] = color + "╔═" + RESET;
        }
        if (!cornerPositions.contains(CornerPosition.TOP_RIGHT)) {
            card[0][upperRight] = color + "═╗" + RESET;
        }
        if (!cornerPositions.contains(CornerPosition.LOWER_RIGHT)) {
            card[lowerLeft][upperRight] = color + "═╝" + RESET;
        }
        if (!cornerPositions.contains(CornerPosition.LOWER_LEFT)) {
            card[lowerLeft][0] = color + "╚═" + RESET;
        }

    }

    /**
     * Initialize the inside of the matrix with empty spaces.
     *
     * @param cardMatrix the card seen as an array of strings.
     */
    private static void initializeMatrix(String[][] cardMatrix) {
        for (int i = 0; i < cardMatrix.length; i++) {
            for (int j = 0; j < cardMatrix[0].length; j++) {
                cardMatrix[i][j] = " ";
            }
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
                card[3][3] = cardColorConversion(colors.get(0)) + "○" + RESET;
                card[2][4] = cardColorConversion(colors.get(1)) + "○" + RESET;
                card[1][5] = cardColorConversion(colors.get(2)) + "○" + RESET;
                break;
            case 2:
                card[1][3] = cardColorConversion(colors.get(0)) + "○" + RESET;
                card[2][3] = cardColorConversion(colors.get(1)) + "○" + RESET;
                card[3][4] = cardColorConversion(colors.get(2)) + "○" + RESET;
                break;
            case 3:
                card[1][3] = cardColorConversion(colors.get(0)) + "○" + RESET;
                card[2][4] = cardColorConversion(colors.get(1)) + "○" + RESET;
                card[3][5] = cardColorConversion(colors.get(2)) + "○" + RESET;
                break;
            case 4:
                card[1][3] = cardColorConversion(colors.get(0)) + "○" + RESET;
                card[2][4] = cardColorConversion(colors.get(1)) + "○" + RESET;
                card[3][4] = cardColorConversion(colors.get(2)) + "○" + RESET;
                break;
            case 5:
                card[3][3] = cardColorConversion(colors.get(0)) + "○" + RESET;
                card[2][3] = cardColorConversion(colors.get(1)) + "○" + RESET;
                card[1][4] = cardColorConversion(colors.get(2)) + "○" + RESET;
                break;
            case 6:
                card[3][3] = cardColorConversion(colors.get(0)) + "○" + RESET;
                card[2][4] = cardColorConversion(colors.get(1)) + "○" + RESET;
                card[1][4] = cardColorConversion(colors.get(2)) + "○" + RESET;
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
            String resultName = color + centeredString(maxUsernameLength, username) + RESET;
            // "delete" name if player isn't connected
            if (!i.isConnected()) {
                resultName = STRIKETHROUGH + resultName;
            }
            int points = i.getPlayground().getPoints();

            str.append("║").append(resultName).append("║");
            str.append(centeredString(maxPointsLength, Integer.toString(points))).append("║\n");
        }
        str.append("╚").append(ClientUtil.appendHorizontalLine(maxUsernameLength)).append("╩").append(ClientUtil.appendHorizontalLine(maxPointsLength)).append("╝\n");

        return str.toString();
    }

    /**
     * Prints the cards in the player's hand
     *
     * @param hand the player's hand
     * @param side the side of the cards to be printed
     */
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
        printToLineColumn(GameScreenArea.RESOURCES.getScreenPosition().getX(),
                GameScreenArea.RESOURCES.getScreenPosition().getY(), createResourcesTable(filler));
    }

    /**
     * @param resources of the player
     * @return resourceTable as string array
     */
    private static String[] createResourcesTable (Map<Symbol, Integer> resources) {
        int rows = resources.size();
        List<String> setupTable = createEmptyTable(rows, 2, Collections.nCopies(2, resourceBoardColumnSpace));

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

    private static String[] createWaitingList(List<String> usernames) {
        int rows = usernames.size() + 1;
        List<String> setupTable = createEmptyTable(rows, 1, Collections.singletonList(maxUsernameLength));
        String separator = "║";

        String tableLine = separator +
                centeredString(maxUsernameLength, "USERS CONNECTED") +
                separator;

        setupTable.add(1, tableLine);

        int i = 3;
        for (String username : usernames) {
            tableLine = separator +
                    centeredString(maxUsernameLength, username) +
                    separator;

            setupTable.add(i, tableLine);
            i += 2;
        }

        return setupTable.toArray(new String[0]);
    }

    /**
     * Creates an empty table.
     *
     * @param rows the number of rows in the table.
     * @return an empty table seen as a list of strings.
     */
    private static List<String> createEmptyTable(int rows, int columns, List<Integer> sizes) {
        String beginning;
        String end;
        String separator;

        List<String> myTable = new LinkedList<>();

        int tableIdx = rows + 1; // create table without entries

        for(int i = 1; i <= tableIdx; i++) {
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

            String inBetween = "═".repeat(sizes.getFirst());
            StringBuilder a = new StringBuilder(beginning + inBetween);
            for(int j = 1; j < columns; j++){
                inBetween = "═".repeat(sizes.get(j));
                a.append(separator).append(inBetween);
            }
            a.append(end);

            myTable.add(a.toString());
        }

        return myTable;
    }

    /**
     * Prints the scoreboard on the screen.
     *
     * @param players the players.
     */
    public static void printScoreboard(List<ClientPlayer> players) {
        printToLineColumn(GameScreenArea.SCOREBOARD.getScreenPosition().getX(),
                GameScreenArea.SCOREBOARD.getScreenPosition().getY(),
                createScoreBoard(players));
    }

    /**
     * Prints the list of the players waiting in the lobby
     *
     * @param usernames the usernames of the waiting players
     */
    public static void printWaitingList(List<String> usernames) {
        printToLineColumn(GameScreenArea.SCOREBOARD.getScreenPosition().getX(),
                GameScreenArea.SCOREBOARD.getScreenPosition().getY(),
                createWaitingList(usernames));
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
     * @param screenPos is screen position, relative to upper left corner
     */
    public static void moveCursor(Position screenPos) {
        System.out.print("\033[" + screenPos.getY() + ";" + screenPos.getX() + "H");
    }

    /**
     * Puts the cursor in the input area.
     */
    public static void putCursorToInputArea() {
        // todo: put correct values
        moveCursor(Position.sum(new Position(1,1),
                new Position(GameScreenArea.INPUT_AREA.getScreenPosition().getY(),
                        GameScreenArea.INPUT_AREA.getScreenPosition().getX())));
    }

    /**
     * Method used to build the drawable playground.
     * @param clientPlayground of reference
     * @param currentOffset respect to the centered start print (is the position that will leave exactly half tiles out)
     *                      in both directions
     * @param requestedOffset to add to the current offset
     */
    public static DrawablePlayground buildPlayground(ClientPlayground clientPlayground, Position currentOffset, Position requestedOffset)
            throws InvalidCardRepresentationException, InvalidCardDimensionException, FittablePlaygroundException {
        DrawablePlayground dp = new DrawablePlayground(7, cardHeight); // 7 array cells for each matrix line of the card

        Position[] realLimitPositions = clientPlayground.retrieveTopLeftAndBottomRightPosition();

        // could be retrieved with the sum of numofoverflowingtiles
        int[] entirePlaygroundSizes = DrawablePlayground.calculateSizes(realLimitPositions);
        int[] maxPrintablePlaygroundSizes = ClientUtil.maxPlaygroundScreenPositions();

        // make the playground fit
        // considering only overlapping cards (the last not overlapping is counted in the integer division)
        int finalPlaygroundWidth =  Math.min(maxPrintablePlaygroundSizes[0], entirePlaygroundSizes[0]);
        int finalPlaygroundHeight =  Math.min(maxPrintablePlaygroundSizes[1], entirePlaygroundSizes[1]);
        int[] finalPlaygroundSizes = new int[]{finalPlaygroundWidth, finalPlaygroundHeight};

        dp.allocateMatrix(finalPlaygroundSizes);

        // calculate num of overflowing tiles (0 if it fits)
        int[] numOfOverflowingTiles = new int[] {Math.max(entirePlaygroundSizes[0] - maxPrintablePlaygroundSizes[0], 0),
                Math.max(entirePlaygroundSizes[1] - maxPrintablePlaygroundSizes[1], 0)};

        boolean isOverflowing = Arrays.stream(numOfOverflowingTiles).anyMatch(i -> i != 0);

        // set the temporary upper left printable (before adding the requested offset)
        dp.setStartPrintPos(numOfOverflowingTiles, realLimitPositions[0], finalPlaygroundSizes, currentOffset);

        if (!requestedOffset.equals(new Position(0,0)) && !isOverflowing)
            throw new FittablePlaygroundException();
        else if (isOverflowing) {
            // there is at least a coordinate that overflows
            // normalize offset if the given one is exaggerated (will overwrite it)
            // overwrite the value of the reference, so it can be updated in ClientTUI
            requestedOffset = normalizeRequestedOffset(requestedOffset, dp, realLimitPositions);
        }
        // no need of final else: if position is 0,0 this isn't a problem

        // check if normalized requested offset won't change the displayed area (i.e. 0,0)
        // and check if current offset is 0,0 because real playground fits the screen
        // requested offset = 0,0 is also the case when the system is autoupdating the screen
        if (!currentOffset.equals(new Position(0,0)) && requestedOffset.equals(new Position(0,0)) && isOverflowing)
            throw new FittablePlaygroundException();

        //this will be the playground position to start printing
        dp.setStartPrintPos(numOfOverflowingTiles, realLimitPositions[0], finalPlaygroundSizes,
                Position.sum(currentOffset, !isOverflowing ? new Position(0,0) : requestedOffset));

        Predicate<Position> noOverflowingPosition = isOverflowingPos(dp, finalPlaygroundWidth, finalPlaygroundHeight);

        List<Position> positioningOrderNoOverflow = clientPlayground.getPositioningOrder().stream()
                .filter(noOverflowingPosition).toList();

        Map<Position, ClientTile> fittedArea =  clientPlayground.getAllPositions().stream().filter(noOverflowingPosition)
                .collect(Collectors.toMap(position -> position, clientPlayground::getTile));

        ClientPlayground printablePlayground = new ClientPlayground(fittedArea, positioningOrderNoOverflow);

        // print first available positions (so they don't override corners)
        for (Position pos : printablePlayground.getAvailablePositions()) {
            dp.drawCard(pos, drawAvailablePosition(pos));
        }

        // then print occupied positions. Already ordered
        for (Position pos : printablePlayground.getPositioningOrder()) {
            dp.drawCard(pos, designCard(clientPlayground.getTile(pos).getFace()));
        }

        return dp;
    }

    private static Predicate<Position> isOverflowingPos(DrawablePlayground dp, int finalPlaygroundWidth, int finalPlaygroundHeight) {
        Position newStartPrint = dp.getLimitPositions()[0];
        // minus one: so the last card ends exactly on the end of the matrix
        return p -> !(
                p.getX() < newStartPrint.getX() ||
                        // with = because one card is already considered in the newOffset card
                p.getX() >= newStartPrint.getX() + finalPlaygroundWidth ||
                p.getY() > newStartPrint.getY() ||
                        // with = because one card is already considered in the newOffset card
                p.getY() <= newStartPrint.getY() - finalPlaygroundHeight);
    }

    /**
     * This method will adapt the requested offset, in order to maximize the use of the available space.
     * Offsets are relative to centered offset. P.S: this method will only be called if playground overflows
     * @param dp drawable playground (to take the max and min of the printable playground)
     * @param realLimitPositions of the real playground
     * @return the requested offset, normalized
     */
    // todo: permit requested offset only along overflowing sizes
    private static Position normalizeRequestedOffset(Position requestedOffset, DrawablePlayground dp,
                                                     Position[] realLimitPositions) {
        //Position offsetPos = Position.sum(currentOffset, requestedOffset);
        int[] normalizedPosArr = new int[2];

        // this is the temporary position where printable playground starts
        Position curStartPrint = dp.getLimitPositions()[0];

        // don't consider case equal zero: it's already normalized

        if (requestedOffset.getX() < 0)
            normalizedPosArr[0] = Math.max(realLimitPositions[0].getX() - curStartPrint.getX(), requestedOffset.getX());
        else if (requestedOffset.getX() > 0)
            // maxSize - 1 because one is the card already considered in the current offset
            normalizedPosArr[0] = Math.min(realLimitPositions[1].getX() - curStartPrint.getX() - (maxPrintablePlaygroundSize[0] - 1),
                    requestedOffset.getX());

        if (requestedOffset.getY() > 0)
            normalizedPosArr[1] = Math.min(realLimitPositions[0].getY() - curStartPrint.getY(), requestedOffset.getY());
        else if (requestedOffset.getY() < 0)
            // maxSize - 1 because one is the card already considered in the current offset
            normalizedPosArr[1] = Math.max(realLimitPositions[1].getY() - curStartPrint.getY() + (maxPrintablePlaygroundSize[1] - 1),
                    requestedOffset.getY());

        return new Position(normalizedPosArr[0], normalizedPosArr[1]);
    }

    /**
     * Number of cards that will fit the screen
     */
    public static int[] maxPlaygroundScreenPositions() {
        // consider one card less: just to leave the space
        return new int[]{GameScreenArea.PLAYGROUND.getWidth() / (cardWidth - 1),
                GameScreenArea.PLAYGROUND.getHeight() / (cardHeight - 1)};
    }

    /**
     * Method used to print the playground. In addition, returns the new offset
     * @param clientPlayground of reference
     * @param currentOffset of the playground
     * @param requestedOffset related to current playground position
     */
    public static Position printPlayground(ClientPlayground clientPlayground, Position currentOffset, Position requestedOffset)
            throws UndrawablePlaygroundException {

        DrawablePlayground drawablePlayground = buildPlayground(clientPlayground, currentOffset, requestedOffset);
        String[][] playgroundToPrint = drawablePlayground.getPlaygroundRepresentation();
        int playgroundHeight = playgroundToPrint.length;
        int playgroundWidth = playgroundToPrint[0].length;

        // center playground
        int printX = GameScreenArea.PLAYGROUND.getScreenPosition().getX() + ((GameScreenArea.PLAYGROUND.getWidth() - playgroundWidth) / 2);
        int printY = GameScreenArea.PLAYGROUND.getScreenPosition().getY() + ((GameScreenArea.PLAYGROUND.getHeight() - playgroundHeight) / 2);

        // clear playground area
        printToLineColumn(GameScreenArea.PLAYGROUND.getScreenPosition().getY(),
                GameScreenArea.PLAYGROUND.getScreenPosition().getX(),
                createEmptyArea(GameScreenArea.PLAYGROUND.getHeight(), GameScreenArea.PLAYGROUND.getWidth()));

        // then print playground
        printToLineColumn(printY,
                printX,
                playgroundToPrint);

        return drawablePlayground.getCurrentOffset();
    }

    /**
     * Draws the card located in <code>pos</code>
     * The passed position must be an available position
     *
     * @param pos available position
     * @return a card containing of the position
     */
    // todo: please update card representation
    public static String[][] drawAvailablePosition(Position pos) {
        int matrixCardLength = cardWidth - 2;
        String[][] placeHolder = createEmptyArea(cardHeight, matrixCardLength);

        // upper and lower part
        for(int y = 0; y < cardHeight; y += 2) {
            // add a space in the corner matrix line
            placeHolder[y][0] += " ";
            placeHolder[y][1] = YELLOW + placeHolder[y][1]; // after corners, so it doesn't get overlapped
            placeHolder[y][matrixCardLength - 1] = "  " + RESET;

            for (int x = 1; x < matrixCardLength - 1; x++) {
                placeHolder[y][x] = "═";
            }
        }

        // middle part
        placeHolder[1][0] = YELLOW + "║";

        placeHolder[1][2] = String.valueOf(pos.getX());
        placeHolder[1][3] = ",";
        placeHolder[1][4] = String.valueOf(pos.getY());

        placeHolder[1][matrixCardLength - 1] = "║" + RESET;

        // fill middle if shorter
        int cardFixedStuffSize = 3; // border of the card + comma
        int positionSize = placeHolder[1][2].length() + placeHolder[1][4].length();
        int availableSpaces = cardWidth - cardFixedStuffSize - positionSize;
        placeHolder[1][1] = " ".repeat((availableSpaces)/2 + (availableSpaces % 2)); // add one space more if needed
        placeHolder[1][5] = " ".repeat((availableSpaces)/2);

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
            if (relativeX + cardWidth > GameScreenArea.FACE_UP_CARDS.getWidth()){
                relativeY += cardHeight + areaPadding;
                relativeX = areaPadding;
            }

            ClientFace face = i < faces.size() ? faces.get(i) : null;
            printCardOutsidePlayground(GameScreenArea.FACE_UP_CARDS.getScreenPosition().getX() + relativeX,
                    GameScreenArea.FACE_UP_CARDS.getScreenPosition().getY() + relativeY,
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
    public static List<String> searchForLastNineLineMessages(List<Message> messages,ClientPlayer mainPlayer) {
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
            if(lastSender.equals(mainPlayer.getUsername())||lastRecipient.equals(mainPlayer.getUsername())||lastRecipient.equals("Everyone")){
                messagesToPrint.add(messageModifier(lastSender, lastRecipient, lastMessage));
            }
            j += linesOccupiedByLastMessage;
            i--;
        }
        return messagesToPrint;
    }

    /**
     * Prints the chat on the screen.
     * @param messages the messages in the game.
     */
    public static void printChat(List<Message> messages, ClientPlayer mainPlayer) {
        List<String> messagesToPrint = searchForLastNineLineMessages(messages,mainPlayer);
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
        List<String> split = Arrays.stream(string.split("\n")).map(sub -> {
            List<String> tmp = new ArrayList<>();
            int len = sub.length();
            for (int i = 0; i < len; i += availableSpace) {
                tmp.add(sub.substring(i, i + Math.min(len - i, availableSpace)));
            }

            return tmp;
        }).flatMap(Collection::stream).toList();

        int onGoingLine = line;
        for (String s : split) {
            System.out.print("\033[" + onGoingLine + ";" + column + "H" + s);
            ++onGoingLine;
        }

        //for (int i = 0; i < string.length(); i++) {
        //    char currChar = string.charAt(i);
        //    if (cnt == availableSpace || currChar == '\n') {
        //        onGoingLine++;
        //        cnt = 0;
        //    }

        //    System.out.print("\033[" + onGoingLine + ";" + (column + cnt) + "H" + string.charAt(i));

        //    cnt++;
        //}
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

        for (int i = 0; i < 2; ++i, y += objectiveCardWidth + areaPadding) {
            String[][] toPrint = i + 1 <= objectives.size() ?
                    designObjectiveCard(objectives.get(i)) :
                    createEmptyArea(objectiveCardHeight, objectiveCardWidth);

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

    /**
     * Decides whether the <code>currOffset</code> is exaggerated or not and if it is, corrects it and returns the
     * upper left position from where the printing will start
     *
     * @param playground of reference
     * @param currOffset of the playground
     * @return the adjusted position
     * @throws UndrawablePlaygroundException if an error occurs during the playground representation design
     */
    public static Position printPlayground(ClientPlayground playground, Position currOffset)
            throws UndrawablePlaygroundException {
        return printPlayground(playground, currOffset, new Position(0,0));
    }
}
