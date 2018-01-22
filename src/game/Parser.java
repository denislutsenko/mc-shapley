package game;

import game.exceptions.InvalidPatternException;
import game.model.Player;

import java.math.BigDecimal;
import java.util.*;

import static game.model.PlayerFactory.getPlayer;

public class Parser {

    private final static String CONJUNCTION = "(&|/\\\\)";

    private final static String NEGATION = "(~|-|!)";

    private final static String RULE_REGEX = "\\{\\w+((&|/\\\\)(~|-|!)*\\w+)*\\}(=>|->|=)(-)*\\d+";

    private final static String EF_RULE_REGEX = "\\{\\w+((&|/\\\\)(~|-|!)*\\w+)*(\\|\\w+((&|/\\\\)(~|-|!)\\w+)*(,\\w+((&|/\\\\)(~|-|!)\\w+)*)*)*\\}(=>|->|=)(-)*\\d+";



    public static Map<Boolean, List<Player>> parsePlayers(String line) throws InvalidPatternException {
        line = trimSpaces(line);

        if (!line.matches(RULE_REGEX)){
            throw new InvalidPatternException("Input is not valid! Syntax: \"{Ben /\\ !John} -> 5\" (without quotes).");
        }

        if (line.contains("{") && line.contains("}")){
            line = line.substring(line.indexOf("{") + 1, line.indexOf("}"));
        }

        Map<Boolean, List<Player>> players = new HashMap<>();
        players.put(true, new ArrayList<>());
        players.put(false, new ArrayList<>());

        String[] names = line.split("&|/\\\\");
        for (String name : names) {
            boolean isPositive = !isNegated(name);
            name = isPositive ? name : name.substring(1);
            players.get(isPositive).add(getPlayer(name));
        }

        return Collections.unmodifiableMap(players);
    }

    private static boolean isNegated(String playerName){
        return playerName.matches("(~|-|!)\\w+");
    }

    public static BigDecimal parseValue(String line) throws InvalidPatternException {
        line = trimSpaces(line);

        if (!line.matches(RULE_REGEX)){
            throw new InvalidPatternException("Input does not match the rule's syntax! Syntax: \"{Ben /\\ !John} -> 5\" (without quotes).");
        }

        line = line.replaceAll("(=>|->|=)", "=");
        String value = line.substring(line.indexOf("=") + 1);
        return new BigDecimal(value);
    }

    public static String convertLineToEf(String line) throws InvalidPatternException {
        line = trimSpaces(line);

        if (!line.matches(EF_RULE_REGEX)){
            throw new InvalidPatternException("Input does not match the rule's syntax! Syntax: \"{Ben /\\ !John} -> 5\" (without quotes).");
        }

        String equals = line.split("\\}")[1];

        if (line.contains("{") && line.contains("}")){
            line = line.substring(line.indexOf("{"), line.indexOf("}"));
        }

        String[] parts = line.split("\\|");
        String rightPart = parts[0];
        String leftPart = parts.length > 1 ? parts[1] : "";
        String[] names = leftPart.split("&|/\\\\|,");
        for (String name : names) {
            if (isNegated(name)){
                continue;
            }
            rightPart += "&!" + name;
        }

        rightPart += "}";
        return rightPart + equals;
    }

    public static String trimSpaces(String line){
        return line.replace(" ", "");
    }
}
