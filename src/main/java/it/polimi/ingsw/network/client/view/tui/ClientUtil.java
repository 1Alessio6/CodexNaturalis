package it.polimi.ingsw.network.client.view.tui;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;

import java.util.*;

import static it.polimi.ingsw.model.card.Symbol.*;
import static it.polimi.ingsw.network.client.view.tui.ANSIColor.*;

public class ClientUtil {

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
    static String threeDoubleSpaces = doubleThinSpace + doubleThinSpace + doubleThinSpace;
    static String doubleHairSpace = "\u200A" + "\u200A";
    static String hairSpace = "\u200A";
    static String corner = "\uD83D\uDDC2️";
    static String one = "\uD835\uDFCF";
    static String two = "\uD835\uDFD0";
    static String three = "\uD835\uDFD1";
    static String four = "\uD835\uDFD2";
    static String five = "\uD835\uDFD3";

    public static void printHelpCommands(Set<GameCommands> consentedCommands) {
        for (GameCommands command : consentedCommands) {
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

    //resources:symbol at the back, requirements:needed symbols that allow to place a card
    //condition is always 1 or 0
    public static void printCard(ClientCard card) {
        ANSIColor color = cardColorConversion(card.getFront().getColor());
        StringBuilder str = new StringBuilder();
        if (card.getBack().getBackCenterResources().isEmpty()) {
            Map<CornerPosition, Corner> cornerPositionCornerMap = card.getFront().getCorners();
            Condition pointsCondition = card.getFront().getPointsCondition();
            Map<Symbol, Integer> requirements = card.getFront().getRequirements();
            Map<Symbol, Integer> resources = card.getFront().getBackCenterResources();
            int points = card.getFront().getScore();

            int upperSwitchCase = upperCornersNumber(cornerPositionCornerMap);
            int lowerSwitchCase = lowerCornersNumber(cornerPositionCornerMap);

            str.append(printUpperCorners(color, cornerPositionCornerMap, resources, new StringBuilder(), upperSwitchCase, lowerSwitchCase, pointsCondition, points));
            printLowerCorners(color, cornerPositionCornerMap, resources, str, lowerSwitchCase, requirements);
        } else {
            Map<CornerPosition, Corner> cornerPositionCornerMap = card.getBack().getCorners();
            Condition pointsCondition = card.getBack().getPointsCondition();
            Map<Symbol, Integer> requirements = card.getBack().getRequirements();
            Map<Symbol, Integer> resources = card.getBack().getBackCenterResources();
            int points = card.getBack().getScore();

            int upperSwitchCase = upperCornersNumber(cornerPositionCornerMap);
            int lowerSwitchCase = lowerCornersNumber(cornerPositionCornerMap);

            str.append(printUpperCorners(color, cornerPositionCornerMap, resources, new StringBuilder(), upperSwitchCase, lowerSwitchCase, pointsCondition, points));
            printLowerCorners(color, cornerPositionCornerMap, resources, str, lowerSwitchCase, requirements);
        }
        System.out.println(str);

    }

    public static void printOptionalCard() {
        StringBuilder str = new StringBuilder();
        printUpperCorners(BLUE, new HashMap<>(), new HashMap<>(), str, 7, 8, null, 0);
        printLowerCorners(BLUE, new HashMap<>(), new HashMap<>(), str, 8, new HashMap<>());
        System.out.println(str);
    }

    public static void printObjectiveCard(ClientObjectiveCard objectiveCard) {
        StringBuilder str = new StringBuilder();
        ANSIColor color = YELLOW;
        Map<Position, CardColor> positionCondition = objectiveCard.getPositionCondition();
        Map<Symbol, Integer> resourceCondition = objectiveCard.getResourceCondition();
        int points = objectiveCard.getScore();
        int switchCase = positionOrResourcesSwitchCase(positionCondition, resourceCondition);

        if (switchCase == 1) {//it is a card with a position condition
            appendPositions(color, positionCondition, str, points);

        } else if (switchCase == 2) {
            printUpperCorners(color, new HashMap<>(), resourceCondition, str, 7, 8, null, points);
            printLowerCorners(color, new HashMap<>(), resourceCondition, str, 8, new HashMap<>());
        }

        System.out.println(str);
    }

    // Secondary methods

    public static String calculateSpaces(int availableSpaces, String string) {
        double numberOfSpaces = ((availableSpaces - string.length()) / 2.0);
        int indexOfDecimal = String.valueOf(numberOfSpaces).indexOf(".");
        String decimal = String.valueOf(0).concat(String.valueOf(numberOfSpaces).substring(indexOfDecimal));
        StringBuilder a = new StringBuilder();


        a.append(" ".repeat((int) Math.max(0, numberOfSpaces)));
        if (decimal.equals(String.valueOf(.5))) {
            a.append(doubleHairSpace);
        }

        return a.toString();
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

    private static int requirementsSize(Map<Symbol, Integer> requirements) {
        int i = 0;
        for (Map.Entry<Symbol, Integer> entry : requirements.entrySet()) {
            i += entry.getValue();
        }
        return i;
    }

    private static StringBuilder printUpperCorners(ANSIColor color, Map<CornerPosition, Corner> cornerPositionSymbolMap, Map<Symbol, Integer> resources, StringBuilder str, int upperSwitchCase, int lowerSwitchCase, Condition condition, int points) {
        str.setLength(0);
        switch (upperSwitchCase) {
            case 1:
                str.append(color.getColor()).append("╔═════╦════════════╦═════╗\n");
                str.append(color.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_LEFT).getSymbol())).append(doubleThinSpace).append(color.getColor()).append(" ║     ");
                if (condition == null) {
                    str.append(YELLOW.getColor()).append(" ").append(printPoints(points)).append("    ").append(color.getColor()).append(" ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(" ║\n");
                } else {
                    str.append(YELLOW.getColor()).append(printPoints(points)).append("|").append(printCondition(condition)).append(doubleThinSpace).append(color.getColor()).append("  ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(" ║\n");
                }

                break;

            case 2: // just top_left
                str.append(color.getColor()).append("╔═════╦══════════════════╗\n");
                str.append(color.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_LEFT).getSymbol())).append(doubleThinSpace).append(color.getColor()).append(" ║     ");
                if (condition == null) {
                    str.append(YELLOW.getColor()).append(" ").append(printPoints(points)).append("   ").append(color.getColor()).append("        ║\n");
                } else {
                    str.append(YELLOW.getColor()).append(printPoints(points)).append("|").append(printCondition(condition)).append(doubleThinSpace).append(color.getColor()).append("        ║\n");
                }
                break;

            case 3://only Top_right

                str.append(color.getColor()).append("╔══════════════════╦═════╗\n");
                if (condition == null) {
                    str.append(color.getColor()).append("║            ").append(YELLOW.getColor()).append(printPoints(points)).append(color.getColor()).append("     ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(" ║\n");
                } else {
                    str.append(color.getColor()).append("║           ").append(YELLOW.getColor()).append(printPoints(points)).append("|").append(printCondition(condition)).append(doubleThinSpace).append(color.getColor()).append("  ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(" ║\n");
                }
                break;

            case 7://no left nor right
                if (condition == null) {
                    str.append(color.getColor()).append("╔════════════════════════╗\n").append("║           ").append(doubleThinSpace).append(thinSpace).append(YELLOW.getColor()).append(printPoints(points)).append("  ").append(threeDoubleSpaces).append(thinSpace).append(hairSpace).append(color.getColor()).append("      ║\n");
                } else {
                    str.append(color.getColor()).append("╔════════════════════════╗\n").append("║           ").append(YELLOW.getColor()).append(printPoints(points)).append("|").append(printCondition(condition)).append(doubleThinSpace).append(color.getColor()).append("        ║\n");
                }

        }
        str.append(drawUpperBorder(resources, upperSwitchCase, lowerSwitchCase, color));
        return str;
    }

    private static void printLowerCorners(ANSIColor color, Map<CornerPosition, Corner> cornerPositionSymbolMap, Map<Symbol, Integer> resources, StringBuilder str, int switchCase, Map<Symbol, Integer> requirements) {

        switch (switchCase) {
            case 4://lower_left and lower_right
                if (resourcesSize(resources) < 3) {
                    str.append(color.getColor()).append("╠═════╗            ╔═════╣\n");
                    appendMiddleLowerBorder(switchCase, color, cornerPositionSymbolMap, str, requirements);

                } else {
                    appendMiddleLowerBorder(switchCase, color, cornerPositionSymbolMap, str, requirements);
                }
                break;


            case 5:
                if (resourcesSize(resources) < 3) {
                    str.append(color.getColor()).append("║                  ╔═════╣\n");
                    appendMiddleLowerBorder(switchCase, color, cornerPositionSymbolMap, str, requirements);

                } else {
                    appendMiddleLowerBorder(switchCase, color, cornerPositionSymbolMap, str, requirements);
                }
                break;
            case 6:
                if (resourcesSize(resources) < 3) {
                    str.append(color.getColor()).append("╠═════╗                  ║\n");
                    appendMiddleLowerBorder(switchCase, color, cornerPositionSymbolMap, str, requirements);

                } else {
                    appendMiddleLowerBorder(switchCase, color, cornerPositionSymbolMap, str, requirements);
                }
                break;
            case 8:
                if (resourcesSize(resources) < 3) {
                    str.append(color.getColor()).append("║                        ║\n").append("║       ");
                    appendRequirements(requirements, str);
                    str.append("     ║\n").append("╚════════════════════════╝");
                } else {
                    appendMiddleLowerBorder(switchCase, color, cornerPositionSymbolMap, str, requirements);
                }
                break;
        }
    }

    private static void appendMiddleLowerBorder(int switchCase, ANSIColor color, Map<CornerPosition, Corner> cornerPositionSymbolMap, StringBuilder str, Map<Symbol, Integer> requirements) {
        if (switchCase == 4) {
            str.append(color.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.LOWER_LEFT).getSymbol())).append(doubleThinSpace).append(color.getColor()).append(" ║");
            appendRequirements(requirements, str);
            str.append(color.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.LOWER_RIGHT).getSymbol())).append(doubleThinSpace).append(color.getColor()).append(" ║\n");
            str.append(color.getColor()).append("╚═════╩════════════╩═════╝\n");
        } else if (switchCase == 5) {//just lower_right
            str.append(color.getColor()).append("║     ");
            appendRequirements(requirements, str);
            str.append(color.getColor()).append(" ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.LOWER_RIGHT).getSymbol())).append(color.getColor()).append(doubleThinSpace).append(" ║\n");
            str.append(color.getColor()).append("╚══════════════════╩═════╝\n");
        } else if (switchCase == 6) {//just lower_left
            str.append(color.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.LOWER_LEFT).getSymbol())).append(color.getColor()).append(doubleThinSpace).append(" ║");
            appendRequirements(requirements, str);
            str.append(color.getColor()).append("      ║\n");
            str.append(color.getColor()).append("╚═════╩══════════════════╝\n");

        } else if (switchCase == 8) {
            str.append(color.getColor()).append("║     ");
            appendRequirements(requirements, str);
            str.append("       ║\n" +
                    "╚════════════════════════╝");
        }
    }

    private static StringBuilder drawUpperBorder(Map<Symbol, Integer> resources, int upperSwitchCase, int lowerSwitchCase, ANSIColor color) {
        StringBuilder str = new StringBuilder();
        if (resources.isEmpty()) {
            switch (upperSwitchCase) {
                case 1:
                    str.append(color.getColor()).append("╠═════╝");
                    str.append(color.getColor()).append("            ╚═════╣\n");
                    str.append(color.getColor()).append("║                        ║\n");
                    break;
                case 2:
                    str.append(color.getColor()).append("╠═════╝");
                    str.append(color.getColor()).append("                  ║\n");
                    str.append(color.getColor()).append("║                        ║\n");
                    break;
                case 3:
                    str.append(color.getColor()).append("║     ");
                    str.append(color.getColor()).append("             ╚═════╣\n");
                    str.append(color.getColor()).append("║                        ║\n");
                    break;
                case 7:
                    str.append(color.getColor()).append("║                        ║\n").append("║                        ║\n");
            }
        } else {
            if (upperSwitchCase == 1 || upperSwitchCase == 2) {
                str.append(color.getColor()).append("╠═════╝     ").append(thinSpace);
            } else if (upperSwitchCase == 3) {
                str.append(color.getColor()).append("║           ").append(thinSpace);
            } else if (upperSwitchCase == 7) {
                str.append(color.getColor()).append("║           ").append(thinSpace);
            }
            appendResources(resources, str, color, upperSwitchCase, lowerSwitchCase);
        }
        return str;

    }

    private static void appendPositions(ANSIColor color, Map<Position, CardColor> positionCondition, StringBuilder str, int points) {


        switch (positionCase(positionCondition)) {
            case 1:
                str.append(color.getColor()).append("╔════════════════════════╗\n").append("║            ").append(YELLOW.getColor()).append(printPoints(points)).append(color.getColor()).append("           ║\n");
                str.append(color.getColor()).append("║            ").append(cardColorConversion(positionCondition.get(new Position(2, 2))).getColor()).append("┌‐‐‐┐       ").append(color.getColor()).append("║\n");
                str.append(color.getColor()).append("║         ").append(cardColorConversion(positionCondition.get(new Position(1, 1))).getColor()).append("┌‐‐*┐").append(cardColorConversion(positionCondition.get(new Position(2, 2))).getColor()).append("‐‐┘       ").append(color.getColor()).append("║\n");
                str.append(color.getColor()).append("║     ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("┌‐‐*┐").append(cardColorConversion(positionCondition.get(new Position(1, 1))).getColor()).append("‐‐‐┘").append(color.getColor()).append("          ║\n");
                str.append(color.getColor()).append("║     ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("└‐‐‐┘").append("              ").append("║\n");
                str.append(color.getColor()).append("╚════════════════════════╝\n");
                break;
            case 2:
                str.append(color.getColor()).append("╔════════════════════════╗\n");
                str.append(color.getColor()).append("║     ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("┌‐‐‐┐").append(color.getColor()).append("  ").append(YELLOW.getColor()).append(printPoints(points)).append(color.getColor()).append("           ║\n");
                str.append(color.getColor()).append("║     ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("└‐‐‐┘").append(color.getColor()).append("              ║\n");
                str.append(color.getColor()).append("║     ").append(cardColorConversion(positionCondition.get(new Position(0, -2))).getColor()).append("┌‐‐‐┐").append(color.getColor()).append("              ║\n");
                str.append(color.getColor()).append("║     ").append(cardColorConversion(positionCondition.get(new Position(0, -2))).getColor()).append("└‐‐‐").append(cardColorConversion(positionCondition.get(new Position(1, -3))).getColor()).append("┌*‐‐┐").append(color.getColor()).append("          ║\n");
                str.append(color.getColor()).append("║         ").append(cardColorConversion(positionCondition.get(new Position(1, -3))).getColor()).append("└‐‐‐┘").append(color.getColor()).append("          ║\n");
                str.append(color.getColor()).append("╚════════════════════════╝\n");
                break;

            case 3:
                str.append(color.getColor()).append("╔════════════════════════╗\n");
                str.append(color.getColor()).append("║            ").append(YELLOW.getColor()).append(printPoints(points)).append(color.getColor()).append("           ║\n");
                str.append(color.getColor()).append("║     ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("┌‐‐‐┐").append(color.getColor()).append("              ║\n");
                str.append(color.getColor()).append("║     ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("└‐‐‐").append(cardColorConversion(positionCondition.get(new Position(1, -1))).getColor()).append("┌*‐‐┐").append(color.getColor()).append("          ║\n");
                str.append(color.getColor()).append("║         ").append(cardColorConversion(positionCondition.get(new Position(1, -1))).getColor()).append("└‐‐‐").append(cardColorConversion(positionCondition.get(new Position(2, -2))).getColor()).append("┌*‐‐┐").append(color.getColor()).append("      ║\n");
                str.append(color.getColor()).append("║             ").append(cardColorConversion(positionCondition.get(new Position(2, -2))).getColor()).append("└‐‐‐┘").append(color.getColor()).append("      ║\n");
                str.append(color.getColor()).append("╚════════════════════════╝\n");
                break;

            case 4:
                str.append(color.getColor()).append("╔════════════════════════╗\n");
                str.append(color.getColor()).append("║     ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("┌‐‐‐┐ ").append(YELLOW.getColor()).append(printPoints(points)).append(color.getColor()).append("            ║\n");
                str.append(color.getColor()).append("║     ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("└‐‐‐").append(cardColorConversion(positionCondition.get(new Position(1, -1))).getColor()).append("┌*‐‐┐").append(color.getColor()).append("          ║\n");
                str.append(color.getColor()).append("║         ").append(cardColorConversion(positionCondition.get(new Position(1, -1))).getColor()).append("└‐‐‐┘").append(color.getColor()).append("          ║\n");
                str.append(color.getColor()).append("║         ").append(cardColorConversion(positionCondition.get(new Position(1, -3))).getColor()).append("┌‐‐‐┐").append(color.getColor()).append("          ║\n");
                str.append(color.getColor()).append("║         ").append(cardColorConversion(positionCondition.get(new Position(1, -3))).getColor()).append("└‐‐‐┘").append(color.getColor()).append("          ║\n");
                str.append(color.getColor()).append("╚════════════════════════╝\n");
                break;

            case 5:
                str.append(color.getColor()).append("╔════════════════════════╗\n");
                str.append(color.getColor()).append("║           ").append(YELLOW.getColor()).append(printPoints(points)).append(cardColorConversion(positionCondition.get(new Position(1, 3))).getColor()).append(" ┌‐‐‐┐").append(color.getColor()).append("      ║\n");
                str.append(color.getColor()).append("║          ").append(cardColorConversion(positionCondition.get(new Position(0, 2))).getColor()).append("┌‐‐*┐").append(cardColorConversion(positionCondition.get(new Position(1, 3))).getColor()).append("‐‐┘").append(color.getColor()).append("      ║\n");
                str.append(color.getColor()).append("║          ").append(cardColorConversion(positionCondition.get(new Position(0, 2))).getColor()).append("└‐‐‐┘").append(color.getColor()).append("         ║\n");
                str.append(color.getColor()).append("║          ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("┌‐‐‐┐").append(color.getColor()).append("         ║\n");
                str.append(color.getColor()).append("║          ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("└‐‐‐┘").append(color.getColor()).append("         ║\n");
                str.append(color.getColor()).append("╚════════════════════════╝");
                break;

            case 6:
                str.append(color.getColor()).append("╔════════════════════════╗\n");
                str.append(color.getColor()).append("║           ").append(YELLOW.getColor()).append(printPoints(points)).append(cardColorConversion(positionCondition.get(new Position(1, 3))).getColor()).append(" ┌‐‐‐┐").append(color.getColor()).append("      ║\n");
                str.append(color.getColor()).append("║             ").append(cardColorConversion(positionCondition.get(new Position(1, 3))).getColor()).append("└‐‐‐┘").append(color.getColor()).append("      ║\n");
                str.append(color.getColor()).append("║             ").append(cardColorConversion(positionCondition.get(new Position(1, 1))).getColor()).append("┌‐‐‐┐").append(color.getColor()).append("      ║\n");
                str.append(color.getColor()).append("║         ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("┌‐‐*┐").append(cardColorConversion(positionCondition.get(new Position(1, 1))).getColor()).append("‐‐‐┘").append(color.getColor()).append("      ║\n");
                str.append(color.getColor()).append("║         ").append(cardColorConversion(positionCondition.get(new Position(0, 0))).getColor()).append("└‐‐‐┘").append(color.getColor()).append("          ║\n");
                str.append(color.getColor()).append("╚════════════════════════╝\n");
                break;


        }

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
        };
    }

    private static void appendRequirements(Map<Symbol, Integer> requirements, StringBuilder str) {
        if (requirements.isEmpty()) {
            str.append("            ");
        } else {
            spacesHandler(requirements, str);

            for (Map.Entry<Symbol, Integer> entry : requirements.entrySet()) {
                for (int j = 0; j < entry.getValue(); j++) {
                    str.append(printResources(entry.getKey()));
                }
            }
            spacesHandler(requirements, str);
        }
    }

    private static void spacesHandler(Map<Symbol, Integer> requirements, StringBuilder str) {
        if (requirementsSize(requirements) == 1) {
            str.append("    ").append(doubleHairSpace).append(thinSpace);
        } else if (requirementsSize(requirements) == 2) {
            str.append("   ").append(doubleThinSpace);
        } else if (requirementsSize(requirements) == 3) {
            str.append(thinSpace).append(threeDoubleSpaces);
        } else if (requirementsSize(requirements) == 4) {
            str.append(" ").append(doubleHairSpace);
        } else if (requirementsSize(requirements) == 5) {
            str.append(thinSpace);
        }
    }

    private static void appendResources(Map<Symbol, Integer> resources, StringBuilder str, ANSIColor color, int upperSwitchCase, int lowerSwitchCase) {
        //str.setLength(0);
        int i = 0;
        for (Map.Entry<Symbol, Integer> entry : resources.entrySet()) {
            for (int j = 0; j < entry.getValue(); j++) {
                if (i == 0) {
                    if (resourcesSize(resources) != 1) {
                        str.append(printResources(entry.getKey())).append(thinSpace);
                        if (upperSwitchCase == 1 || upperSwitchCase == 3) {
                            str.append(color.getColor()).append("    ╚═════╣\n");
                        } else if (upperSwitchCase == 2) {
                            str.append(color.getColor()).append("          ║\n");

                        } else if (upperSwitchCase == 7) {
                            str.append(color.getColor()).append("          ║\n");
                        }

                    } else {
                        if (resourcesSize(resources) == 1) {
                            if (upperSwitchCase == 1 || upperSwitchCase == 3) {
                                str.append(color.getColor()).append(thinSpace).append(hairSpace).append("      ╚═════╣\n");
                            } else {
                                str.append(color.getColor()).append(thinSpace).append(hairSpace).append("            ║\n");
                            }

                            str.append(color.getColor()).append("║           ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("          ║\n");
                        }
                    }
                } else if (i == 1) {
                    str.append(color.getColor()).append("║           ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("          ║\n");
                } else if (i == 2) {//verify
                    if (lowerSwitchCase == 4) {
                        str.append(color.getColor()).append("╠═════╗     ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("    ╔═════╣\n");
                    } else if (lowerSwitchCase == 5) {
                        str.append(color.getColor()).append("║           ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("    ╔═════╣\n");
                    } else if (lowerSwitchCase == 6) {
                        str.append(color.getColor()).append("╠═════╗     ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("          ║\n");
                    } else if (lowerSwitchCase == 8) {
                        str.append(color.getColor()).append("║           ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("          ║\n");
                    }
                }
                i++;
            }

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

    private static int upperCornersNumber(Map<CornerPosition, Corner> cornerPositionCornerMap) {
        ArrayList<CornerPosition> cornerPositions = new ArrayList<>();

        for (Map.Entry<CornerPosition, Corner> entry : cornerPositionCornerMap.entrySet()) {
            cornerPositions.add(entry.getKey());
        }

        if (cornerPositions.contains(CornerPosition.TOP_LEFT) && cornerPositions.contains(CornerPosition.TOP_RIGHT)) {
            return 1;
        } else if (cornerPositions.contains(CornerPosition.TOP_LEFT) && !cornerPositions.contains(CornerPosition.TOP_RIGHT)) {
            return 2;
        } else if (!cornerPositions.contains(CornerPosition.TOP_LEFT) && cornerPositions.contains(CornerPosition.TOP_RIGHT)) {
            return 3;
        } else if (!cornerPositions.contains(CornerPosition.TOP_LEFT) && !cornerPositions.contains(CornerPosition.TOP_RIGHT)) {
            return 7;
        } else {
            return -1;
        }
    }

    private static int lowerCornersNumber(Map<CornerPosition, Corner> cornerPositionCornerMap) {
        ArrayList<CornerPosition> cornerPositions = new ArrayList<>();
        for (Map.Entry<CornerPosition, Corner> entry : cornerPositionCornerMap.entrySet()) {
            cornerPositions.add(entry.getKey());
        }

        if (cornerPositions.contains(CornerPosition.LOWER_RIGHT) && cornerPositions.contains(CornerPosition.LOWER_LEFT)) {
            return 4;
        } else if (cornerPositions.contains(CornerPosition.LOWER_RIGHT) && !cornerPositions.contains(CornerPosition.LOWER_LEFT)) {
            return 5;
        } else if (!cornerPositions.contains(CornerPosition.LOWER_RIGHT) && cornerPositions.contains(CornerPosition.LOWER_LEFT)) {
            return 6;
        } else if (!cornerPositions.contains(CornerPosition.LOWER_RIGHT) && !cornerPositions.contains(CornerPosition.LOWER_LEFT)) {
            return 8;
        } else {
            return -1;
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
}
