package it.polimi.ingsw.network.client.view.tui;

import it.polimi.ingsw.model.Deck.DeckType;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Condition;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.network.client.model.ANSIColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.model.card.Symbol.*;
import static it.polimi.ingsw.network.client.model.ANSIColor.BLUE;
import static it.polimi.ingsw.network.client.model.ANSIColor.YELLOW;

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


    //resources:symbol at the back, requirements:needed symbols that allow to place a card; deckType refers to the card type
    //condition is always 1 or 0
    private static void printCard(ANSIColor color, Map<CornerPosition, Corner> cornerPositionCornerMap, Condition pointsCondition, Map<Symbol, Integer> requirements, Map<Symbol, Integer> resources, int points, DeckType type) {
        StringBuilder str = new StringBuilder();
        int upperSwitchCase = upperCornersNumber(cornerPositionCornerMap);
        int lowerSwitchCase = lowerCornersNumber(cornerPositionCornerMap);

        str.append(printUpperCorners(color, type, cornerPositionCornerMap, resources, new StringBuilder(), upperSwitchCase, lowerSwitchCase, pointsCondition, points));
        printLowerCorners(color, type, cornerPositionCornerMap, resources, str, lowerSwitchCase, requirements);

        System.out.println(str.toString());
    }

    private static void printOptionalCard() {
        StringBuilder str = new StringBuilder();
        printUpperCorners(BLUE, DeckType.RESOURCE, new HashMap<>(), new HashMap<>(), str, 7, 8, null, 0);
        printLowerCorners(BLUE, DeckType.RESOURCE, new HashMap<>(), new HashMap<>(), str, 8, new HashMap<>());
        System.out.println(str.toString());
    }

    private static void printObjectiveCard(ANSIColor color, Map<Position, Color> positionCondition, Map<Symbol, Integer> resourceCondition, int points) {
        StringBuilder str = new StringBuilder();
        int switchCase = positionOrResourcesSwitchCase(positionCondition, resourceCondition);
        if (switchCase == 1) {//it is a card with a position condition
            printUpperCorners(color, DeckType.GOLDEN, new HashMap<>(), new HashMap<>(), str, 7, 8, null, points);
            System.out.println(str);
        } else if (switchCase == 2) {
            printCard(color, new HashMap<>(), null, new HashMap<>(), resourceCondition, points, DeckType.GOLDEN);
        }
    }

    public static StringBuilder printUpperCorners(ANSIColor color, DeckType type, Map<CornerPosition, Corner> cornerPositionSymbolMap, Map<Symbol, Integer> resources, StringBuilder str, int upperSwitchCase, int lowerSwitchCase, Condition condition, int points) {
        str.setLength(0);
        switch (upperSwitchCase) {
            case 1:
                if (type == DeckType.GOLDEN) {
                    str.append(color.getColor()).append("╔═════").append(YELLOW.getColor()).append("╦").append(color.getColor()).append("════════════").append(YELLOW.getColor()).append("╦").append(color.getColor()).append("═════╗\n");
                    str.append(color.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_LEFT).getSymbol())).append(doubleThinSpace).append(YELLOW.getColor()).append(" ║     ");
                    if (condition == null) {
                        str.append(YELLOW.getColor()).append(printPoints(points)).append("    ").append("  ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(color.getColor()).append(" ║\n");
                    } else {
                        str.append(YELLOW.getColor()).append(printPoints(points)).append("|").append(printCondition(condition)).append(doubleThinSpace).append("  ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(color.getColor()).append(" ║\n");
                    }

                } else {
                    str.append(color.getColor()).append("╔═════╦════════════╦═════╗\n");
                    str.append(color.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_LEFT).getSymbol())).append(doubleThinSpace).append(color.getColor()).append(" ║     ");
                    if (condition == null) {
                        str.append(YELLOW.getColor()).append(printPoints(points)).append("     ").append(color.getColor()).append(" ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(" ║\n");
                    } else {
                        str.append(YELLOW.getColor()).append(printPoints(points)).append("|").append(printCondition(condition)).append(doubleThinSpace).append(color.getColor()).append("  ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(" ║\n");
                    }

                }
                break;

            case 2: // just top_left
                if (type == DeckType.GOLDEN) {
                    str.append(color.getColor()).append("╔═════").append(YELLOW.getColor()).append("╦").append(color.getColor()).append("══════════════════╗\n");
                    str.append(color.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_LEFT).getSymbol())).append(doubleThinSpace).append(YELLOW.getColor()).append(" ║     ");
                    if (condition == null) {
                        str.append(YELLOW.getColor()).append(printPoints(points)).append("    ").append(color.getColor()).append("        ║\n");
                    } else {
                        str.append(YELLOW.getColor()).append(printPoints(points)).append("|").append(printCondition(condition)).append(doubleThinSpace).append(color.getColor()).append("        ║\n");
                    }

                } else {
                    str.append(color.getColor()).append("╔═════╦══════════════════╗\n");
                    str.append(color.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_LEFT).getSymbol())).append(doubleThinSpace).append(color.getColor()).append(" ║     ");
                    if (condition == null) {
                        str.append(YELLOW.getColor()).append(printPoints(points)).append("    ").append(color.getColor()).append("        ║\n");
                    } else {
                        str.append(YELLOW.getColor()).append(printPoints(points)).append("|").append(printCondition(condition)).append(doubleThinSpace).append(color.getColor()).append("        ║\n");
                    }

                }
                break;

            case 3://only Top_right
                if (type == DeckType.GOLDEN) {
                    str.append(color.getColor()).append("╔══════════════════").append(YELLOW.getColor()).append("╦").append(color.getColor()).append("═════╗\n");
                    if (condition == null) {
                        str.append(color.getColor()).append("║           ").append(YELLOW.getColor()).append(printPoints(points)).append("    ").append("  ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(color.getColor()).append(" ║\n");
                    } else {
                        str.append(color.getColor()).append("║           ").append(YELLOW.getColor()).append(printPoints(points)).append("|").append(printCondition(condition)).append(doubleThinSpace).append("  ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(color.getColor()).append(" ║\n");
                    }

                } else {
                    str.append(color.getColor()).append("╔══════════════════╦═════╗\n");
                    if (condition == null) {
                        str.append(color.getColor()).append("║         ").append(YELLOW.getColor()).append(printPoints(points)).append("    ").append(color.getColor()).append("    ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(" ║\n");
                    } else {
                        str.append(color.getColor()).append("║           ").append(YELLOW.getColor()).append(printPoints(points)).append("|").append(printCondition(condition)).append(doubleThinSpace).append(color.getColor()).append("  ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.TOP_RIGHT).getSymbol())).append(doubleThinSpace).append(" ║\n");
                    }

                }
                break;

            case 7://no left nor right
                if (condition == null) {
                    str.append(color.getColor()).append("╔════════════════════════╗\n").append("║           ").append(doubleThinSpace).append(thinSpace).append(YELLOW.getColor()).append(printPoints(points)).append("  ").append(threeDoubleSpaces).append(thinSpace).append(hairSpace).append(color.getColor()).append("      ║\n");
                } else {
                    str.append(color.getColor()).append("╔════════════════════════╗\n").append("║           ").append(YELLOW.getColor()).append(printPoints(points)).append("|").append(printCondition(condition)).append(doubleThinSpace).append(color.getColor()).append("        ║\n");
                }

        }
        str.append(drawUpperBorder(resources, upperSwitchCase, lowerSwitchCase, type, color));
        return str;
    }

    public static void printLowerCorners(ANSIColor color, DeckType type, Map<CornerPosition, Corner> cornerPositionSymbolMap, Map<Symbol, Integer> resources, StringBuilder str, int switchCase, Map<Symbol, Integer> requirements) {

        switch (switchCase) {
            case 4://lower_left and lower_right
                if (resourcesSize(resources) < 3) {
                    if (type == DeckType.GOLDEN) {
                        str.append(YELLOW.getColor()).append("╠═════╗            ╔═════╣\n");
                        appendMiddleLowerBorder(switchCase, type, color, cornerPositionSymbolMap, str, requirements);
                    } else {
                        str.append(color.getColor()).append("╠═════╗            ╔═════╣\n");
                        appendMiddleLowerBorder(switchCase, type, color, cornerPositionSymbolMap, str, requirements);
                    }
                } else {
                    appendMiddleLowerBorder(switchCase, type, color, cornerPositionSymbolMap, str, requirements);
                }
                break;


            case 5:
                if (resourcesSize(resources) < 3) {
                    if (type == DeckType.GOLDEN) {
                        str.append(color.getColor()).append("║ ").append(YELLOW.getColor()).append("                 ╔═════╣\n");
                        appendMiddleLowerBorder(switchCase, type, color, cornerPositionSymbolMap, str, requirements);
                    } else {
                        str.append(color.getColor()).append("║                  ╔═════╣\n");
                        appendMiddleLowerBorder(switchCase, type, color, cornerPositionSymbolMap, str, requirements);
                    }
                } else {
                    appendMiddleLowerBorder(switchCase, type, color, cornerPositionSymbolMap, str, requirements);
                }
                break;
            case 6:
                if (resourcesSize(resources) < 3) {
                    if (type == DeckType.GOLDEN) {
                        str.append(YELLOW.getColor()).append("╠═════╗                  ").append(color.getColor()).append("║\n");
                        appendMiddleLowerBorder(switchCase, type, color, cornerPositionSymbolMap, str, requirements);
                    } else {
                        str.append(color.getColor()).append("╠═════╗                  ║\n");
                        appendMiddleLowerBorder(switchCase, type, color, cornerPositionSymbolMap, str, requirements);
                    }
                } else {
                    appendMiddleLowerBorder(switchCase, type, color, cornerPositionSymbolMap, str, requirements);
                }
                break;
            case 8:
                if (resourcesSize(resources) < 3) {
                    str.append(color.getColor()).append("║                        ║\n").append("║       ");
                    appendRequirements(requirements, str);
                    str.append("     ║\n").append("╚════════════════════════╝");
                } else {
                    appendMiddleLowerBorder(switchCase, type, color, cornerPositionSymbolMap, str, requirements);
                }
                break;
        }
    }

    private static void appendMiddleLowerBorder(int switchCase, DeckType type, ANSIColor color, Map<CornerPosition, Corner> cornerPositionSymbolMap, StringBuilder str, Map<Symbol, Integer> requirements) {
        if (type == DeckType.GOLDEN) {
            if (switchCase == 4) {
                str.append(color.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.LOWER_LEFT).getSymbol())).append(doubleThinSpace).append(YELLOW.getColor()).append(" ║");
                appendRequirements(requirements, str);
                str.append(YELLOW.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.LOWER_RIGHT).getSymbol())).append(color.getColor()).append(doubleThinSpace).append(" ║\n");
                str.append(color.getColor()).append("╚═════").append(YELLOW.getColor()).append("╩").append(color.getColor()).append("════════════").append(YELLOW.getColor()).append("╩").append(color.getColor()).append("═════╝\n");
            } else if (switchCase == 5) {//just lower_right
                str.append(color.getColor()).append("║     ");
                appendRequirements(requirements, str);
                str.append(YELLOW.getColor()).append(" ║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.LOWER_RIGHT).getSymbol())).append(doubleThinSpace).append(color.getColor()).append(" ║\n");
                str.append(color.getColor()).append("╚══════════════════").append(YELLOW.getColor()).append("╩").append(color.getColor()).append("═════╝\n");
            } else if (switchCase == 6) {//just lower_left
                str.append(color.getColor()).append("║ ").append(printResources(cornerPositionSymbolMap.get(CornerPosition.LOWER_LEFT).getSymbol())).append(YELLOW.getColor()).append(doubleThinSpace).append(" ║");
                appendRequirements(requirements, str);
                str.append(color.getColor()).append("      ║\n");
                str.append(color.getColor()).append("╚═════").append(YELLOW.getColor()).append("╩").append(color.getColor()).append("══════════════════╝\n");

            } else if (switchCase == 8) {
                str.append(color.getColor()).append("║     ");
                appendRequirements(requirements, str);
                str.append("       ║\n" +
                        "╚════════════════════════╝");
            }
        } else {
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
    }

    private static StringBuilder drawUpperBorder(Map<Symbol, Integer> resources, int upperSwitchCase, int lowerSwitchCase, DeckType type, ANSIColor color) {
        StringBuilder str = new StringBuilder();
        if (resources.isEmpty()) {
            switch (upperSwitchCase) {
                case 1:
                    if (type == DeckType.GOLDEN) {
                        str.append(YELLOW.getColor()).append("╠═════╝");
                        str.append(YELLOW.getColor()).append("            ╚═════╣\n");
                        str.append(color.getColor()).append("║                        ║\n");
                    } else {
                        str.append(color.getColor()).append("╠═════╝");
                        str.append(color.getColor()).append("            ╚═════╣\n");
                        str.append(color.getColor()).append("║                        ║\n");
                    }
                    break;
                case 2:
                    if (type == DeckType.GOLDEN) {
                        str.append(YELLOW.getColor()).append("╠═════╝");
                        str.append(color.getColor()).append("                  ║\n");
                        str.append(color.getColor()).append("║                        ║\n");
                    } else {
                        str.append(color.getColor()).append("╠═════╝");
                        str.append(color.getColor()).append("                  ║\n");
                        str.append(color.getColor()).append("║                        ║\n");

                    }
                    break;
                case 3:
                    if (type == DeckType.GOLDEN) {
                        str.append(color.getColor()).append("║     ");
                        str.append(YELLOW.getColor()).append("             ╚═════╣\n");
                        str.append(color.getColor()).append("║                        ║\n");
                    } else {
                        str.append(color.getColor()).append("║     ");
                        str.append(color.getColor()).append("             ╚═════╣\n");
                        str.append(color.getColor()).append("║                        ║\n");
                    }
                    break;
                case 7:
                    str.append(color.getColor()).append("║                        ║\n").append("║                        ║\n");
            }
        } else {
            if (upperSwitchCase == 1 || upperSwitchCase == 2) {
                if (type == DeckType.GOLDEN) {
                    str.append(YELLOW.getColor()).append("╠═════╝     ").append(thinSpace);
                } else {
                    str.append(color.getColor()).append("╠═════╝     ").append(thinSpace);
                }
            } else if (upperSwitchCase == 3) {
                str.append(color.getColor()).append("║           ").append(thinSpace);

            } else if (upperSwitchCase == 7) {
                str.append(color.getColor()).append("║           ").append(thinSpace);
            }
            appendResources(resources, str, type, color, upperSwitchCase, lowerSwitchCase);
        }
        return str;

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

    private static void appendResources(Map<Symbol, Integer> resources, StringBuilder str, DeckType type, ANSIColor color, int upperSwitchCase, int lowerSwitchCase) {
        //str.setLength(0);
        int i = 0;
        for (Map.Entry<Symbol, Integer> entry : resources.entrySet()) {
            for (int j = 0; j < entry.getValue(); j++) {
                if (i == 0) {
                    if (resourcesSize(resources) != 1) {
                        str.append(printResources(entry.getKey())).append(thinSpace);
                        if (upperSwitchCase == 1 || upperSwitchCase == 3) {
                            if (type == DeckType.GOLDEN) {
                                str.append(YELLOW.getColor()).append("    ╚═════╣\n");
                            } else {
                                str.append(color.getColor()).append("    ╚═════╣\n");
                            }

                        } else if (upperSwitchCase == 2) {
                            str.append(color.getColor()).append("          ║\n");

                        } else if (upperSwitchCase == 7) {
                            str.append(color.getColor()).append("          ║\n");
                        }

                    } else {
                        if (resourcesSize(resources) == 1) {
                            if (upperSwitchCase == 1 || upperSwitchCase == 3) {
                                if (type == DeckType.GOLDEN) {
                                    str.append(YELLOW.getColor()).append(thinSpace).append(hairSpace).append("      ╚═════╣\n");
                                } else {
                                    str.append(color.getColor()).append(thinSpace).append(hairSpace).append("      ╚═════╣\n");
                                }
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
                        if (type == DeckType.GOLDEN) {
                            str.append(YELLOW.getColor()).append("╠═════╗     ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("    ╔═════╣\n");
                        } else {
                            str.append(color.getColor()).append("╠═════╗     ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("    ╔═════╣\n");
                        }
                    } else if (lowerSwitchCase == 5) {
                        if (type == DeckType.GOLDEN) {
                            str.append(color.getColor()).append("║           ").append(YELLOW.getColor()).append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("    ╔═════╣\n");
                        } else {
                            str.append(color.getColor()).append("║           ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("    ╔═════╣\n");
                        }
                    } else if (lowerSwitchCase == 6) {
                        if (type == DeckType.GOLDEN) {
                            str.append(YELLOW.getColor()).append("╠═════╗     ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append(color.getColor()).append("          ║\n");
                        } else {
                            str.append(color.getColor()).append("╠═════╗     ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("          ║\n");
                        }
                    } else if (lowerSwitchCase == 8) {
                        str.append(color.getColor()).append("║           ").append(thinSpace).append(printResources(entry.getKey())).append(thinSpace).append("          ║\n");
                    }
                }
                i++;
            }

        }

    }

    public static void positionCase() {
        ArrayList<Position> caseOne = new ArrayList<>();
        Collections.addAll(caseOne, new Position(0, 0), new Position(1, 1), new Position(2, 2));
        for (Position i : caseOne) {
            System.out.println(i);
        }

    }

    public static int upperCornersNumber(Map<CornerPosition, Corner> cornerPositionCornerMap) {
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

    public static int lowerCornersNumber(Map<CornerPosition, Corner> cornerPositionCornerMap) {
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
            return null;
        }
    }

    private static int positionOrResourcesSwitchCase(Map<Position, Color> positionCondition, Map<Symbol, Integer> resourcesCondition) {
        if (!positionCondition.isEmpty() && resourcesCondition.isEmpty()) {
            return 1;
        } else if (positionCondition.isEmpty() && !resourcesCondition.isEmpty()) {
            return 2;
        } else {
            return 0;
        }
    }
}
